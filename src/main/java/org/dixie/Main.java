package org.dixie;

import org.dixie.image.ColourSchemaChar;
import org.dixie.image.Converter;
import org.dixie.image.ImageSizeException;
import org.dixie.image.TextGraphicsConverter;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ImageSizeException, IOException {
        TextGraphicsConverter converter = new Converter();
        converter.setTextColorSchema(new ColourSchemaChar());
        converter.setMaxHeight(200);
        converter.setMaxWidth(350);
        converter.setMaxRatio(5);

        String imgTxt = converter.convert("pictures/gunter.jpg");
        System.out.println(imgTxt);

        try (FileOutputStream output = new FileOutputStream("picture_test")) {
            output.write(imgTxt.getBytes());
        }
    }
}