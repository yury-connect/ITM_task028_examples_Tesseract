package dev.it_mentor.demo.util;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class GeoLinksGenerator {
    private final double latitude;
    private final double longitude;

    /**
     * Форматы координат
     */
    public enum CoordinateFormat {
        DECIMAL_DEGREES,  // 55.755831, 37.617673
        DEGREES_MINUTES,  // 55°45'21"N, 37°37'04"E
        DEGREES_MINUTES_SECONDS // 55°45'21.0"N, 37°37'03.6"E
    }

    /**
     * Генерирует все возможные ссылки на карты
     */
    public Map<String, String> generateAllMapLinks(String locationName) {
        Map<String, String> links = new HashMap<>();

        // Google Maps
        links.put("Google Maps (Decimal)", generateGoogleMapsLink(CoordinateFormat.DECIMAL_DEGREES, locationName));
        links.put("Google Maps (Satellite)", generateGoogleMapsLink("satellite", locationName));

        // Yandex Maps
        links.put("Yandex Maps", generateYandexMapsLink(locationName));
        links.put("Yandex Navigator", generateYandexNavigatorLink());

        // Альтернативные форматы
        links.put("OpenStreetMap", generateOpenStreetMapLink());
        links.put("Apple Maps", generateAppleMapsLink());

        return links;
    }

    /**
     * Google Maps ссылки
     */
    public String generateGoogleMapsLink(CoordinateFormat format, String locationName) {
        String coords = formatCoordinates(format);
        return String.format("https://www.google.com/maps/place/%s/@%.6f,%.6f,17z",
                locationName != null ? locationName : coords, latitude, longitude);
    }

    public String generateGoogleMapsLink(String mapType, String locationName) {
        return String.format("https://www.google.com/maps/@?api=1&map_action=map&center=%.6f,%.6f&zoom=15&basemap=%s",
                latitude, longitude, mapType);
    }

    /**
     * Yandex Maps ссылки
     */
    public String generateYandexMapsLink(String locationName) {
        return String.format("https://yandex.ru/maps/?pt=%.6f,%.6f&z=15&l=map",
                longitude, latitude);
    }

    public String generateYandexNavigatorLink() {
        return String.format("https://yandex.ru/navi/?whatshere[point]=%.6f,%.6f",
                longitude, latitude);
    }

    /**
     * Другие картографические сервисы
     */
    public String generateOpenStreetMapLink() {
        return String.format("https://www.openstreetmap.org/?mlat=%.6f&mlon=%.6f#map=15/%.6f/%.6f",
                latitude, longitude, latitude, longitude);
    }

    public String generateAppleMapsLink() {
        return String.format("https://maps.apple.com/?ll=%.6f,%.6f&q=%s",
                latitude, longitude, "Location");
    }

    /**
     * Форматирование координат в разных форматах
     */
    private String formatCoordinates(CoordinateFormat format) {
        switch (format) {
            case DEGREES_MINUTES:
                return String.format("%s, %s",
                        convertToDegreesMinutes(latitude, true),
                        convertToDegreesMinutes(longitude, false));
            case DEGREES_MINUTES_SECONDS:
                return String.format("%s, %s",
                        convertToDegreesMinutesSeconds(latitude, true),
                        convertToDegreesMinutesSeconds(longitude, false));
            default:
                return String.format("%.6f, %.6f", latitude, longitude);
        }
    }

    private String convertToDegreesMinutes(double decimalDegrees, boolean isLatitude) {
        int degrees = (int) decimalDegrees;
        double minutes = (decimalDegrees - degrees) * 60;
        char direction = isLatitude ?
                (degrees >= 0 ? 'N' : 'S') :
                (degrees >= 0 ? 'E' : 'W');
        return String.format("%d°%.3f'%c", Math.abs(degrees), minutes, direction);
    }

    private String convertToDegreesMinutesSeconds(double decimalDegrees, boolean isLatitude) {
        int degrees = (int) decimalDegrees;
        double remaining = (decimalDegrees - degrees) * 60;
        int minutes = (int) remaining;
        double seconds = (remaining - minutes) * 60;
        char direction = isLatitude ?
                (degrees >= 0 ? 'N' : 'S') :
                (degrees >= 0 ? 'E' : 'W');
        return String.format("%d°%d'%.1f\"%c",
                Math.abs(degrees), minutes, seconds, direction);
    }
}