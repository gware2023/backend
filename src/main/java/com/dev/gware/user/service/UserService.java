package com.dev.gware.user.service;

import com.dev.gware.user.dto.request.UpdateUserReq;
import com.dev.gware.user.dto.response.GetUserRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    GetUserRes findById(Long userId);

    Page<GetUserRes> findAll(Pageable pageable);

    void updateUser(UpdateUserReq updateUserReq);
}
