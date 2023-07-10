package org.dixie.image.exception;

public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super("Parameter exceeds the ceiling (256)");
    }
}