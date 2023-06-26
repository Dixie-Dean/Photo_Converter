package org.dixie.image;

import java.io.IOException;

public interface TextGraphicsConverter {
    String convert(String filepath) throws IOException, ImageSizeException;

    void setMaxWidth(int width);

    void setMaxHeight(int height);

    void setMaxRatio(double maxRatio);

    void setTextColorSchema(TextColorSchema schema);
}
