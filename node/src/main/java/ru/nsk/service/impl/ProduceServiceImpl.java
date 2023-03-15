package ru.nsk.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.nsk.service.ProducerService;

import static ru.nsk.RabbitQueue.ANSWER_MESSAGE;


@Service
public class ProduceServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProduceServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void producerAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}