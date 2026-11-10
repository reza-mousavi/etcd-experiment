package com.reza.learning.contract.order.exception;

public class CustomerClientException extends RuntimeException {

    public CustomerClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
