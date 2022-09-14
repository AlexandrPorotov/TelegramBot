package com.example.telegrambot.service;

import com.example.telegrambot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Creating your actual bot: The class must extends TelegramLongPollingBot
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig){
        this.botConfig = botConfig;
    }

    //This method must always return your Bot username
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    /*This method must always return your Bot Token
    (If you don't know it, you may want to talk with @BotFather).*/
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    //This method will be called when an Update is received by your bot.
    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if(update.hasMessage() && update.getMessage().hasText()){

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            System.out.println("request : " + "\"" + messageText + "\"");

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    sendCustomKeyboard(chatId);
                    break;
                case "/date":
                    startCommandReceived(chatId, LocalDateTime.now().toString());
                    break;
                case "/inlineCmd":
                    sendInlineKeyboard(chatId);
                    break;
                default: sendMassage(chatId, "Sorry, i don`t know this command.");
            }
        }

    }

    //кнопки в чате
    private void sendCustomKeyboard(Long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Custom Keyboard");

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button
        row.add(new KeyboardButton("/start"));
        row.add(new KeyboardButton("/date"));
        // Add the first tow to the keyboard
        keyboard.add(row);
        // Set the keyboard to markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }

    }

    //Кнопки под постом
    public void sendInlineKeyboard(Long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Inline model below.");

        // Create InlineKeyboardMarkup object
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // Create the keyboard (list of InlineKeyboardButton list)
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        // Create a list for buttons
        List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();
        // Initialize each button, the text must be written
        InlineKeyboardButton youtube= new InlineKeyboardButton("youtube");
        // Also must use exactly one of the optional fields,it can edit  by set method
        youtube.setUrl("https://www.youtube.com");
        // Add button to the list
        Buttons.add(youtube);
        // Initialize each button, the text must be written
        InlineKeyboardButton github= new InlineKeyboardButton("github");
        // Also must use exactly one of the optional fields,it can edit  by set method
        github.setUrl("https://github.com");
        // Add button to the list
        Buttons.add(github);
        keyboard.add(Buttons);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            // Send the message
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startCommandReceived(Long chatId, String name){

        String answer = "Hi, " + name + "!";
        System.out.println("response : " + "\"" + answer + "\"");
        sendMassage(chatId, answer);

    }

    private void sendMassage(Long chatId, String textToSend){

        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(String.valueOf(chatId)); //Required to send a message to a specific chat
        message.setText(textToSend);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException ex) {
            throw new RuntimeException(ex);
        }
    }
}
