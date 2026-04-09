package com.thai27.trang_tin_tuc_v5_be.Exception;

public class SignUpCodeExpiredException extends RuntimeException{
    public SignUpCodeExpiredException(String message){
        super(message);
    }
}
