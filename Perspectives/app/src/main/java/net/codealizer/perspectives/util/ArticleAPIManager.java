package net.codealizer.perspectives.util;

import android.util.Log;

import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.AspectBasedSentimentParams;
import com.aylien.textapi.parameters.ConceptsParams;
import com.aylien.textapi.parameters.EntityLevelSentimentParams;
import com.aylien.textapi.parameters.SentimentParams;
import com.aylien.textapi.parameters.SummarizeParams;
import com.aylien.textapi.responses.Aspect;
import com.aylien.textapi.responses.AspectSentence;
import com.aylien.textapi.responses.AspectsSentiment;
import com.aylien.textapi.responses.Concepts;
import com.aylien.textapi.responses.EntitiesSentiment;
import com.aylien.textapi.responses.EntitySentiment;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;

import java.util.List;

public class ArticleAPIManager {

    private static ArticleAPIManager instance;

    private TextAPIClient mAPI;
    private static final String APP_ID = "d4bfba5a";
    private static final String APP_KEY = "55c2fff1ec0131acfa8b3622a2b02d8d";

    private ArticleAPIManager() {
        mAPI = new TextAPIClient(APP_ID, APP_KEY);
    }

    public static ArticleAPIManager getInstance() {
        if (instance == null) {
            instance = new ArticleAPIManager();
        }

        return instance;
    }

    public String[] extractAspects(String text, String query) {
        try {
            Summarize c = mAPI.summarize(SummarizeParams.newBuilder()
                    .setText(text).setTitle(query).build());
            return c.getSentences();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        return null;
    }

}
