package com.scm.product.management.exception;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
@Component
public class ProductModifyException extends RuntimeException {

    private final int status;

    public ProductModifyException(int status, String message){
        super(message);
        this.status = status;
    }

    public ProductModifyException(){
        this.status = HttpStatus.SC_NOT_ACCEPTABLE;
    }

    public int getStatus() {
        return status;
    }

}