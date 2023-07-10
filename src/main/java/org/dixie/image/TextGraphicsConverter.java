package org.dixie.image;

import org.dixie.image.exception.RatioException;
import org.dixie.image.exception.WrongParameterException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TextGraphicsConverter {
    String convert(String source) throws IOException, RatioException, URISyntaxException;

    void setMaxWidth(int width) throws WrongParameterException;

    void setMaxHeight(int height) throws WrongParameterException;

    void setMaxRatio(double maxRatio) throws WrongParameterException;

    void setTextColorSchema(TextColorSchema schema);
}
