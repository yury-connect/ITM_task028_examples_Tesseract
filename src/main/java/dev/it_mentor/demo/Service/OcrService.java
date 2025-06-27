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

        // 1. Абсолютный путь к tessdata берется из classpath (автоматически)
        // Файлы будут в target/classes/tessdata благодаря maven-dependency-plugin
        String tessDataPath = new File("target/classes/tessdata").getAbsolutePath();
        tesseract.setDatapath(tessDataPath);

//        tesseract.setPageSegMode(3); // Отключите автоматическое определение ориентации/ без ориентации

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
