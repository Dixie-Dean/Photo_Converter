package org.dixie;

import org.dixie.image.ColourSchemaChar;
import org.dixie.image.Converter;
import org.dixie.image.TextGraphicsConverter;

public class Main {
    public static void main(String[] args) {
        TextGraphicsConverter converter = new Converter(new ColourSchemaChar());
        converter.convert();


//        String url =
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);
    }
}