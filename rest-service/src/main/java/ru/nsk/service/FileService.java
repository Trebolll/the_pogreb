package ru.nsk.service;

import ru.nsk.entity.AppDocument;
import ru.nsk.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}
