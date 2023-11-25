package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CurrencyFetcher {

    private static final String token = "6762698594:AAF74Pzty7WX2DQjJfoyhhGL8qHO635FAeo";
    private static final ThreadLocal<CurrencyTelegramBot> bot = ThreadLocal.withInitial(CurrencyTelegramBot::new);

    public static void main(String[] args) {

        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> list) {
                for (Update update : list) {
                    CompletableFuture.runAsync(() -> bot.get().run(update),
                            Executors.newFixedThreadPool(10));
                }
                return CONFIRMED_UPDATES_ALL;
            }
        }, Throwable::printStackTrace);


    }

}
