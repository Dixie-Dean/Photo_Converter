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

//        String image = converter.convert("pictures/GRL.jpg");
//        try(FileOutputStream outputStream = new FileOutputStream("picture.txt")) {
//            outputStream.write(image.getBytes());
//        }

        GServer server = new GServer(converter);
        server.start();
    }
}