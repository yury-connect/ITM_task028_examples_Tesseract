package dev.it_mentor.demo.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import dev.it_mentor.demo.service.OcrService;
import dev.it_mentor.demo.util.GeoLinksGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;


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

        // 1. Логируем базовую информацию о файле
        log.info("""
                Загружен файл:
                - Оригинальное имя: {}
                - Размер: {} bytes
                """, file.getOriginalFilename(), file.getSize());

        StringBuilder fileDescription = new StringBuilder();
        String fileName = file.getOriginalFilename();
        fileDescription.append("Name = " + fileName + ";\n");
        fileDescription.append("Size (byte) = " + file.getSize() + ";\n");
        fileDescription.append("ContentType = " + file.getContentType() + ";\n");


        // 2. Создаем временный файл с сохранением расширения
        File tempFile = createTempFileWithExtension(file);

        try {
            // 3. Извлекаем полные метаданные изображения
            ImageMetadata metadata = extractCompleteImageMetadata(tempFile, fileDescription);
            log.info("Полные метаданные изображения:\n{}", metadata);

            // 4. Проверяем минимальные требования к изображению
            validateImageRequirements(metadata);

            // 5. Извлекаем гео-данные
            GeoLocation geoData = extractGeoData(tempFile);
            if (geoData != null) {
                fileDescription.append("Геоданные изображения: " + geoData + "\n");

                GeoLinksGenerator generator = new GeoLinksGenerator(
                        geoData.getLatitude(), geoData.getLatitude());
                StringBuilder links = new StringBuilder();
                generator.generateAllMapLinks(fileName).forEach((name, url) -> {
                    links.append(name + ": " + url + "\n");
                });
                fileDescription.append("\nGPG - Ссылки\n" + links + "\n");

            } else {
                fileDescription.append("Изображение не содержит геоданных");
            }

            // 5. Выполняем распознавание текста
            log.info("\n\n" + fileDescription.toString());
            String result = ocrService.recognizeText(tempFile, fileDescription.toString());
            log.info("Результат распознавания:\n{}\n", result);
            return result;
        } finally {
            // 6. Обязательно удаляем временный файл
            Files.deleteIfExists(tempFile.toPath());
        }
    }




    /**
     * Создает временный файл с правильным расширением
     */
    private File createTempFileWithExtension(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf('.')) : ".tmp";
        File tempFile = File.createTempFile("ocr-", extension);
        file.transferTo(tempFile);
        log.debug("Создан временный файл: {}", tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * Извлекает полные метаданные изображения
     */
    private ImageMetadata  extractCompleteImageMetadata(File imageFile, StringBuilder imageMetaData) throws IOException {
        try (ImageInputStream in = ImageIO.createImageInputStream(imageFile)) {
            // Получаем подходящий ImageReader для этого формата
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (!readers.hasNext()) {
                throw new IOException("Не удалось определить ридер для данного формата изображения");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(in);

                // 1. Основные параметры изображения
                BufferedImage image = reader.read(0);
                int width = image.getWidth();                                           // Ширина в пикселях
                imageMetaData.append("Resolution, width (pixels): " + width + ";\n");
                int height = image.getHeight();                                         // Высота в пикселях
                imageMetaData.append("Resolution, height  (pixels): " + height + ";\n");
                String format = reader.getFormatName();                                 // Формат изображения (PNG, JPEG и т.д.)
                imageMetaData.append("Image format: " + format + ";\n");
                int bitsPerPixel = image.getColorModel().getPixelSize();                // Бит на пиксель
                imageMetaData.append("Bits per pixel: " + bitsPerPixel + ";\n");
                imageMetaData.append("Number of color components: " + image.getColorModel().getNumComponents() + ";\n");    // Количество цветовых компонентов
                imageMetaData.append("The presence of an alpha channel: " + image.getColorModel().hasAlpha() + ";\n");      //  Наличие альфа-канала

                // 2. EXIF и другие метаданные (если доступны)
                IIOMetadata metadata = reader.getImageMetadata(0);
                imageMetaData.append("Metadata format: " + metadata.getNativeMetadataFormatName() + ";\n");    // Формат метаданных (EXIF и т.д.)

                // 3. Дополнительная информация о цветовом пространстве
                String colorSpace = image.getColorModel().getColorSpace().getType() + " (" +
                        image.getColorModel().getColorSpace().getNumComponents() + ")";
                imageMetaData.append("Color space: " + colorSpace + ";\n");             // Цветовое пространство

                long fileSize = imageFile.length();

//                return new ImageMetadata(width, height, format, bitsPerPixel, fileSize);
                return ImageMetadata.builder()
                        .width(width)
                        .height(height)
                        .format(format)
                        .bitsPerPixel(bitsPerPixel)
                        .fileSize(fileSize)
                        .build();
            } finally {
                reader.dispose();
            }
        }
    }


    /**
     * Проверяет минимальные требования к изображению
     */
    private void validateImageRequirements(ImageMetadata metadata) throws IOException {
        // 1. Проверка разрешения
        int minWidth = 50;
        int minHeight = 50;
        if (metadata.getWidth() < minWidth || metadata.getHeight() < minHeight) {
            throw new IOException("Разрешение изображение слишком маленькое. Минимальный размер: " + minWidth + "x" + minHeight + " пикселей");
        }

        // 2. Проверка формата
        if (!isSupportedFormat(metadata.getFormat())) {
            throw new IOException("Неподдерживаемый формат изображения: " + metadata.getFormat());
        }

        // 3. Проверка глубины цвета
        int minBits = 8;
        if (metadata.getBitsPerPixel() < minBits) {
            throw new IOException("Изображение должно иметь минимум " + minBits + " бит на пиксель");
        }

        // 4. Проверка размера файла
        long minSize = 3*1024L;
        long maxSize = 50*1024*1024L;
        if (metadata.getFileSize() < minSize || metadata.getFileSize() > maxSize) {
            throw new IOException("Размер иИзображение должен быть от " + minSize + " до " + maxSize + " байт.");
        }
    }


    /**
     * Проверяет поддерживается ли формат изображения
     */
    private boolean isSupportedFormat(String format) {
        String[] supported = {"PNG", "JPEG", "JPG", "TIFF", "BMP"};
        for (String fmt : supported) {
            if (fmt.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Класс для хранения всех извлеченных метаданных изображения
     */
    @Getter
//    @RequiredArgsConstructor
    @Builder
    private static class ImageMetadata {
        private final int width;              // Ширина в пикселях
        private final int height;             // Высота в пикселях
        private final String format;          // Формат изображения (PNG, JPEG и т.д.)
        private final int bitsPerPixel;       // Бит на пиксель
//        private final int numComponents;      // Количество цветовых компонентов
//        private final boolean hasAlpha;       // Наличие альфа-канала
//        private final String metadataFormat;  // Формат метаданных (EXIF и т.д.)
//        private final String colorSpace;      // Цветовое пространство
//        private final String compression;     // Тип сжатия
        private final long fileSize;          // Размер файла в байтах


        @Override
        public String toString() {
            return String.format("""
                    Разрешение: %dx%d пикселей;\n
                    Формат: %s;\n
                    Глубина цвета: %d бит/пиксель;\n
                    Размер файла: %d bytes;\n
                    """,
                    width, height, format, bitsPerPixel, fileSize);
        }
    }


    // Модифицированный метод извлечения метаданных изображения
    private GeoLocation extractGeoData(File imageFile) throws IOException {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gpsDirectory == null) {
                log.warn("Файл не содержит директории GPS (возможно, нет геоданных)");
                return null;
            }
            if (!gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)) {
                log.warn("GPS-директория есть, но отсутствует тег широты");
                return null;
            }


//            if (gpsDirectory != null && gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)) {
            return new GeoLocation(
                    gpsDirectory.getGeoLocation().getLatitude(),
                    gpsDirectory.getGeoLocation().getLongitude(),
                    gpsDirectory.getDescription(GpsDirectory.TAG_ALTITUDE),
//                        gpsDirectory.getDescription(GpsDirectory.TAG_DIRECTION) // нет такой константы   TAG_DIRECTION
                    gpsDirectory.getDescription(GpsDirectory.TAG_IMG_DIRECTION)
            );
//            }
//            return null;
        } catch (Exception e) {
            log.warn("Не удалось извлечь геоданные: {}", e.getMessage());
            return null;
        }
    }

    // Класс для хранения геоданных
    @Getter
    @AllArgsConstructor
    public static class GeoLocation {
        private final double latitude;    // Широта
        private final double longitude;   // Долгота
        private final String altitude;    // Высота над уровнем моря
        private final String direction;   // Направление съемки

        @Override
        public String toString() {
            return String.format("GPS: %.6f°N, %.6f°E, высота: %s, направление: %s",
                    latitude, longitude, altitude != null ? altitude : "н/д",
                    direction != null ? direction : "н/д");
        }
    }
}