package dev.it_mentor.demo.config;

import net.sourceforge.tess4j.Tesseract;


public class TesseractSettings {

    public static void applyDefaultSettings(Tesseract tesseract) {
        tesseract.setLanguage("rus+eng");
        tesseract.setPageSegMode(6);
        tesseract.setTessVariable("preserve_interword_spaces", "1");
    }

    public static void applyCustomSettings(Tesseract tesseract, String lang, int segMode) {
        tesseract.setLanguage(lang);
        tesseract.setPageSegMode(segMode);
    }
}