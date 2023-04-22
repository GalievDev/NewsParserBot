package org.galievdev.newsparser;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "###";
    final private String BOT_NAME = "###";
    Storage storage;
    ReplyKeyboardMarkup replyKeyboardMarkup;

    Bot() throws TelegramApiException {
        storage = new Storage();
        initKeyboard();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText())
        {
            Message inMess = update.getMessage();
            String chatID = getChatID(update);
            String response = parseMessage(inMess.getText());
            SendMessage outMess = new SendMessage();

            outMess.setReplyMarkup(replyKeyboardMarkup);
            outMess.setChatId(chatID);
            outMess.setText(response);
        }
    }

    public void sendNews() {
        SendMessage message = new SendMessage("469039689", storage.getNews());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getChatID(Update update) {
        Message message = update.getMessage();

        return message.getChatId().toString();
    }

    @Override
    public Message execute(SendAnimation sendAnimation) throws TelegramApiException {
        return super.execute(sendAnimation);
    }

    public String parseMessage(String textMsg) {
        String response;
        int i = 0;

        switch (textMsg) {
            case "/start" -> response = "Привет! Хочешь узнать новости Узбекистана? Жми /get";
            case "/get" -> response = storage.getNews();
            case "Просвяти" -> {
                i++;
                response = storage.getOldNews(i);
            }
            default -> response = "Сообщение не распознано";
        }

        return response;
    }

    void initKeyboard()
    {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton("Просвяти"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
}
