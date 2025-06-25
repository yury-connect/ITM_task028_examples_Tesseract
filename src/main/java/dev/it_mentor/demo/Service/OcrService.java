package dev.it_mentor.demo.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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
        tesseract.setDatapath(new File("src/main/resources").getAbsolutePath());

        // Устанавливаем язык (eng, rus, etc.)
        tesseract.setLanguage("rus+eng"); // Распознает русский и английский

        // Опционально: настройки для улучшения распознавания
        tesseract.setPageSegMode(1); // Авто-определение структуры текста
        tesseract.setOcrEngineMode(1); // Использование LSTM (лучший режим)

        // Конвертируем файл в BufferedImage
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Не удалось прочитать изображение. Проверьте формат (PNG, JPG, BMP).");
        }

        return tesseract.doOCR(imageFile);
    }
}
