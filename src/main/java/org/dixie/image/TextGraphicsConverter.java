package org.dixie.image;

import java.io.IOException;

public interface TextGraphicsConverter {
    String convert(String filepath) throws IOException, RatioException;

    void setMaxWidth(int width) throws WrongParameterException;

    void setMaxHeight(int height) throws WrongParameterException;

    void setMaxRatio(double maxRatio) throws WrongParameterException;

    void setTextColorSchema(TextColorSchema schema);
}
