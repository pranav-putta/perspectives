package net.codealizer.perspectives.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.util.TwitterAPIManager;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private String[] mTopics;
    private int[] res;
    private Activity ac;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public RelativeLayout layout;

        public ViewHolder(RelativeLayout l, TextView v, ImageView w) {
            super(l);
            this.layout = l;
            mTextView = v;
            mImageView = w;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TopicListAdapter(String[] myDataset, int[] res, Activity ac) {
        mTopics = myDataset;
        this.res = res;
        this.ac = ac;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_topic, parent, false);

        TextView t = v.findViewById(R.id.card_title);
        ImageView i = v.findViewById(R.id.card_image);
        ViewHolder vh = new ViewHolder(v, t, i);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mTopics[position]);
        holder.mImageView.setImageResource(res[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ac, ItemActivity.class);
                String value = "";
                if (position == 0) {
                    value = "Donald Trump";
                } else if (position == 1) {
                    value = "Elon Musk";
                } else if (position == 2) {
                    value = "stock market";
                } else if (position == 3) {
                    value = "Intel";
                }

                intent.putExtra(TwitterAPIManager.TWITTER_BUNDLE, value);
                ac.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTopics.length;
    }
}
