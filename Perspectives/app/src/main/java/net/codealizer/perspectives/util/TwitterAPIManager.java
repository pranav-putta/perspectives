package net.codealizer.perspectives.util;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPIManager {

    private Twitter twitter;

    private static TwitterAPIManager instance;

    public static final String TWITTER_BUNDLE = "net.codealizer.perspectives.TWITTER_BUNDLE";

    private TwitterAPIManager() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey("GWBlZ7RyKYfOb8ezHpb24DBNe")
                .setOAuthConsumerSecret("0QA7R2QYEmKr8x6hz9ZOAMJNBCUZMdZkbq65SLs9y5Km9ltn8y")
                .setOAuthAccessToken("361438192-14lUk4kCUQmKhTv3wzZTUn8zWeyJPoDiOpFBc0xC")
                .setOAuthAccessTokenSecret("rHKTazFv8G8yvoZZxhKbD9LWjqqsK9RycxvOLgTToXkmh");

        TwitterFactory tf = new TwitterFactory(builder.build());
        this.twitter = tf.getInstance();
    }

    // Get a list of tweets
    public void query(String query, TwitterAPIAsyncRequest.Callback<QueryResult> callback) {
            TwitterAPIAsyncRequest request = new TwitterAPIAsyncRequest(query, callback);
            request.execute();
    }

    /**
     * Returns the most popular hashtag in an array of tweets
     * @param tweets
     * @return String containing most popular hashtag
     */
    public String popularHastag(List<Status> tweets) {
        List<HashtagEntity> hashtags= new ArrayList<>();

        for (Status s : tweets) {
            hashtags.addAll(Arrays.asList(s.getHashtagEntities()));
        }

        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < hashtags.size(); i++) {
            if (map.containsKey(hashtags.get(i).getText())) {
                map.put(hashtags.get(i).getText(), map.get(hashtags.get(i).getText()));
            } else {
                map.put(hashtags.get(i).getText(), 1);
            }
        }

        Set<String> set = map.keySet();
        int max = 0;
        String key = "";
        for (String s : set) {
            int m = map.get(s);
            if (m > max) {
                max = m;
                key = s;
            }
        }

        return key;
    }

    public static TwitterAPIManager getInstance() {
        if (instance == null) {
            instance = new TwitterAPIManager();
        }

        return instance;
    }

    public static class TwitterAPIAsyncRequest extends AsyncTask<String, String, QueryResult> {

        private String query;
        private Callback callback;

        TwitterAPIAsyncRequest(String query, Callback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected QueryResult doInBackground(String... strings) {
            QueryResult result = null;
            try {
                Query q = new Query(query);
                q.setCount(100);

                // Run twitter api
                result = TwitterAPIManager.getInstance().twitter.search(q);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
             return result;
        }

        @Override
        protected void onPostExecute(QueryResult result) {
            callback.onCompleted(result);
        }

        public interface Callback<T> {

            void onCompleted(T data);

        }
    }

}
