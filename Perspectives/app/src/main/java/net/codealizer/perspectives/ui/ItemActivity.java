package net.codealizer.perspectives.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.ui.view.SimpleRVAdapter;
import net.codealizer.perspectives.util.Global;
import net.codealizer.perspectives.util.NewsAPIManager;
import net.codealizer.perspectives.util.TwitterAPIManager;
import net.codealizer.perspectives.util.WatsonAPIManager;

import java.util.ArrayList;
import java.util.List;

import twitter4j.QueryResult;
import twitter4j.Status;

public class ItemActivity extends AppCompatActivity {

    String query;
    ProgressDialog progressDialog;

    ToneAnalysis analysis;
    List<Status> tweets;

    TextView queryLabel;
    ImageView emoji1;
    ImageView emoji2;
    ImageView emoji3;

    TextView emoji1Text;
    TextView emoji2Text;
    TextView emoji3Text;

    TextView hashtag;
    TextView h_tweet_1;
    TextView h_tweet_2;
    TextView h_tweet_3;

    RecyclerView perspective;

    private List<NewsAPIManager.Perspective> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Retrieve Twitter feed bundle
        if (getIntent().hasExtra(TwitterAPIManager.TWITTER_BUNDLE)) {
            query = getIntent().getStringExtra(TwitterAPIManager.TWITTER_BUNDLE);
        }

        getSupportActionBar().setTitle("\"" + query + "\"");

        initialize();
    }

    private void initialize() {

        showProgress();
        TwitterAPIManager.getInstance().query(query, new TwitterAPIManager.TwitterAPIAsyncRequest.Callback<QueryResult>() {
            @Override
            public void onCompleted(QueryResult data) {
                tweets = data.getTweets();
                StringBuilder builder = new StringBuilder();
                for (Status s : data.getTweets()) {
                    builder.append(s.getText());
                }

                WatsonAPIManager.getInstance().analyze(builder.toString(), new WatsonAPIManager.WatsonAsyncRequest.Callback<ToneAnalysis>() {
                    @Override
                    public void onCompleted(ToneAnalysis data) {
                        hideProgress();
                        analysis = data;

                        NewsAPIManager.getInstance().getPerspectives(query, new NewsAPIManager.NewsArticleAPIAsyncRequest.Callback<List<NewsAPIManager.Perspective>>() {
                            @Override
                            public void onCompleted(List<NewsAPIManager.Perspective> data) {
                                ItemActivity.this.data = data;
                                buildUI();

                            }
                        });

                    }
                });
            }
        });
    }

    private void buildUI() {
        queryLabel = findViewById(R.id.item_query_label);
        emoji1 = findViewById(R.id.item_emotion_1);
        emoji2 = findViewById(R.id.item_emotion_2);
        emoji3 = findViewById(R.id.item_emotion_3);
        emoji1Text = findViewById(R.id.item_emotion_1_label);
        emoji2Text = findViewById(R.id.item_emotion_2_label);
        emoji3Text = findViewById(R.id.item_emotion_3_label);
        hashtag = findViewById(R.id.item_hashtag);
        h_tweet_1 = findViewById(R.id.item_h_tweet_1);
        h_tweet_2 = findViewById(R.id.item_h_tweet_2);
        h_tweet_3 = findViewById(R.id.item_h_tweet_3);
        perspective = findViewById(R.id.perspective_list);

        queryLabel.setText("\""+query+"\"");

        List<ToneScore> tones = analysis.getDocumentTone().getTones();
        if (tones != null && tones.size() > 0) {
            if (tones.size() == 1) {
                emoji2.setImageResource(Global.emotionMap.get(tones.get(0).getToneName().toLowerCase()));
                emoji2Text.setText(tones.get(0).getToneName());
            } else if (tones.size() == 2) {
                emoji1.setImageResource(Global.emotionMap.get(tones.get(0).getToneName().toLowerCase()));
                emoji2.setImageResource(Global.emotionMap.get(tones.get(1).getToneName().toLowerCase()));
                emoji1Text.setText(tones.get(0).getToneName());
                emoji2Text.setText(tones.get(1).getToneName());
            } else {
                emoji1.setImageResource(Global.emotionMap.get(tones.get(0).getToneName().toLowerCase()));
                emoji2.setImageResource(Global.emotionMap.get(tones.get(1).getToneName().toLowerCase()));
                emoji3.setImageResource(Global.emotionMap.get(tones.get(2).getToneName().toLowerCase()));
                emoji1Text.setText(tones.get(0).getToneName());
                emoji2Text.setText(tones.get(1).getToneName());
                emoji3Text.setText(tones.get(2).getToneName());
            }
        }

        String h = TwitterAPIManager.getInstance().popularHastag(tweets);
        hashtag.setText("#" + h);

        if (tweets.size() > 2) {
            h_tweet_1.setText(tweets.get(0).getText());
            h_tweet_2.setText(tweets.get(1).getText());
            h_tweet_3.setText(tweets.get(2).getText());
        }

        if (data != null) {


            perspective.setHasFixedSize(true);
            perspective.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            perspective.setAdapter(new SimpleRVAdapter(data));
        }


    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "Please wait", "Analyzing data for your search");
        }

        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.hide();
    }
}
