package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.UserTypeEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.project.entity.Activity;
import com.Hebut.JiangXin.project.entity.ActivityUser;
import com.Hebut.JiangXin.project.entity.Batch;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.AddActivityRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityRequest;
import com.Hebut.JiangXin.project.entity.request.DeleteActivityRequest;
import com.Hebut.JiangXin.project.entity.response.CheckActivityResponse;
import com.Hebut.JiangXin.project.mapper.ActivityMapper;
import com.Hebut.JiangXin.project.mapper.ActivityUserMapper;
import com.Hebut.JiangXin.project.mapper.BatchMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IActivityService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    ActivityMapper activityMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    ActivityUserMapper activityUserMapper;

    @Override
    public void addActivity(AddActivityRequest addActivityRequest) {
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addActivityRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, addActivityRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "班主任验证失败");
        }
        if (Objects.equals(addActivityRequest.getActivityTitle(), "教评活动")) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "如需发布教评活动请到开启教评界面开启");
        }
        Activity activity = new Activity();
        String activityId = "activity-" + UUID.randomUUID().toString().replace("\\-", "");
        activity.setActivityId(activityId);
        activity.setTitle(addActivityRequest.getActivityTitle());
        activity.setIntroduction(addActivityRequest.getActivityIntroduction());
        activity.setBatchId(addActivityRequest.getBatchId());
        activity.setBeginning(addActivityRequest.getBeginning());
        activity.setDeadline(addActivityRequest.getDeadline());
        activityMapper.insert(activity);
    }

    @Override
    public Page<CheckActivityResponse> checkActivity(CheckActivityRequest checkActivityRequest) {
        if (checkActivityRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkActivityRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        Page page = new Page(checkActivityRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkActivityRequest.getCheckRequest().getPageRequest().getPageSize());
        page.addOrder(OrderItem.ascs("beginning"));
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkActivityRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkActivityRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkActivityRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有该批次");
        }
        List<CheckActivityResponse> checkActivityResponseList = new ArrayList<>();
        Page<CheckActivityResponse> checkActivityResponsePage = new Page<>();
        Page<Activity> activityPage = activityMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getBatchId, batch.getBatchId())
        );
        for (Activity activity : activityPage.getRecords()) {
            CheckActivityResponse checkActivityResponse = new CheckActivityResponse();
            checkActivityResponse.setActivityId(activity.getActivityId());
            checkActivityResponse.setBatchId(activity.getBatchId());
            checkActivityResponse.setActivityTitle(activity.getTitle());
            checkActivityResponse.setActivityIntroduction(activity.getIntroduction());
            checkActivityResponse.setBeginning(activity.getBeginning());
            checkActivityResponse.setDeadline(activity.getDeadline());
            checkActivityResponseList.add(checkActivityResponse);
        }
        checkActivityResponsePage.setRecords(checkActivityResponseList);
        BeanUtils.copyProperties(activityPage,checkActivityResponsePage);
        return checkActivityResponsePage;
    }

    @Override
    public void deleteActivity(DeleteActivityRequest deleteActivityRequest) {
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteActivityRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, deleteActivityRequest.getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        activityMapper.delete(
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getActivityId, deleteActivityRequest.getActivityId())
        );
        activityUserMapper.delete(
                Wrappers.lambdaQuery(ActivityUser.class)
                        .eq(ActivityUser::getActivityId, deleteActivityRequest.getActivityId())
        );
    }
}
