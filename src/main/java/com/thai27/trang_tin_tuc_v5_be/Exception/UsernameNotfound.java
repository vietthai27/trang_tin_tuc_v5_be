package com.thai27.trang_tin_tuc_v5_be.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class UsernameNotfound extends Exception{
    public UsernameNotfound(String message){
        super(message);
    }

}
