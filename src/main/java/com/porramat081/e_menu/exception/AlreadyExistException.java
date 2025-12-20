package com.porramat081.e_menu.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String resourceName) {
        super(resourceName + " already exist");
    }
}
