package dev.it_mentor.demo.Controller;

import dev.it_mentor.demo.Service.OcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/api/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/recognize")
    public String recognizeText(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        System.out.println("file.name = " + file.getName());
        System.out.println("file.originalName = " + file.getOriginalFilename());
        System.out.println("file.size = " + file.getSize());

        // Создаём временный файл с правильным расширением
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";
        File tempFile = File.createTempFile("ocr-", extension);

        try {
            // Копируем содержимое MultipartFile во временный файл
            file.transferTo(tempFile);

            // Проверяем, можно ли прочитать изображение
            if (ImageIO.read(tempFile) == null) {
                throw new IOException("Неподдерживаемый формат изображения");
            }

            // Распознаём текст
            return ocrService.recognizeText(tempFile);
        } finally {
            // Удаляем временный файл
            Files.deleteIfExists(tempFile.toPath());
        }
    }
}