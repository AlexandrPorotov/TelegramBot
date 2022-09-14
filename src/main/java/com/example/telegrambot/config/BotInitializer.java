package com.example.telegrambot.config;

import com.example.telegrambot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    @Autowired
    TelegramBot bot;

    /*Instantiate TelegramBotsApi and register our new bot: For this part,
    we need to actually perform 2 steps:
    Instantiate Telegram Api and Register our Bot.*/

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException{

        /*Easy as well, just create a new instance.
        Remember that a single instance can handle different bots
        but each bot can run only once (Telegram doesn't support concurrent calls to GetUpdates)*/
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot); //There we need to register a new instance of our previously created bot class in the api
        } catch (TelegramApiException ex){
            ex.printStackTrace();
        }
    }

}
