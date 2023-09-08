package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.AdminTable;
import com.android.quest.project.entity.NoteTable;
import com.android.quest.project.entity.request.AddNoteRequest;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckNoteListResponse;
import com.android.quest.project.mapper.AdminTableMapper;
import com.android.quest.project.mapper.NoteTableMapper;
import com.android.quest.project.service.INoteTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class NoteTableServiceImpl extends ServiceImpl<NoteTableMapper, NoteTable> implements INoteTableService {

    @Resource
    AdminTableMapper adminTableMapper;
    @Resource
    NoteTableMapper noteTableMapper;
    @Override
    public CheckNoteListResponse checkNoteList(CheckRequest checkRequest) {
        CheckNoteListResponse checkNoteListResponse = new CheckNoteListResponse();
        AdminTable adminTable = null;
        adminTable = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,checkRequest.getUser_id())
        );
        if(adminTable == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            List<NoteTable> noteTableList = noteTableMapper.selectList(
                    Wrappers.lambdaQuery(NoteTable.class)
            );
            checkNoteListResponse.setNoteTableList(noteTableList);
        }
        return checkNoteListResponse;
    }

    @Override
    public void addNote(AddNoteRequest addNoteRequest) {
        AdminTable adminTable = null;

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);

        adminTable = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,addNoteRequest.getAdminId())
        );
        if(adminTable == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            NoteTable noteTable = new NoteTable();
            noteTable.setTitle(addNoteRequest.getTitle());
            noteTable.setArticle(addNoteRequest.getArticle());
            noteTable.setCreateTime(localTime);
            noteTableMapper.insert(noteTable);
        }
    }
}
