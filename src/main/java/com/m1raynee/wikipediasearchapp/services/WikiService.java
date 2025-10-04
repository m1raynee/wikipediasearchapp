package com.m1raynee.wikipediasearchapp.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

import com.m1raynee.wikipediasearchapp.models.SearchResult;

public class WikiService {
    private static final String API_URL = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=";
    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    public SearchResult search(String queryText) throws Exception {
        String encodedQuery = URLEncoder.encode(queryText, "UTF-8");
        
        String fullUrl = API_URL + encodedQuery;
        System.out.println("Выполняется запрос: " + fullUrl);

        HttpRequest request = HttpRequest.newBuilder(URI.create(fullUrl))
            .GET()
            .header("User-Agent", "WikipediaSearchApp (emdarichev@stud.etu.ru)")
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Ошибка HTTP: " + response.statusCode());
        }

        return gson.fromJson(response.body(), SearchResult.class);
    }
}
