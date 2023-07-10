package org.dixie.image.exception;

public class RatioException extends Exception {
    public RatioException(double ratio, double maxRatio) {
        super("Maximum image aspect ratio must be: " + maxRatio + ". Current aspect ratio: " + ratio);
    }
}
