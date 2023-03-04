package ru.nsk.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nsk.dao.AppUserDAO;
import ru.nsk.dao.RawDataDAO;
import ru.nsk.entity.AppUser;
import ru.nsk.entity.RawData;
import ru.nsk.entity.enums.AppDocument;
import ru.nsk.entity.enums.AppPhoto;
import ru.nsk.exeption.UploadFileException;
import ru.nsk.service.FileService;
import ru.nsk.service.MainService;
import ru.nsk.service.ProducerService;
import ru.nsk.service.enums.LinkType;
import ru.nsk.service.enums.ServiceCommand;

import static ru.nsk.entity.enums.UserState.BASIC_STATE;
import static ru.nsk.entity.enums.UserState.WAIT_FROM_EMAIL_STATE;
import static ru.nsk.service.enums.ServiceCommand.*;

@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final AppUserDAO appUserDao;
    private final ProducerService produceService;
    private final FileService fileService;


    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService produceService, FileService fileService, AppUserDAO appUserDao) {
        this.rawDataDAO = rawDataDAO;
        this.produceService = produceService;
        this.fileService = fileService;
        this.appUserDao = appUserDao;
    }



    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getUserState();
        var text = update.getMessage().getText();
        var output = "";

        var serviceCommand = ServiceCommand.fromValue(text);
        if(CANCEL.equals(serviceCommand)){
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        }else if(WAIT_FROM_EMAIL_STATE.equals(userState)){
            //TODO добавить реализацию регистрации по электронной почте
        }else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка введите /cancel";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId,appUser)){
            return;
        }
        try {
            AppDocument doc =fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            var answer = "Документ успешно загружен. " +
                    "Ссылка на скачивание: " + link;
            sendAnswer(answer,chatId);
        }catch (UploadFileException ex){
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже";
            sendAnswer(error,chatId);
        }
    }
    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId,appUser)){
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            var answer = "Документ успешно загружен. " +
                    "Ссылка на скачивание: " + link;
            sendAnswer(answer,chatId);
        }catch (UploadFileException ex){
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже";
            sendAnswer(error,chatId);
        }

        //TODO добавить сохранение фото
        var answer = "Фото успешно загружено. "
                + "Ссылка на скачивание: https://www.photo/download";
        sendAnswer(answer,chatId);
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getUserState();
        if(!appUser.getIsActive()){
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента ";
            sendAnswer(error,chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов ";
            sendAnswer(error,chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        produceService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        var serviceCommand = ServiceCommand.fromValue(cmd);
        if(REGISTRATION.equals(serviceCommand)){
            //TODO добавить регистрацию
            return "Временно недоступно";
        } else if (HELP.equals(serviceCommand)) {
            return help();
        }else if (START.equals(serviceCommand)){
            return "Привет. Чтобы посмотреть список доступных команд введите /help ";
        }else {
            return "Неизвестная команда. Чтобы посмотреть список доступных команд введите /help ";
        }
    }

    private String help() {
        return "Список доступных команд: \n"
                +"/cancel - отмена выполнения текущей команды;\n"
                +"/registration - регистрация пользователя";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserDao.save(appUser);
        return "Команда отменена ";
    }

    private AppUser findOrSaveAppUser(Update update){
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDao.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser == null){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .userState(BASIC_STATE)
                    .build();
            return appUserDao.save(transientAppUser);
        }

        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder().
                event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
