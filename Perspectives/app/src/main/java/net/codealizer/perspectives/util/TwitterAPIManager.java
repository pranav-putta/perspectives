package net.codealizer.perspectives.util;

import java.util.List;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPIManager {

    private Twitter twitter;

    private static TwitterAPIManager instance;

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
    public List<Status> getTweets(String query) {
        try {
            Query q = new Query(query);
            return twitter.search(q).getTweets();
        } catch (TwitterException ex) {
            return null;
        }
    }

    public static TwitterAPIManager getInstance() {
        if (instance == null) {
            instance = new TwitterAPIManager();
        }

        return instance;
    }

}
