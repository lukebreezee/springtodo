package com.example.demo.payload.response.auth;

public class SigninResponseJwt {
    private Boolean success;
    private String message;
    private String jwt;
    private String refreshToken;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public SigninResponseJwt(Boolean success, String message, String jwt, String refreshToken) {
        this.success = success;
        this.message = message;
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "SigninResponseJwt{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", jwt='" + jwt + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
