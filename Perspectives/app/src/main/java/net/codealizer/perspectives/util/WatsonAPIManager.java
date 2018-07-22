package net.codealizer.perspectives.util;

import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

public class WatsonAPIManager {

    private static final String VERSION = "2017-09-21";
    private static final String USERNAME = "ec24a73d-2edd-4f97-bb4f-e2280b898850";
    private static final String PASSWORD = "kAvNtzHgrJOo";

     ToneAnalyzer mAPI;

    private WatsonAPIManager() {
        mAPI = new ToneAnalyzer(VERSION, USERNAME, PASSWORD);
    }

    public static WatsonAPIManager getInstance() {
        return new WatsonAPIManager();
    }

    /**
     * Runs an API request on thread
     * @param text input text
     */
    public void analyze(String text, WatsonAsyncRequest.Callback<ToneAnalysis> callback) {
        WatsonAsyncRequest request = new WatsonAsyncRequest(text, callback);
        request.execute();
    }

    public static class WatsonAsyncRequest extends AsyncTask<String, String, ToneAnalysis> {

        private String query;
        private Callback callback;

        WatsonAsyncRequest(String query, Callback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected ToneAnalysis doInBackground(String... strings) {
                ToneOptions toneOptions = new ToneOptions.Builder()
                        .text(query)
                        .sentences(true)
                        .build();
                ToneAnalysis analysis= null;
                try {
                    analysis = WatsonAPIManager.getInstance().mAPI.tone(toneOptions).execute();
                } catch (Exception ex) {
                    Log.e("SU", ex.getMessage());
                }

            return analysis;
        }

        @Override
        protected void onPostExecute(ToneAnalysis analysis) {
            callback.onCompleted(analysis);
        }

        public interface Callback<T> {

            void onCompleted(T data);

        }
    }


}
