package ru.nsk.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;


public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
