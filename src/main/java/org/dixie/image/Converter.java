package org.dixie.image;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Converter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

//    public Converter(TextColorSchema schema) {
//        this.schema = schema;
//    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, ImageSizeException {
        BufferedImage image = ImageIO.read(new URL(url));

        if (maxRatio != 0 && maxHeight != 0 && maxWidth != 0) {
            if (checkAspectRatio(image)) {
                HashMap<String, Integer> finalSize = determineNewSize(image);
                Image scaledImg = changeImgSize(finalSize, image);
                BufferedImage bwImg = makeImgBW(finalSize, scaledImg);
                List<List<Character>> pixelsLists = collectPixelsToList(bwImg);
                return getCharImage(pixelsLists);
            } else {
                return null;
            }
        } else {
            HashMap<String, Integer> imageSize = new HashMap<>();
            imageSize.put("width", image.getWidth());
            imageSize.put("height", image.getHeight());

            BufferedImage bwImg = makeImgBW(imageSize, image);
            List<List<Character>> pixelsLists = collectPixelsToList(bwImg);
            return getCharImage(pixelsLists);
        }
    }

    private boolean checkAspectRatio(BufferedImage image) throws ImageSizeException {
        double imgRatio = (double) image.getWidth() / image.getHeight();

        if (imgRatio < maxRatio) {
            return true;
        } else {
            throw new ImageSizeException(imgRatio, maxRatio);
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
