package org.dixie.image;

public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super("Parameter exceeds the ceiling");
    }
}
