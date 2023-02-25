package com.example.paymentsystem.service.sketch;


import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.history.UserHistory;

import java.util.List;


public interface UserService {
    UserHistory insert(UserDTO user);

    UserHistory remove(UserDTO user);
    UserHistory update(UserDTO user);

    List<UserDTO> findAll();


    void handleUser(ApproveDTO user);

    AuthDTO logIn(AuthDTO authDTO);
    List<HistoryDTO> mapApproveTable();

    UserHistory  changePassword(ChangePasswordDTO passwordDTO);
}
