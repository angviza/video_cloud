package com.hdvon.nmp.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdvon.nmp.exception.UserTimeoutException;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.vo.UserVo;

@Service
public class UserBiz {

    @Autowired
    public RedisHelper redisHelper;

    public UserVo getLoginUser(String token) {
        UserVo userVo = redisHelper.getUserByToken(token);
        if(userVo == null){
            throw new UserTimeoutException("账号已超时");
        }
        return userVo;
    }

}
