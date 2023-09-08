package com.example.questApplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Entity.DungeonEntity;
import com.example.questApplication.Entity.DungeonQuestEntity;
import com.example.questApplication.Entity.QuestEntity;
import com.example.questApplication.Enum.QuestEnum;
import com.example.questApplication.R;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DungeonContainerAdapter extends RecyclerView.Adapter<DungeonContainerAdapter.dungeonHolder> {
    private Context context;
    private List<DungeonEntity> dungeonEntityList;

    private int user_id;
    private int dungeon_id;


    //创建构造函数
    public DungeonContainerAdapter(Context context, List<DungeonEntity> dungeonEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.dungeonEntityList = dungeonEntityList;//实体类数据ArrayList

    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DungeonContainerAdapter.dungeonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_dungeon, parent, false);
        DungeonContainerAdapter.dungeonHolder dungeon = new DungeonContainerAdapter.dungeonHolder(itemView);
        dungeon.btn_dungeon_yes.setOnClickListener(view -> {
            TextView tv_dungeon_id = itemView.findViewById(R.id.tv_dungeon_id);
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确定参与副本？");
                builder.setMessage("副本凶险异常，只有真正的勇者才可以进入！");
                builder.setIcon(R.drawable.pixel_hero);
                builder.setPositiveButton("一往无前", (dialogInterface, i) -> {
                    SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                    user_id = Integer.valueOf(sp.getString("user_id", null));
                    String dungeon_string = tv_dungeon_id.getText().toString();
                    dungeon_id = Integer.valueOf(dungeon_string.substring(4, dungeon_string.length()));
                    Thread join = new Thread(() -> {
                        try {
                            String json = "{\n" +
                                    "  \"dungeon_id\": " + dungeon_id + ",\n" +
                                    "  \"user_id\": " + user_id + "\n" +
                                    "}";
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://140.210.204.183:8081/project/r-user-dungeon-table/joinDungeon")
                                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            int code = jsonObject.getInt("code");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    join.start();
                    try {
                        join.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog2 = builder
                        .create();
                alertDialog2.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return dungeon;
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(DungeonContainerAdapter.dungeonHolder holder, int position) {
        DungeonEntity data = dungeonEntityList.get(position);
        holder.tv_title.setText(data.getDungeonTitle());
        String type = "每日任务";
        for (DungeonQuestEntity quest : data.getQuestEntityList()) {
            if (Objects.equals(quest.getQuest_type(), "daily")) {
                type = "每日任务";
            } else if (Objects.equals(quest.getQuest_type(), "weekly")) {
                type = "每周任务";
            } else {
                type = "成就任务";
            }
            holder.tv_container.setText(holder.tv_container.getText().toString() + quest.getQuest_title() + "(" + type + ")\n");
        }
        if (data.isComplete()) {
            holder.btn_dungeon_yes.setClickable(false);
        }
        holder.tv_dungeon_id.setText("ID: " + data.getDungeon_id());
        holder.tv_treasure.setText(" * " + data.getCoin());
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return dungeonEntityList.size();
    }

    class dungeonHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_dungeon_id;
        TextView tv_title;
        TextView tv_container;
        TextView tv_treasure;

        Button btn_dungeon_yes;


        public dungeonHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_quest);
            tv_dungeon_id = (TextView) itemView.findViewById(R.id.tv_dungeon_id);
            tv_title = (TextView) itemView.findViewById(R.id.tv_dungeon_title);
            tv_container = (TextView) itemView.findViewById(R.id.tv_dungeon_container);
            tv_treasure = (TextView) itemView.findViewById(R.id.tv_dungeon_treasure);
            btn_dungeon_yes = (Button) itemView.findViewById(R.id.btn_dungeon_yes);
        }
    }
}
