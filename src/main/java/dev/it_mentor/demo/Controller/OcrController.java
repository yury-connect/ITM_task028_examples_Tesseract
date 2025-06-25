package dev.it_mentor.demo.Controller;

import dev.it_mentor.demo.Service.OcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

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




        // Сохраняем файл временно
        File tempFile = File.createTempFile("ocr-", ".tmp");
        file.transferTo(tempFile);

        // Распознаем текст
        String result = ocrService.recognizeText(tempFile);

        // Удаляем временный файл
        tempFile.delete();

        System.out.println(result);

        return result;
    }
}