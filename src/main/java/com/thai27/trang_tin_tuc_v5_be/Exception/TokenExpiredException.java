package com.thai27.trang_tin_tuc_v5_be.Exception;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message){
        super(message);
    }
}
