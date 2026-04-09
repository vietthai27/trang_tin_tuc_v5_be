package com.thai27.trang_tin_tuc_v5_be.Exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
