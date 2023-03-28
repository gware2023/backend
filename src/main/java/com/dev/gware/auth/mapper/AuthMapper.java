package com.dev.gware.auth.mapper;

import com.dev.gware.auth.dto.request.UpdateRefreshTokenRequest;
import com.dev.gware.user.domain.UsrInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    public UsrInfo findByKey(Long usrKey);

    public UsrInfo findById(String usrId);

    public void updateRefreshTokenById(UpdateRefreshTokenRequest request);
}
