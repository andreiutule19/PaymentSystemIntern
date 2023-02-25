package com.example.paymentsystem.service.impl;


import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.enums.Status;
import com.example.paymentsystem.exception.ResourceNotFoundException;
import com.example.paymentsystem.history.UserHistory;
import com.example.paymentsystem.repository.UserHistoryRepository;
import com.example.paymentsystem.repository.UserRepository;
import com.example.paymentsystem.security.JwtTokenUtil;
import com.example.paymentsystem.service.sketch.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private List<UserHistory> listHistoryApprove() {
        Set< UserHistory> historySet = new TreeSet<>(userHistoryRepository.findAll());
        List<String> usernames = historySet.stream().map( UserHistory::getUsername).toList();
        List<UserHistory> userHistoryList= new ArrayList<>();
        for(String username : usernames){
            List<UserHistory> userHistories= userHistoryRepository.
                    findUserHistoriesByUsernameOrderByTimestampDesc(username);
            userHistoryList.add(userHistories.get(0));
        }
        return  userHistoryList;
    }

    private UserHistory setUserHistory(UserDTO user,Status currentStatus,Status lastStatus,Boolean register){
        if(register){
            return new UserHistory(
                    user.getEmail(),new BCryptPasswordEncoder().encode(user.getPassword()),
                    user.getFullName(),user.getAddress(),currentStatus,lastStatus, user.getExecutor(),
                    user.getUsername() );
        }

        return new UserHistory(
                user.getEmail(),null,
                user.getFullName(),user.getAddress(),currentStatus,lastStatus, user.getExecutor(),
                user.getUsername() );

    }


    private UserHistory setUserHistoryApprove(ApproveDTO user,Status currentStatus,Status lastStatus,String password){
            return new UserHistory(
                    user.getEmail(),password,
                    user.getFullName(),user.getAddress(),currentStatus,lastStatus, user.getExecutor(),
                    user.getUsername() );
    }


    @Override
    @Transactional
    public UserHistory insert(UserDTO user) {
        return userHistoryRepository.save(
                this.setUserHistory(user,Status.PENDING_ACTIVE,null,true)
        );
    }

    @Override
    @Transactional
    public UserHistory remove(UserDTO user) {
        if(!user.getStatus().equals(Status.REMOVED)) {
            return userHistoryRepository.save(
                    this.setUserHistory(user,Status.PENDING_REMOVE,user.getStatus(),false)
            );
        }
        else {
            throw new ResourceNotFoundException("User","Username",user.getUsername(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @Transactional
    public UserHistory update(UserDTO user) {
        if(!user.getStatus().equals(Status.REMOVED)) {
            return userHistoryRepository.save(
                    this.setUserHistory(user,Status.PENDING_MODIFY,user.getStatus(),false)
            );
        }
        else{
            throw new ResourceNotFoundException("User","Username",user.getUsername(), HttpStatus.NOT_ACCEPTABLE);
        }
    }




    @Override
    @Transactional
    public List<HistoryDTO> mapApproveTable(){
        return this.listHistoryApprove().stream().filter(userHistory -> !userHistory.getCurrentStatus().equals(Status.ACTIVE) && !userHistory.getCurrentStatus().equals(Status.MODIFIED) && !userHistory.getCurrentStatus().equals(Status.REMOVED)).map(history ->
                new HistoryDTO(history.getUsername(),history.getEmail(),
                        history.getFullName(),history.getAddress(),history.getCurrentStatus(),
                        history.getLastStatus(),history.getExecutor(),history.getTimestamp())).collect(Collectors.toList());
    }



    @Override
    @Transactional
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserDTO(user.getUsername(),user.getEmail(),
                user.getFullName(),user.getAddress(),user.getCurrentStatus(),null,null)).collect(Collectors.toList());
    }
    


    @Override
    @Transactional
    public void handleUser(ApproveDTO willBeApprovedUser) {
        UserHistory verifyUser = null;
        for(UserHistory y : this.listHistoryApprove()){
            if(y.getUsername().equals(willBeApprovedUser.getUsername())){
                verifyUser= y;
                break;
            }
        }
        if (verifyUser != null) {
            if(verifyUser.getExecutor().equals(willBeApprovedUser.getExecutor()) || willBeApprovedUser.getExecutor().equals(willBeApprovedUser.getUsername())){
                throw new ResourceNotFoundException("User","Username",willBeApprovedUser.getUsername(), HttpStatus.NOT_ACCEPTABLE);
            }
            else {
                if (verifyUser.getCurrentStatus().equals(Status.PENDING_ACTIVE)) {
                    UserHistory local = userHistoryRepository.findUserHistoryByUsernameAndEmail(verifyUser.getUsername(), verifyUser.getEmail());
                    User newUser = new User(willBeApprovedUser.getUsername(), willBeApprovedUser.getEmail(), local.getPassword(),
                            willBeApprovedUser.getFullName(), willBeApprovedUser.getAddress(), Status.ACTIVE);
                    userHistoryRepository.save(
                            this.setUserHistoryApprove(willBeApprovedUser,Status.ACTIVE,Status.PENDING_ACTIVE, local.getPassword())
                    );
                    userRepository.save(newUser);
                }
                if (verifyUser.getCurrentStatus().equals(Status.PENDING_MODIFY)) {
                    User newUser = userRepository.findUserByUsername(verifyUser.getUsername());
                    newUser.setCurrentStatus(Status.MODIFIED);
                    newUser.setUsername(willBeApprovedUser.getUsername());
                    newUser.setEmail(willBeApprovedUser.getEmail());
                    newUser.setAddress(willBeApprovedUser.getAddress());
                    newUser.setFullName(willBeApprovedUser.getFullName());
                    userHistoryRepository.save(
                            this.setUserHistoryApprove(willBeApprovedUser,Status.MODIFIED,Status.PENDING_MODIFY,null)
                    );
                    userRepository.save(newUser);
                }
                if (verifyUser.getCurrentStatus().equals(Status.PENDING_REMOVE)) {
                    User newUser = userRepository.findUserByUsername(verifyUser.getUsername());
                    newUser.setCurrentStatus(Status.REMOVED);
                    userHistoryRepository.save(
                            this.setUserHistoryApprove(willBeApprovedUser,Status.REMOVED,Status.PENDING_REMOVE,null)
                    );
                    userRepository.save(newUser);
                }
            }
        }


    }


    @Override
    @Transactional
    public AuthDTO logIn(AuthDTO authDTO) {
        User user = userRepository.findUserByUsername(authDTO.getUsername());

        if(user == null){
            throw new ResourceNotFoundException("User","username",authDTO.getUsername(), HttpStatus.NOT_FOUND);
        }
        else{
            if(!(new BCryptPasswordEncoder().matches(authDTO.getPassword(), user.getPassword()))){
                throw new ResourceNotFoundException("User","Username",authDTO.getUsername(), HttpStatus.NOT_FOUND);
            }
        }
        authDTO.setToken(jwtTokenUtil.generateToken(authDTO));

        return authDTO;
    }

    @Override
    @Transactional
    public UserHistory changePassword(ChangePasswordDTO passwordDTO) {

        System.out.println(userRepository.findUserByUsername(passwordDTO.getExecutor()).getPassword());
        if(new BCryptPasswordEncoder().matches(passwordDTO.getOldPassword(),userRepository.findUserByUsername(passwordDTO.getExecutor()).getPassword())) {
            User user = userRepository.findUserByUsername(passwordDTO.getExecutor());
            if(!user.getCurrentStatus().equals(Status.REMOVED)) {
                return userHistoryRepository.save(new UserHistory(
                        user.getEmail(),new BCryptPasswordEncoder().encode(passwordDTO.getPassword()),
                        user.getFullName(),user.getAddress(),Status.PENDING_MODIFY,user.getCurrentStatus(),
                        passwordDTO.getExecutor(),user.getUsername()
                        )
                );
            }
            else{
                throw new ResourceNotFoundException("User","Username","", HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            throw new ResourceNotFoundException("User","Username","", HttpStatus.NOT_FOUND);
        }
    }



}
