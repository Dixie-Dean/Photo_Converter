package org.dixie.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Converter implements TextGraphicsConverter {
    protected final static int WIDTH_CEILING = 256;
    protected final static int HEIGHT_CEILING = 256;
    protected final static double RATIO_CEILING = 3;

    private int maxWidth = WIDTH_CEILING;
    private int maxHeight = HEIGHT_CEILING;
    private double maxRatio = RATIO_CEILING;
    private TextColorSchema schema;

    @Override
    public void setMaxWidth(int width) throws WrongParameterException {
        if (width > WIDTH_CEILING) {
            throw new WrongParameterException();
        }
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) throws WrongParameterException {
        if (height > HEIGHT_CEILING) {
            throw new WrongParameterException();
        }
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double ratio) throws WrongParameterException {
        if (ratio > RATIO_CEILING) {
            throw new WrongParameterException();
        }
        this.maxRatio = ratio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String filepath) throws IOException, RatioException {
        BufferedImage image = ImageIO.read(new File(filepath));

        checkAspectRatio(image);
        HashMap<String, Integer> finalSize = determineNewSize(image);
        Image scaledImg = changeImgSize(finalSize, image);
        BufferedImage bwImg = makeImgBW(finalSize, scaledImg);
        List<List<Character>> pixelsLists = collectPixelsToList(bwImg);
        return getCharImage(pixelsLists);
    }

    private void checkAspectRatio(BufferedImage image) throws RatioException {
        double imgRatioWidth = (double) image.getWidth() / image.getHeight();
        if (imgRatioWidth > maxRatio) {
            throw new RatioException(imgRatioWidth, maxRatio);
        }

        double imgRatioHeight = (double) image.getHeight() / image.getWidth();
        if (imgRatioHeight > maxRatio) {
            throw new RatioException(imgRatioHeight, maxRatio);
        }
    }

    private HashMap<String, Integer> determineNewSize(BufferedImage image) {
        int shrinkInXTimes;

        if (image.getWidth() > maxWidth && image.getHeight() > maxHeight) {
            if (image.getHeight() > image.getWidth()) {
                shrinkInXTimes = image.getHeight() / maxHeight;
            } else {
                shrinkInXTimes = image.getWidth() / maxWidth;
            }
            HashMap<String, Integer> newWidthAndHeight = new HashMap<>();
            newWidthAndHeight.put("width", image.getWidth() / shrinkInXTimes);
            newWidthAndHeight.put("height", image.getHeight() / shrinkInXTimes);
            return newWidthAndHeight;

        } else if (image.getWidth() > maxWidth && image.getHeight() < maxHeight) {
            shrinkInXTimes = image.getWidth() / maxWidth;
            HashMap<String, Integer> newWidthAndHeight = new HashMap<>();
            newWidthAndHeight.put("width", image.getWidth() / shrinkInXTimes);
            newWidthAndHeight.put("height", image.getHeight() / shrinkInXTimes);
            return newWidthAndHeight;

        } else if (image.getWidth() < maxWidth && image.getHeight() > maxHeight) {
            shrinkInXTimes = image.getHeight() / maxHeight;
            HashMap<String, Integer> newWidthAndHeight = new HashMap<>();
            newWidthAndHeight.put("width", image.getWidth() / shrinkInXTimes);
            newWidthAndHeight.put("height", image.getHeight() / shrinkInXTimes);
            return newWidthAndHeight;

        } else {
            HashMap<String, Integer> newWidthAndHeight = new HashMap<>();
            newWidthAndHeight.put("width", image.getWidth());
            newWidthAndHeight.put("height", image.getHeight());
            return newWidthAndHeight;
        }
    }

    private Image changeImgSize(HashMap<String, Integer> size, BufferedImage image) {
        return image.getScaledInstance(size.get("width"),
                size.get("height"),
                BufferedImage.SCALE_SMOOTH);
    }

    private BufferedImage makeImgBW(HashMap<String, Integer> size, Image image) {
        BufferedImage bwImg = new BufferedImage(size.get("width"),
                size.get("height"),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        return bwImg;
    }

    private List<List<Character>> collectPixelsToList(BufferedImage bwImg) {
        WritableRaster bwRaster = bwImg.getRaster();
        List<List<Character>> pixelsLists = new LinkedList<>();
        for (int i = 0; i < bwRaster.getHeight(); i++) {
            List<Character> certainPixelList = new LinkedList<>();
            for (int j = 0; j < bwRaster.getWidth(); j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                certainPixelList.add(c);
            }
            pixelsLists.add(certainPixelList);
        }
        return pixelsLists;
    }

    private String getCharImage(List<List<Character>> pixelsLists) {
        StringBuilder result = new StringBuilder();
        for (List<Character> listOfPixels : pixelsLists) {
            for (char symbol : listOfPixels) {
                result.append(symbol).append(symbol).append(symbol);
            }
            result.append("\n");
        }
        return result.toString();
    }
}
