package com.thai27.trang_tin_tuc_v5_be.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class SignUpCodeExpiredException extends RuntimeException{
    public SignUpCodeExpiredException(String message){
        super(message);
    }

}
