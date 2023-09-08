package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务实体类")
public class QuestResponse {

    @ApiModelProperty(value = "任务编号")
    private int quest_id;

    @ApiModelProperty(value = "任务标题")
    private String title;

    @ApiModelProperty(value = "任务类型")
    private String questEnum;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "自定义奖励")
    private String self_treasure;

    @ApiModelProperty(value = "连击")
    private int combo;

    @ApiModelProperty(value = "是否完成")
    private String is_complete;

    public int compareTo(QuestResponse q) {
        int a = 0, b = 0;
        switch (q.questEnum) {
            case "daily":
                a = 1;
                break;
            case "weekly":
                a = 2;
                break;
            case "achievement":
                a = 3;
                break;
            case "dungeon":
                a = 4;
                break;
        }
        switch (this.questEnum) {
            case "daily":
                b = 1;
                break;
            case "weekly":
                b = 2;
                break;
            case "achievement":
                b = 3;
                break;
            case "dungeon":
                b = 4;
                break;
        }
        if (b > a) return 1;
        else if (a > b) return -1;
        else return 0;
    }
}
