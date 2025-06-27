package dev.it_mentor.demo.service.processors;

import java.awt.image.BufferedImage;


// Сервис предобработки изображений (ImagePreprocessor)
public interface ImagePreprocessor {
    BufferedImage preprocess(BufferedImage original);
}


