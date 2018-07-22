package net.codealizer.perspectives.util;

import android.os.AsyncTask;
import android.util.Log;

import com.aylien.textapi.responses.Aspect;
import com.aylien.textapi.responses.EntitiesSentiment;
import com.aylien.textapi.responses.Mention;
import com.aylien.textapi.responses.Summarize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

public class NewsAPIManager {

    String url = "https://newsapi.org/v2/everything?q=%q&sortBy=publishedAt&apiKey=be9b033e764e4b3ea8e815c90f19d76c";

    private NewsAPIManager() {

    }

    public static NewsAPIManager getInstance() {
        return new NewsAPIManager();
    }

    private String getJSON(String q) throws Exception {
        URL website = new URL(url.replaceAll("%q", q));
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public void getPerspectives(String query, NewsArticleAPIAsyncRequest.Callback<List<Perspective>> c) {
        NewsArticleAPIAsyncRequest req = new NewsArticleAPIAsyncRequest(query, c);
        req.execute();
    }

    public static class NewsArticleAPIAsyncRequest extends AsyncTask<String, String, List<Perspective>> {

        private String query;
        private Callback callback;

        NewsArticleAPIAsyncRequest(String query, Callback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected List<Perspective> doInBackground(String... strings) {
            List<Perspective> result = new ArrayList<>();
            try {
                String json = NewsAPIManager.getInstance().getJSON(query);

                ArticleWrapper wrapper = new Gson().fromJson(json, ArticleWrapper.class);
                StringBuilder text = new StringBuilder();

                for (Article a : wrapper.articles) {
                    text.append(a.description).append(" ").append(a.title);
                }

                ToneOptions toneOptions = new ToneOptions.Builder()
                        .text(text.toString())
                        .sentences(true)
                        .build();
                ToneAnalysis analysis= null;
                try {
                    analysis = WatsonAPIManager.getInstance().mAPI.tone(toneOptions).execute();
                } catch (Exception ex) {
                    Log.e("SU", ex.getMessage());
                }

                HashMap<String, String> map = new HashMap<>();
                for (SentenceAnalysis a : analysis.getSentencesTone()) {
                    if (a.getTones() !=  null) {
                        double max = 0;
                        ToneScore scoreToUse = null;
                        for (ToneScore s: a.getTones()) {
                            if (s.getScore() > max) {
                                max = s.getScore();
                                scoreToUse = s;
                            }
                        }
                        if (scoreToUse != null && !map.containsKey(scoreToUse.getToneName().toLowerCase())) {
                            map.put(scoreToUse.getToneName().toLowerCase(), a.getText());
                        }
                    }
                }

                for (String s : map.keySet()) {
                    result.add(new Perspective(map.get(s).substring(0, 40) + "...", "", s));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( List<Perspective> result) {
            callback.onCompleted(result);
        }

        public interface Callback<T> {

            void onCompleted(T data);

        }
    }

    public static class ArticleWrapper {
        public String statusCode;
        public int totalResults;
        public Article[] articles;

        public ArticleWrapper(String statusCode, int totalResults, Article[] articles) {
            this.statusCode = statusCode;
            this.totalResults = totalResults;
            this.articles = articles;
        }
    }

    public static class Article {
        Source source;
        String author;
        String title;
        String description;
        String url;
        String urlToImage;
        String publishedAt;

        public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt) {
            this.source = source;
            this.author = author;
            this.title = title;
            this.description = description;
            this.url = url;
            this.urlToImage = urlToImage;
            this.publishedAt = publishedAt;
        }
    }

    public static class Source {
        String id;
        String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Perspective {

        public String title;
        String url;
        public String emoji;

        public Perspective(String t, String u, String emoji) {
            title = t;
            url = u;
            this.emoji = emoji;
        }

    }

}
