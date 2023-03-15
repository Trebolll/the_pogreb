package ru.nsk.service;

import ru.nsk.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
