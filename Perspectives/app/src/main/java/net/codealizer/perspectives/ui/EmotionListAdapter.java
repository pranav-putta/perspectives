package net.codealizer.perspectives.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.util.Global;

import java.util.List;

public class EmotionListAdapter extends RecyclerView.Adapter<EmotionListAdapter.ViewHolder> {

private List<ToneScore> mTones;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTextView;
    public ImageView mImage;
    public ViewHolder(RelativeLayout l, TextView v, ImageView i) {
        super(l);
        mTextView = v;
        mImage = i;
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public EmotionListAdapter(List<ToneScore> myDataset) {
        mTones = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmotionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emotion, parent, false);

        TextView t = v.findViewById(R.id.item_emotion_label);
        ImageView imageView = v.findViewById(R.id.item_emotion_icon);
        EmotionListAdapter.ViewHolder vh = new EmotionListAdapter.ViewHolder(v, t, imageView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EmotionListAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mTones.get(position).getToneName());
        holder.mImage.setImageResource(Global.emotionMap.get(mTones.get(position).getToneName().toLowerCase()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTones.size();
    }
}
