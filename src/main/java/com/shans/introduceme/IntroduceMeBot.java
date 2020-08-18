package com.shans.introduceme;

import com.shans.introduceme.information.ForceReply;
import com.shans.introduceme.information.PersonInformation;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class IntroduceMeBot extends TelegramLongPollingBot {

    private static final String TOKEN = "1222190563:AAHHojpOEqJe1782YnGdTXtuPSSbC-BDSxg";
    private static final String BOTNAME = " Introduceme_bot";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow keyboardFirstRow = new KeyboardRow();
    private KeyboardRow keyboardSecondRow = new KeyboardRow();

    private ForceReply forceReply = new ForceReply();

    public IntroduceMeBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            long chat_id = message.getChatId();
            SendMessage sendMessage = new SendMessage().setChatId(chat_id);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            try {
                String answer = getAnswer(message);
                switch (answer) {
                    case "photo":
                        execute(sendMePhoto(message));
                        break;
                    case "projects":
                        execute(sendMessage.setText(PersonInformation.getProjects()).enableMarkdown(true));
                        break;
                    case "leaveme":
                        sendMessage.setText("Give me you contacts");
                        execute(sendMessage);
                        break;
                    case "contact":
                        setInlineKeyboard(sendMessage, "Back");
                        sendMessage.setText("Write me");
                        forceReply.usersWaiting.put(chat_id, "write");
                        execute(sendMessage);
                        break;
                    default:
                        execute(sendMessage.setText(answer));
                        break;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            if (call_data.equalsIgnoreCase("back")) {
                String answer = "Ok";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SendPhoto sendMePhoto(Message message) {
        SendPhoto sendPhoto = new SendPhoto().setChatId(message.getChatId());
        sendPhoto.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendPhoto.setPhoto("me", new FileInputStream(new File("./images/me.jpg")));
            sendPhoto.setCaption(PersonInformation.getCaption());
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

    private String getAnswer(Message message) {
        if (message.hasContact()) {
            return "contact";
        }
        String msg = message.getText();
        long id =message.getChatId();
        //User input waiting?
        if(forceReply.usersWaiting.containsKey(id)){
            forceReply.usersWaiting.remove(id);
            System.out.println(msg);
            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Contacts");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "Thanks";
        }
        if (msg.equalsIgnoreCase("/start") || msg.equalsIgnoreCase("Me")) {
            return "photo";
        }
        if (msg.equalsIgnoreCase("Projects")) {
            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Contacts");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "projects";
        }
        if (msg.equalsIgnoreCase("Contacts")) {

            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Projects");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "contacts";
        }
        if (msg.equalsIgnoreCase("Leave me massage")) {
            AddKeyboard keyboardAdd = () -> {
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText("Share your number ->").setRequestContact(true);
                keyboardSecondRow.add("Back");
                keyboardFirstRow.add(keyboardButton);
            };
            cleanBoardAndAdd(keyboardAdd);
            return "leaveme";
        }
        if (msg.equalsIgnoreCase("Back")) {
            AddKeyboard keyboardAdd = () -> {
                keyboardFirstRow.add("Me");
                keyboardFirstRow.add("Projects");
                keyboardSecondRow.add("Leave me massage");
            };
            cleanBoardAndAdd(keyboardAdd);
            return "back";
        }
        return "I haven't answer for your massage";
    }

    private SendMessage setInlineKeyboard(SendMessage message, String s) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(s).setCallbackData(s));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    private void cleanBoardAndAdd(AddKeyboard keyboardAdd) {
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
    interface AddKeyboard {
        void add();
    }
}
