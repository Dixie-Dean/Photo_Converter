package org.dixie;

import org.dixie.image.ColourSchemaChar;
import org.dixie.image.Converter;
import org.dixie.image.TextGraphicsConverter;
import org.dixie.server.GServer;

import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new Converter();
        converter.setTextColorSchema(new ColourSchemaChar());

//        String imgTxt = converter.convert("pictures/eve.jpg");
//        try (FileOutputStream output = new FileOutputStream("picture_test")) {
//            output.write(imgTxt.getBytes());
//        }

        GServer server = new GServer(converter);
        server.start();
    }
}