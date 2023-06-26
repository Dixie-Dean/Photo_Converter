package org.dixie.image;

public class ImageSizeException extends Exception {
    public ImageSizeException(double ratio, double maxRatio) {
        super("Maximum image aspect ratio must be: " + maxRatio + ". Current aspect ratio: " + ratio);
    }
}
