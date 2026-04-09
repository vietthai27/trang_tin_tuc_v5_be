package com.thai27.trang_tin_tuc_v5_be.Exception;

public class UserInfoAlreadyExistException extends RuntimeException{
    public UserInfoAlreadyExistException(String message){
        super(message);
    }
}
