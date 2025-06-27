package dev.it_mentor.demo.controller;

import dev.it_mentor.demo.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Slf4j
@RestController
@RequestMapping(value = "/api/ocr")
public class OcrController {

    private final OcrService ocrService;

    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/recognize")
    public String recognizeText(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
//        log.info("file.name = " + file.getName());
//        log.debug("file.originalName = " + file.getOriginalFilename());
//        log.debug("file.size = " + file.getSize());

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