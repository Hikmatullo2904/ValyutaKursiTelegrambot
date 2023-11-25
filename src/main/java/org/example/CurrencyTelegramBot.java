package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CurrencyTelegramBot {
    private final TelegramBot bot = new TelegramBot("6762698594:AAF74Pzty7WX2DQjJfoyhhGL8qHO635FAeo");
    private static final String usdUrl = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/USD/";
    private static final String rubUrl = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/RUB/";
    public void run(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = update.message();
        if (message != null) {
            String text = message.text();
            Chat chat = message.chat();
            Long id = chat.id();
            if(text.equals("/start")) {
                SendMessage sendMessage = new SendMessage(id, "Men valyutalar qiymatini korsatuvchi botman \ndavom etish uchun /continue bosing");
                bot.execute(sendMessage);
            } else if(text.equals("/continue")) {
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                InlineKeyboardButton usd = new InlineKeyboardButton("USD");
                usd.callbackData("usd");
                InlineKeyboardButton rbl = new InlineKeyboardButton("RUBL");
                rbl.callbackData("rbl");
                InlineKeyboardButton both = new InlineKeyboardButton("Hammasi");
                both.callbackData("both");
                markup.addRow(usd, rbl);
                markup.addRow(both);

                SendMessage sendMessage = new SendMessage(id, "tanlang");
                sendMessage.replyMarkup(markup);
                bot.execute(sendMessage);

            }
        } else {
            String data = callbackQuery.data();
            Chat chat = callbackQuery.message().chat();
            Long id = chat.id();
            if(data.equals("usd")) {
                String usd = fetchDataFromUrl(usdUrl);
                String dollarKursi = getCurrencyValue(usd);
                String mes = "$1 dollarning narxi hozirgi paytda %s ga teng".formatted(dollarKursi);
                SendMessage sendMessage = new SendMessage(id, mes);
                bot.execute(sendMessage);
            } else if(data.equals("rbl")) {
                String rubl = fetchDataFromUrl(rubUrl);
                String rublKursi = getCurrencyValue(rubl);
                String mes = "1 rublning narxi hozirgi paytda %s ga teng".formatted(rublKursi);
                SendMessage sendMessage = new SendMessage(id, mes);
                bot.execute(sendMessage);
            } else if (data.equals("both")) {
                String rubl = fetchDataFromUrl(rubUrl);
                String usd = fetchDataFromUrl(usdUrl);

                String rublKursi = getCurrencyValue(rubl);
                String dollarKursi = getCurrencyValue(usd);

                String mes = "1 rublning narxi hozirgi paytda %s ga teng \n$1 dollarning narxi hozirgi paytda %s ga teng".formatted(rublKursi, dollarKursi);
                SendMessage sendMessage = new SendMessage(id, mes);

                bot.execute(sendMessage);
            }
        }
    }
    public static String getCurrencyValue(String str) {
        List<Currency> currencyInObjectFormat = getCurrencyInObjectFormat(str);
        String val = "";
        for(Currency currency : currencyInObjectFormat) {
            val = currency.getRate();
        }
        return val;
    }

    private static List<Currency> getCurrencyInObjectFormat(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Currency>>() {}.getType();
        return gson.fromJson(str, type);
    }

    private static String fetchDataFromUrl(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
