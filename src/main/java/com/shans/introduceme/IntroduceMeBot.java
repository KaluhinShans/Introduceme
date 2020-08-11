package com.shans.introduceme;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class IntroduceMeBot extends TelegramLongPollingBot {

    private static final String TOKEN = "1222190563:AAHHojpOEqJe1782YnGdTXtuPSSbC-BDSxg";
    private static final String BOTNAME = " Introduceme_bot";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public IntroduceMeBot(DefaultBotOptions options){super(options);}

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            long chat_id = message.getChatId();
            SendMessage sendMessage = new SendMessage().setChatId(chat_id);

            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            try {
                sendMessage.setText(getMessage(message.getText()));
                execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    public String getMessage(String msg){
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if(msg.equals("Hello")){
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Popular");
            keyboardFirstRow.add("News");
            keyboardSecondRow.add("Info");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Choose";
        }
        if(msg.equals("Popular")){
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Hello");
            keyboardFirstRow.add("News");
            keyboardSecondRow.add("Info");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Choose";
        }
        return "Nope";
    }

    @Override
    public String getBotUsername() {
        return BOTNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
