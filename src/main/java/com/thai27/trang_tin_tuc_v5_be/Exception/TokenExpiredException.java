package com.thai27.trang_tin_tuc_v5_be.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TokenExpiredException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TokenExpiredException(String message){
        super(message);
    }

}
