package com.dev.gware.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: If you want to add more detail to the exception, you could add more constructors.

    // public ResourceNotFoundException(String message, Throwable cause) {
    //     super(message, cause);
    // }

    // public ResourceNotFoundException(Throwable cause) {
    //     super(cause);
    // }
}
