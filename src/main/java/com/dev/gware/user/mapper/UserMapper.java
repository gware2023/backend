package com.dev.gware.user.mapper;

import com.dev.gware.user.domain.UsrInfo;
import com.dev.gware.user.dto.request.UpdateUserReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UsrInfo findById(String usrId);

    UsrInfo findByKey(Long usrKey);

    void updateUser(UpdateUserReq updateUserReq);
}
