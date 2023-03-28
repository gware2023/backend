package com.dev.gware.user.mapper;

import com.dev.gware.user.domain.UsrInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UsrInfo findById(String usrId);
}
