package org.dixie;

import org.dixie.image.ColourSchemaChar;
import org.dixie.image.Converter;
import org.dixie.image.TextGraphicsConverter;
import org.dixie.server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new Converter();
        converter.setTextColorSchema(new ColourSchemaChar());

        GServer server = new GServer(converter);
        server.start();
    }
}