package dev.it_mentor.demo.service;

import dev.it_mentor.demo.service.processors.ImagePreprocessor;
import dev.it_mentor.demo.service.processors.TextPostprocessor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Service
public class OcrService {

    private final Tesseract tesseract;
    private final ImagePreprocessor imagePreprocessor;
    private final TextPostprocessor textPostprocessor;


    // Внедрение зависимостей через конструктор
    public OcrService(Tesseract tesseract,
                      ImagePreprocessor imagePreprocessor,
                      TextPostprocessor textPostprocessor) {
        this.tesseract = tesseract;
        this.imagePreprocessor = imagePreprocessor;
        this.textPostprocessor = textPostprocessor;
    }


    public String recognizeText(File imageFile) throws TesseractException, IOException {
        /*
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
        */

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unsupported image format");
        }

        BufferedImage processedImage = imagePreprocessor.preprocess(image);
        String rawText = tesseract.doOCR(processedImage);
        return textPostprocessor.process(rawText);
    }
}
