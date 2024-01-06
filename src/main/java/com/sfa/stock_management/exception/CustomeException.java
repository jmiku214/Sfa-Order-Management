package com.sfa.stock_management.exception;

public class CustomeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomeException(String status) {
        super(status);
    }

}
