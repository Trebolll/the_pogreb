package ru.nsk.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nsk.entity.enums.AppDocument;
import ru.nsk.entity.enums.AppPhoto;
import ru.nsk.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long docId, LinkType linkType);
}
