package dev.it_mentor.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "recognized_text")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class RecognizedText {

    public RecognizedText (String fileInfo, String content) {
        this.fileInfo = fileInfo;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE) // Запрещаем сеттер
    private UUID id;

    @Column(name = "rt_info", columnDefinition = "TEXT")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String fileInfo;

    @Column(name = "rt_content", columnDefinition = "TEXT")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String content;

    @Column(name = "rt_date", updatable = false, columnDefinition = "TIMESTAMP(0)")
    @CreationTimestamp
    @Setter(AccessLevel.NONE) // Запрещаем сеттер
    @ColumnDefault("CURRENT_TIMESTAMP(0)")
    @ToString.Include
    private LocalDateTime recognitionDate;


/*
    public UUID getId() {
        return id;
    }

    public String getFileInfo() {
        return fileInfo;
    }
    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getRecognitionDate() {
        return recognitionDate;
    }
    */
}

