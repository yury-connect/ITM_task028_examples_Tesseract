package dev.it_mentor.demo.service;

import dev.it_mentor.demo.repository.RecognizedTextRepository;
import dev.it_mentor.demo.service.processors.ImagePreprocessor;
import dev.it_mentor.demo.service.processors.TextPostprocessor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import dev.it_mentor.demo.model.RecognizedText;


@Service
public class OcrService {

    private final Tesseract tesseract;
    private final ImagePreprocessor imagePreprocessor;
    private final TextPostprocessor textPostprocessor;
    private final RecognizedTextRepository repository;


    public OcrService(Tesseract tesseract,
                      ImagePreprocessor imagePreprocessor,
                      TextPostprocessor textPostprocessor,
                      RecognizedTextRepository repository) {
        this.tesseract = tesseract;
        this.imagePreprocessor = imagePreprocessor;
        this.textPostprocessor = textPostprocessor;
        this.repository = repository;
    }


    public String recognizeText(File imageFile) throws TesseractException, IOException {

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unsupported image format");
        }

        BufferedImage processedImage = imagePreprocessor.preprocess(image);
        String rawText = tesseract.doOCR(processedImage);

        String result = textPostprocessor.process(rawText);

        String fileInfo = imageFile.getName() + " " + imageFile.length() + " bytes";
//        RecognizedText recognizedText = RecognizedText.builder()
//                .id(UUID.randomUUID())
//                .fileInfo(fileInfo)
//                .text(result)
//                .recognitionDate(java.time.LocalDateTime.now())
//                .build();

        RecognizedText recognizedText = new RecognizedText();
        recognizedText.setFileInfo(fileInfo);
        recognizedText.setText(result);

        repository.save(recognizedText);

        return result;
    }
}
