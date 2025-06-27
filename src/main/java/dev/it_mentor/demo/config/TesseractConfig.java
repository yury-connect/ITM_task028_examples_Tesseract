package dev.it_mentor.demo.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfig {

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("target/classes/tessdata");
        tesseract.setLanguage("rus+eng");
        tesseract.setPageSegMode(6);
        tesseract.setOcrEngineMode(1);
        return tesseract;
    }
}
