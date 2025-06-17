package com.example.exchanger2;

import okhttp3.OkHttpClient;//используется для выполнения HTTP-запросов. Он предоставляет методы для создания и настройки HTTP-клиента, который может отправлять запросы и получать ответы от серверов
import okhttp3.Request;//представляет собой HTTP-запрос. Он позволяет задавать URL, заголовки и другие параметры запроса
import okhttp3.Response;//представляет ответ от сервера на HTTP-запрос. Он содержит данные о статусе ответа, заголовках и теле ответа.
import org.json.JSONObject;
import org.springframework.stereotype.Component;//аннотация Spring, которая указывает на то, что класс является компонентом Spring. Это позволяет Spring автоматически обнаруживать и управлять экземплярами этого класса в контексте приложения.

import java.io.IOException;

@Component
public class CurrencyConverter {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/c7da8bd28869af70b42f1ff9/latest/";

    private final OkHttpClient client = new OkHttpClient();

    public double convert(double amount, String fromCurrency, String toCurrency) {
        try {
            String url = BASE_URL + fromCurrency;
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Неожиданный код " + response);

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            double rate = jsonObject.getJSONObject("conversion_rates").getDouble(toCurrency);
            return amount * rate;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при получении курса конвертации валюты.");
        }
    }
}