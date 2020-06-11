package com.barry.idempotence.service.impl;

import com.barry.idempotence.config.RedisService;
import com.barry.idempotence.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    @Override
    public String createToken() {
        return null;
    }

    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {
        return false;
    }

}
