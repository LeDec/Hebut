package com.example.questApplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Entity.PersonEntity;
import com.example.questApplication.R;

import java.util.List;

public class VillageFriendsAdapter extends RecyclerView.Adapter<VillageFriendsAdapter.friendsViewHolder> {

    private Context context;
    private List<PersonEntity> friendsList;


    public VillageFriendsAdapter(Context context, List<PersonEntity> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_firends_item, parent, false);
        return new VillageFriendsAdapter.friendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(friendsViewHolder holder, int position) {
        PersonEntity data = friendsList.get(position);
        holder.tv_nickname.setText(data.getNickname());
        holder.tv_coin.setText(String.valueOf(data.getCoin()));
        holder.tv_friend_id.setText("No. " + data.get_id());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class friendsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_friend_id;
        private TextView tv_nickname;
        private TextView tv_coin;

        private ImageView tv_profile;

        public friendsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_friend_nickname);
            tv_coin = itemView.findViewById(R.id.tv_friend_coin);
            tv_friend_id = itemView.findViewById(R.id.tv_friend_id);
        }
    }
}
