package net.codealizer.perspectives.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchBar;
import com.lapism.searchview.widget.SearchView;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.ui.fragments.FavoritesFragment;
import net.codealizer.perspectives.ui.fragments.NewTopicsFragment;
import net.codealizer.perspectives.util.TwitterAPIManager;

import java.util.List;

import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

public class MainActivity extends AppCompatActivity {

    private SearchView searchBar;
    private RecyclerView list;

    private NewTopicsFragment f1;
    private FavoritesFragment f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.main_list);
        searchBar = findViewById(R.id.main_toolbar);
        searchBar.setOnQueryTextListener(new Search.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(CharSequence s) {
                // Check if query is empty
                if (!s.toString().isEmpty()) {
                    // Query using an async task
                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                    intent.putExtra(TwitterAPIManager.TWITTER_BUNDLE, s);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onQueryTextChange(CharSequence newText) {

            }
        });


        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(new TopicListAdapter(new String[]{"Politics", "Science", "Finance", "Technology"}, new int[] {R.mipmap.politics, R.mipmap.science, R.mipmap.finance, R.mipmap.tech}, this));
        searchBar.setHint("Search for people, places, topics, and more");

    }


}
