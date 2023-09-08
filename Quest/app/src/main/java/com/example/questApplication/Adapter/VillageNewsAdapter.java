package com.example.questApplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Entity.NoteEntity;
import com.example.questApplication.R;

import java.util.List;

public class VillageNewsAdapter extends RecyclerView.Adapter<VillageNewsAdapter.newsViewHolder> {
    private Context context;

    private List<NoteEntity> noteEntityList;

    public VillageNewsAdapter(Context context, List<NoteEntity> noteEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.noteEntityList = noteEntityList;
    }

    @NonNull
    @Override
    public VillageNewsAdapter.newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_news, parent, false);
        newsViewHolder quest = new newsViewHolder(itemView);
        return quest;
    }

    @Override
    public void onBindViewHolder(@NonNull VillageNewsAdapter.newsViewHolder holder, int position) {
        NoteEntity data = noteEntityList.get(position);
        holder.tv_note_title.setText(data.getTitle());
        holder.tv_note_say.setText(data.getSay());
        holder.tv_note_applicant.setText(data.getApplicant());
    }

    @Override
    public int getItemCount() {
        return noteEntityList.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_note_title;
        TextView tv_note_say;
        TextView tv_note_applicant;
        Button btn_note_read;

        public newsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_note_title = itemView.findViewById(R.id.tv_note_title);
            tv_note_say = itemView.findViewById(R.id.tv_note_say);
            tv_note_applicant = itemView.findViewById(R.id.tv_note_applicant);
            btn_note_read = itemView.findViewById(R.id.btn_note_read);
        }
    }
}
