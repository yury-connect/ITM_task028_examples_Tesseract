package dev.it_mentor.demo.service.processors;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;


// Сервис предобработки изображений (ImagePreprocessor)
// Реализация
@Service
public class DefaultImagePreprocessor implements ImagePreprocessor {

    @Override
    public BufferedImage preprocess(BufferedImage original) {
        // 1. Конвертируем в RGB, если изображение индексированное
        BufferedImage rgbImage = convertToRGB(original);

        // 2. Сначала увеличиваем контраст на RGB-изображении
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        BufferedImage contrasted = rescaleOp.filter(rgbImage, null);

        // 3. Затем применяем ч/б фильтр
        BufferedImage processed = new BufferedImage(
                contrasted.getWidth(),
                contrasted.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g = processed.createGraphics();  // 1. Создаём графический контекст
        g.drawImage(contrasted, 0, 0, null);       // 2. Рисуем изображение
        g.dispose();                               // 3. Освобождаем ресурсы

        return processed;
    }

    private BufferedImage convertToRGB(BufferedImage src) {
        // Добавляем проверку всех проблемных типов
        if (src.getType() == BufferedImage.TYPE_BYTE_INDEXED ||
                src.getType() == BufferedImage.TYPE_BYTE_BINARY ||
                src.getType() == BufferedImage.TYPE_BYTE_GRAY) {

            BufferedImage rgbImage = new BufferedImage(
                    src.getWidth(),
                    src.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            rgbImage.getGraphics().drawImage(src, 0, 0, null);
            return rgbImage;
        }
        return src;
    }
}
