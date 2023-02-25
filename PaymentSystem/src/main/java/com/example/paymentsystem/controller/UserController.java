package com.example.paymentsystem.controller;



import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.history.UserHistory;
import com.example.paymentsystem.service.sketch.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;


    @GetMapping(value = "/users")
    public List<UserDTO> findAll(){
        return userService.findAll();
    }

    @PostMapping(value="/users/insert")
    public UserHistory insert(@RequestBody UserDTO user){
        return userService.insert(user);
    }

    @PostMapping(value="/users/update")
    public UserHistory update(@RequestBody UserDTO user){
        return userService.update(user);
    }

    @PostMapping(value="/users/remove")
    public UserHistory remove(@RequestBody UserDTO user){
        return userService.remove(user);
    }

    @PostMapping(value="/users/handle")
    public void handleUser(@RequestBody ApproveDTO user){
        userService.handleUser(user);
    }

    @PostMapping("/users/login")
    ResponseEntity<AuthDTO> logIn(@RequestBody AuthDTO authDTO){
        return new ResponseEntity<>(userService.logIn(authDTO), HttpStatus.OK);
    }

    @PostMapping("/users/changePass")
    public UserHistory changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO);
    }

    @PostMapping("/users/history")
    public List<HistoryDTO> mapApproveTable(){
        return userService.mapApproveTable();
    }







}
