package com.example.letslearnjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letslearnjava.Models.RankModel;
import com.example.letslearnjava.R;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankModel> usersList;

    public RankAdapter(List<RankModel> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = usersList.get(position).getName();
        int score = usersList.get(position).getScore();
        int rank = usersList.get(position).getRank();

        holder.setData(name,score,rank);
    }

    @Override
    public int getItemCount() {
        if (usersList.size() > 10){
            return 10;
        }else {
            return usersList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nametV, rankTV, scoreTV, imgTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nametV = itemView.findViewById(R.id.name);
            rankTV = itemView.findViewById(R.id.rank);
            scoreTV = itemView.findViewById(R.id.score);
            imgTV = itemView.findViewById(R.id.img_text);
        }

        private void setData(String name, int score, int rank){
            nametV.setText(name);
            scoreTV.setText("Ұпай:" + score);
            rankTV.setText("Дәрежесі - " + rank);
            imgTV.setText(name.toUpperCase().substring(0,1));
        }
    }
}
