package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.AdminTable;
import com.android.quest.project.entity.DungeonTable;
import com.android.quest.project.entity.RUserDungeonTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.request.DeleteDungeonRequest;
import com.android.quest.project.entity.response.CheckDungeonListResponse;
import com.android.quest.project.entity.response.CheckDungeonResponse;
import com.android.quest.project.mapper.AdminTableMapper;
import com.android.quest.project.mapper.DungeonTableMapper;
import com.android.quest.project.mapper.RUserDungeonTableMapper;
import com.android.quest.project.service.IDungeonTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class DungeonTableServiceImpl extends ServiceImpl<DungeonTableMapper, DungeonTable> implements IDungeonTableService {

    @Resource
    AdminTableMapper adminTableMapper;
    @Resource
    DungeonTableMapper dungeonTableMapper;
    @Resource
    RUserDungeonTableMapper rUserDungeonTableMapper;
    @Override
    public CheckDungeonListResponse checkDungeonList(CheckRequest checkRequest) {
        CheckDungeonListResponse checkDungeonListResponse = new CheckDungeonListResponse();
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,checkRequest.getUser_id())
        );
        if(admin == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            List<CheckDungeonResponse> checkDungeonResponseList = new ArrayList<>();
            List<DungeonTable> dungeonTableList = dungeonTableMapper.selectList(
                    Wrappers.lambdaQuery(DungeonTable.class)
            );
            for(DungeonTable dungeonTable : dungeonTableList){
                CheckDungeonResponse checkDungeonResponse = new CheckDungeonResponse();
                checkDungeonResponse.setDungeon_id(dungeonTable.getId());
                checkDungeonResponse.setTitle(dungeonTable.getTitle());
                checkDungeonResponse.setCoin(dungeonTable.getCoin());
                checkDungeonResponse.setUserCount(
                        rUserDungeonTableMapper.selectCount(
                                Wrappers.lambdaQuery(RUserDungeonTable.class)
                                        .eq(RUserDungeonTable::getDungeonId,dungeonTable.getId())
                        )
                );
                checkDungeonResponseList.add(checkDungeonResponse);
            }
            checkDungeonListResponse.setCheckDungeonRequestList(checkDungeonResponseList);
            checkDungeonListResponse.setCount(checkDungeonResponseList.size());
        }
        return checkDungeonListResponse;
    }

    @Override
    public void deleteDungeon(DeleteDungeonRequest deleteDungeonRequest) {
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,deleteDungeonRequest.getAdminId())
        );
        if(admin == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            DungeonTable dungeonTable = null;
            dungeonTable = dungeonTableMapper.selectOne(
                    Wrappers.lambdaQuery(DungeonTable.class)
                            .eq(DungeonTable::getId,deleteDungeonRequest.getDungeonId())
            );
            if(dungeonTable == null){
                throw new CustomException(ErrorEnum.NOT_FOUND_OBJECT.getCode(),"副本不存在");
            }else {
                dungeonTableMapper.deleteById(dungeonTable);
            }
        }
    }
}
