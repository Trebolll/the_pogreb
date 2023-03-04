package ru.nsk.entity.enums;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_document")
public class AppDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String docName;
    private String telegramFileId;
    @OneToOne
    private BinaryContent binaryContent;
    private String mimeType;
    private Long fileSize;

}
