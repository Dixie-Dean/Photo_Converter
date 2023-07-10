package org.dixie.image;

public class ColourSchemaChar implements TextColorSchema {
    private final char[] symbols = {'■', '▲', '¶', '®', 'Δ', '*', '•', '-'};

    @Override
    public char convert(int color) {
        return symbols[(int) Math.floor(color / 256.0 * symbols.length)];
    }
}
