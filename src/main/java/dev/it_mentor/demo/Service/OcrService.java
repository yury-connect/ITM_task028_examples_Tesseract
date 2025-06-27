package dev.it_mentor.demo.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Service
public class OcrService {

    public String recognizeText(File imageFile) throws TesseractException, IOException {
        Tesseract tesseract = new Tesseract();

        // Автоматическое определение пути в ресурсах
//        tesseract.setDatapath(new File("src/main/resources").getAbsolutePath());
//        tesseract.setDatapath("src/main/resources");
        // Правильный способ указания пути к tessdata
        ClassPathResource resource = new ClassPathResource("tessdata");
//        tesseract.setDatapath(resource.getFile().getParent());

// Для разработки можно использовать абсолютный путь
//        tesseract.setDatapath("c:/Users/Yury/IdeaProjects/IT-Mentor/ITM_tasks/ITM_task028_examples_Tesseract/src/main/resources/tessdata");
        tesseract.setDatapath("c:/Users/User/IdeaProjects/IT-Mentor/ITM_task028_examples_Tesseract/src/main/resources/tessdata");

        // Отключите автоматическое определение ориентации
//        tesseract.setPageSegMode(3); // Полностью автоматический, но без ориентации

        // Устанавливаем язык (eng, rus, etc.)
        tesseract.setLanguage("rus+eng"); // Распознает русский и английский

        // Опционально: настройки для улучшения распознавания
        tesseract.setPageSegMode(1); // Авто-определение структуры текста
        tesseract.setOcrEngineMode(1); // Использование LSTM (лучший режим)

        // Конвертируем файл в BufferedImage
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Не удалось прочитать изображение. Поддерживаемые форматы: PNG, JPG, BMP, TIFF");
        }

        // Передаём BufferedImage, а не File
        return tesseract.doOCR(image);
    }
}
