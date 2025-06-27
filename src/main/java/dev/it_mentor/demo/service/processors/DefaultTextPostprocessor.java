package dev.it_mentor.demo.service.processors;

import org.springframework.stereotype.Service;


// Сервис постобработки текста (TextPostprocessor)
// Реализация
@Service
public class DefaultTextPostprocessor implements TextPostprocessor {

    @Override
    public String process(String rawText) {
        return rawText.replaceAll("(?m)^[^\\w\\а-яА-Я]{1,2}$", "")
                .replaceAll("(\n){2,}", "\n")
                .trim();
    }
}
