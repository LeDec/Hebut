package com.example.questApplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.R;

import java.util.List;

public class QuestTitleAdapter extends RecyclerView.Adapter<QuestTitleAdapter.myViewHolder> {
    private Context context;
    private List<String> titles;


    //创建构造函数
    public QuestTitleAdapter(Context context, List<String> titles) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.titles = titles;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_tab_item, parent, false);
        return new myViewHolder(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        String data = titles.get(position);
        holder.tv_title.setText(data);
        switch (data) {
            case "每日任务":
                holder.rec_tab_item.requestFocus();
                break;
        }

    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return titles.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private LinearLayout rec_tab_item;

        public myViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_item);
            rec_tab_item = (LinearLayout) itemView.findViewById(R.id.rec_tab_item);
            itemView.setOnFocusChangeListener((view, b) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(view, titles.get(getLayoutPosition()));
                }
            });

        }
    }

    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, String data);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //再定义一个int类型的返回值方法

}
