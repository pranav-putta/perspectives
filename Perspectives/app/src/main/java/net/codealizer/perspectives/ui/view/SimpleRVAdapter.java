package net.codealizer.perspectives.ui.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.util.Global;
import net.codealizer.perspectives.util.NewsAPIManager;

import java.util.List;

public class SimpleRVAdapter extends RecyclerView.Adapter<SimpleRVAdapter.SimpleViewHolder> {
    private List<NewsAPIManager.Perspective> dataSource;
    public SimpleRVAdapter(List<NewsAPIManager.Perspective> dataArgs){
        dataSource = dataArgs;
    }
    
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_perspective, parent, false);
        TextView textView = view.findViewById(R.id.rip);
        ImageView image = view.findViewById(R.id.rip2);

        SimpleViewHolder viewHolder = new SimpleViewHolder(view, textView, image);
        return viewHolder;
    }
    
    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public CardView item;
        public TextView t;
        public ImageView t2;
        public SimpleViewHolder(CardView itemView, TextView e, ImageView w) {
            super(itemView);
            item = itemView;
            t = e;
            t2 = w;
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.t.setText(dataSource.get(position).title);
        holder.t2.setImageResource(Global.emotionMap.get(dataSource.get(position).emoji));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}