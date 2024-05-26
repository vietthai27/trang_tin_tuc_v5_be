package com.thai27.trangtintuc_v4_be.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class UsernameNotfound extends Exception{
    public UsernameNotfound(String message){
        super(message);
    }

}
