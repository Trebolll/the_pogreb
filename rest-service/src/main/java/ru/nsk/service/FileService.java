package ru.nsk.service;

import org.apache.catalina.webresources.FileResource;
import org.springframework.core.io.FileSystemResource;
import ru.nsk.entity.enums.AppDocument;
import ru.nsk.entity.enums.AppPhoto;
import ru.nsk.entity.enums.BinaryContent;

public interface FileService {

    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);

}
