package com.android.quest.project.service;

import com.android.quest.project.entity.NoteTable;
import com.android.quest.project.entity.request.AddNoteRequest;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckNoteListResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface INoteTableService extends IService<NoteTable> {

    CheckNoteListResponse checkNoteList(CheckRequest checkRequest);

    void addNote(AddNoteRequest addNoteRequest);
}
