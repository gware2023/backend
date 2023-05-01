package com.dev.gware.user.service;

import com.dev.gware.user.dto.request.UpdateUserReq;
import com.dev.gware.user.dto.response.GetUserRes;

public interface UserService {

    GetUserRes findById(Long userId);

    void updateUser(UpdateUserReq updateUserReq);
}
