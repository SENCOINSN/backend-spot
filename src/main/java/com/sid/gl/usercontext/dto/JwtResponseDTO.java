package com.sid.gl.usercontext.dto;

import com.sid.gl.usercontext.domain.User;

public class JwtResponseDTO {
    private String accessToken;
    private String token;
    private User userInfo;

    public JwtResponseDTO(String accessToken, String token, User userInfo) {
        this.accessToken = accessToken;
        this.token = token;
        this.userInfo = userInfo;
    }

    public JwtResponseDTO() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
