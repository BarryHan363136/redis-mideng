package com.barry.idempotence.service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    /**
     * 创建token
     * */
    public String createToken();

    /**
     * 检验token
     * */
    public boolean checkToken(HttpServletRequest request);

}
