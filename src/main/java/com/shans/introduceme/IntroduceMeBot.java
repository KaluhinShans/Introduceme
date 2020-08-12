package com.shans.introduceme;

import com.shans.introduceme.information.PersonInformation;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class IntroduceMeBot extends TelegramLongPollingBot {

    private static final String TOKEN = "1222190563:AAHHojpOEqJe1782YnGdTXtuPSSbC-BDSxg";
    private static final String BOTNAME = " Introduceme_bot";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow keyboardFirstRow = new KeyboardRow();
    private KeyboardRow keyboardSecondRow = new KeyboardRow();

    public IntroduceMeBot(DefaultBotOptions options){super(options);}

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            long chat_id = message.getChatId();
            SendMessage sendMessage = new SendMessage().setChatId(chat_id);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            try {
                String s = getAnswer(message);
                switch (s){
                    case "photo":
                        execute(sendMePhoto(message));
                        break;
                    case "project":
                        execute(sendMessage.setText("Git sha"));
                    default:
                        execute(sendMessage.setText(s));
                        break;
                }
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    private SendPhoto sendMePhoto(Message message){
        SendPhoto sendPhoto = new SendPhoto().setChatId(message.getChatId());
        PersonInformation pi = new PersonInformation();
        sendPhoto.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendPhoto.setPhoto("me", new FileInputStream(new File("./images/me.jpg")));
            sendPhoto.setCaption(pi.getCaption());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AddKeyboard keyboardAdd = () -> {
            keyboardFirstRow.add("Projects");
            keyboardFirstRow.add("Contacts");
            keyboardSecondRow.add("Leave me massage");
        };
        cleanBoardAndAdd(keyboardAdd);
        return sendPhoto;
    }

    private String getAnswer(Message message){
        String msg = message.getText();

        if(msg.equalsIgnoreCase("/start") || msg.equalsIgnoreCase("Me" ) || msg.equalsIgnoreCase("Back" )){
            return "photo";
        }
        if(msg.equalsIgnoreCase("Projects")){
            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Contacts");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "projects";
        }
        if(msg.equalsIgnoreCase("Contacts")){

            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Projects");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "contacts";
        }
        if(msg.equalsIgnoreCase("Leave me massage")){
            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Back");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "Write me, and give me you contacts";
        }
        return "I haven't answer for your massage";
    }

    private void cleanBoardAndAdd(AddKeyboard keyboardAdd){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();
        keyboardAdd.add();

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername() {
        return BOTNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @FunctionalInterface
    interface AddKeyboard{
        void add();
    }
}
