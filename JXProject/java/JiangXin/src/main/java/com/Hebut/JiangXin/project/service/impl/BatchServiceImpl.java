package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckBatchResponse;
import com.Hebut.JiangXin.project.entity.response.CheckTimeResponse;
import com.Hebut.JiangXin.project.entity.response.NowBatchResponse;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IBatchService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 批次表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
@Service
public class BatchServiceImpl extends ServiceImpl<BatchMapper, Batch> implements IBatchService {

    @Resource
    BatchMapper batchMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    InformMapper informMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ExpenseMapper expenseMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public void addBatch(AddBatchRequest addBatchRequest) {
        addBatchRequest.timeLogic();
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addBatchRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, addBatchRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Batch nowBatch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .gt(Batch::getDefendDeadline, LocalDateTime.now())
        );
        if (nowBatch != null) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "当前阶段还未结束");
        }
        Batch batch = new Batch();
        String batchId = "batch-" + UUID.randomUUID().toString().replace("\\-", "");
        batch.setBatchId(batchId);
        batch.setBatchName(addBatchRequest.getBatchName());
        batch.setBatchTheme(addBatchRequest.getBatchTheme());
        batch.setSelectBeginning(addBatchRequest.getSelectBeginning());
        batch.setEnrollBeginning(addBatchRequest.getEnrollBeginning());
        batch.setMediumBeginning(addBatchRequest.getMediumBeginning());
        batch.setDefendBeginning(addBatchRequest.getDefendBeginning());
        batch.setSelectDeadline(addBatchRequest.getSelectDeadline());
        batch.setEnrollDeadline(addBatchRequest.getEnrollDeadline());
        batch.setMediumDeadline(addBatchRequest.getMediumDeadline());
        batch.setDefendDeadline(addBatchRequest.getDefendDeadline());
        batchMapper.insert(batch);
    }

    @Override
    public PageUtil<CheckBatchResponse> checkBatch(CheckBatchRequest checkBatchRequest) {
        if (checkBatchRequest.getPageRequest().getCurrentPage() < 1 || checkBatchRequest.getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkBatchRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkBatchRequest.getCommonRequest().getToken())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您没有该权限！");
        }
        List<CheckBatchResponse> checkBatchResponseList = new ArrayList<>();
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        for (Batch batch : batchList) {
            CheckBatchResponse checkBatchResponse = new CheckBatchResponse();
            checkBatchResponse.setBatchId(batch.getBatchId());
            checkBatchResponse.setBatchName(batch.getBatchName());
            String stage;
            if (batch.getSelectBeginning().isAfter(LocalDateTime.now())) {
                stage = "匠心班未开启";
            } else if (batch.getSelectBeginning().isBefore(LocalDateTime.now()) && batch.getEnrollBeginning().isAfter(LocalDateTime.now())) {
                stage = "征集阶段";
            } else if (batch.getEnrollBeginning().isBefore(LocalDateTime.now()) && batch.getMediumBeginning().isAfter(LocalDateTime.now())) {
                stage = "报名阶段";
            } else if (batch.getMediumBeginning().isBefore(LocalDateTime.now()) && batch.getDefendBeginning().isAfter(LocalDateTime.now())) {
                stage = "项目进行中期阶段";
            } else if (batch.getDefendBeginning().isBefore(LocalDateTime.now()) && batch.getDefendDeadline().isAfter(LocalDateTime.now())) {
                stage = "项目进行答辩阶段";
            } else {
                stage = "结束阶段";
            }
            checkBatchResponse.setBatchStage(stage);
            checkBatchResponse.setSelectBeginning(batch.getSelectBeginning());
            checkBatchResponse.setSelectDeadline(batch.getSelectDeadline());
            checkBatchResponse.setEnrollBeginning(batch.getEnrollBeginning());
            checkBatchResponse.setEnrollDeadline(batch.getEnrollDeadline());
            checkBatchResponse.setMediumBeginning(batch.getMediumBeginning());
            checkBatchResponse.setMediumDeadline(batch.getMediumDeadline());
            checkBatchResponse.setDefendBeginning(batch.getDefendBeginning());
            checkBatchResponse.setDefendDeadline(batch.getDefendDeadline());
            List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, batch.getBatchId())
            );
            BigDecimal amount = new BigDecimal(0);
            for (BatchProject batchProject : batchProjectList) {
                List<Expense> expenses = expenseMapper.selectList(
                        Wrappers.lambdaQuery(Expense.class)
                                .eq(Expense::getProjectId, batchProject.getProjectId())
                                .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
                );
                for (Expense expense : expenses) {
                    amount = amount.add(expense.getAmount());
                }
            }
            checkBatchResponse.setAmount(amount);
            checkBatchResponseList.add(checkBatchResponse);
        }
        checkBatchResponseList.sort(CheckBatchResponse.batchTime);
        return new PageUtil<>(checkBatchRequest.getPageRequest().getCurrentPage(), checkBatchRequest.getPageRequest().getPageSize(), checkBatchResponseList);
    }

    @Override
    public void pushNotification(PushNotificationRequest pushNotificationRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, pushNotificationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, pushNotificationRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, pushNotificationRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该批次");
        }
        if (batch.getSelectBeginning() == null || batch.getSelectDeadline() == null || batch.getEnrollBeginning() == null || batch.getEnrollDeadline() == null || batch.getMediumBeginning() == null || batch.getMediumDeadline() == null || batch.getDefendBeginning() == null || batch.getDefendDeadline() == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "阶段时间尚未设置完成");
        }
        if (batch.getDefendDeadline().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "");
        }
        String informId;
        for (String projectId : pushNotificationRequest.getProjectIdList()) {
            ProjectUser projectUser = ProjectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectId)
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.APPLICANT.getCode())
            );
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectId)
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            String projectSymbol = project.getProjectName().substring(0, index);
            String projectName = project.getProjectName().substring(index + 1);
            project.setStage(ProjectStageEnum.PUSH.getCode());
            projectMapper.updateById(project);
            Inform inform = new Inform();
            informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            inform.setInformId(informId);
            inform.setAcceptorId(projectUser.getUserId());
            inform.setSenderId(pushNotificationRequest.getCommonRequest().getUserId());
            inform.setTitle(batch.getBatchName() + "项目复试情况");
            inform.setInformation("您的项目：" + projectSymbol + "-" + projectName + "项目初审已通过。");
            informMapper.insert(inform);
        }
        List<User> studentList = userMapper.selectList(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, UserTypeEnum.STUDENT.getCode())
        );
        for (User student : studentList) {
            Inform inform = new Inform();
            informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            inform.setInformId(informId);
            inform.setAcceptorId(student.getUserId());
            inform.setSenderId(pushNotificationRequest.getCommonRequest().getUserId());
            inform.setTitle(batch.getBatchName() + "开启通知");
            inform.setInformation(batch.getBatchName() + "已经开启，" + "请各位同学查看项目后选择报名。");
            informMapper.insert(inform);
        }
    }

    @Override
    public void setTime(SetBatchTimeRequest setBatchTimeRequest) {
        setBatchTimeRequest.timeLogic();
        AddBatchRequest addBatchRequest = new AddBatchRequest();
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, setBatchTimeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, setBatchTimeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, setBatchTimeRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次没找到");
        }
        switch (setBatchTimeRequest.getBatchStage()) {
            case "1":
                batch.setSelectBeginning(setBatchTimeRequest.getBeginning());
                batch.setSelectDeadline(setBatchTimeRequest.getDeadline());
                break;
            case "2":
                batch.setEnrollBeginning(setBatchTimeRequest.getBeginning());
                batch.setEnrollDeadline(setBatchTimeRequest.getDeadline());
                break;
            case "3":
                batch.setMediumBeginning(setBatchTimeRequest.getBeginning());
                batch.setMediumDeadline(setBatchTimeRequest.getDeadline());
                break;
            case "4":
                batch.setDefendBeginning(setBatchTimeRequest.getBeginning());
                batch.setDefendDeadline(setBatchTimeRequest.getDeadline());
                break;
            default:
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "状态码错误");
        }
        batchMapper.updateById(batch);
        addBatchRequest.setSelectBeginning(batch.getSelectBeginning());
        addBatchRequest.setSelectDeadline(batch.getSelectDeadline());
        addBatchRequest.setEnrollBeginning(batch.getEnrollBeginning());
        addBatchRequest.setEnrollDeadline(batch.getEnrollDeadline());
        addBatchRequest.setMediumBeginning(batch.getMediumBeginning());
        addBatchRequest.setMediumDeadline(batch.getMediumDeadline());
        addBatchRequest.setDefendBeginning(batch.getDefendBeginning());
        addBatchRequest.setDefendDeadline(batch.getDefendDeadline());
        addBatchRequest.timeLogic();

    }

    @Override
    public NowBatchResponse nowBatch(NowBatchRequest nowBatchRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, nowBatchRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, nowBatchRequest.getCommonRequest().getToken())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        if (batchList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂无批次");
        }
        Batch batch = batchList.get(0);
        for (int i = 1; i < batchList.size(); i++) {
            if (batchList.get(i).getDefendDeadline().isAfter(batchList.get(i - 1).getDefendDeadline())) {
                batch = batchList.get(i);
            }
        }
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有进行中的批次");
        }
        NowBatchResponse nowBatchResponse = new NowBatchResponse();
        if (batch.getSelectBeginning().isAfter(LocalDateTime.now())) {
            nowBatchResponse.setBatchStage(BatchStageEnum.NONE.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.NONE.getCode());
        } else if (batch.getSelectBeginning().isBefore(LocalDateTime.now()) && batch.getEnrollBeginning().isAfter(LocalDateTime.now())) {
            nowBatchResponse.setBatchStage(BatchStageEnum.COLLECTING.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.COLLECTING.getCode());
        } else if (batch.getEnrollBeginning().isBefore(LocalDateTime.now()) && batch.getMediumBeginning().isAfter(LocalDateTime.now())) {
            nowBatchResponse.setBatchStage(BatchStageEnum.ENROLL.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.ENROLL.getCode());
        } else if (batch.getMediumBeginning().isBefore(LocalDateTime.now()) && batch.getDefendBeginning().isAfter(LocalDateTime.now())) {
            nowBatchResponse.setBatchStage(BatchStageEnum.MEDIUM.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.MEDIUM.getCode());
        } else if (batch.getDefendBeginning().isBefore(LocalDateTime.now()) && batch.getDefendDeadline().isAfter(LocalDateTime.now())) {
            nowBatchResponse.setBatchStage(BatchStageEnum.DEFENSE.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.DEFENSE.getCode());
        } else {
            nowBatchResponse.setBatchStage(BatchStageEnum.FINAL.getMsg());
            nowBatchResponse.setBatchStageId(BatchStageEnum.FINAL.getCode());
        }
        nowBatchResponse.setBatchId(batch.getBatchId());
        nowBatchResponse.setBatchName(batch.getBatchName());
        nowBatchResponse.setBatchTheme(batch.getBatchTheme());
        return nowBatchResponse;
    }

    @Override
    public CheckTimeResponse checkTime(CheckTimeRequest checkTimeRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkTimeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkTimeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkTimeRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CheckTimeResponse checkTimeResponse = new CheckTimeResponse();
        checkTimeResponse.setSelectBeginning(dateTimeFormatter.format(batch.getSelectBeginning()));
        checkTimeResponse.setEnrollBeginning(dateTimeFormatter.format(batch.getEnrollBeginning()));
        checkTimeResponse.setMediumBeginning(dateTimeFormatter.format(batch.getMediumBeginning()));
        checkTimeResponse.setDefendBeginning(dateTimeFormatter.format(batch.getDefendBeginning()));
        checkTimeResponse.setSelectDeadline(dateTimeFormatter.format(batch.getSelectDeadline()));
        checkTimeResponse.setEnrollDeadline(dateTimeFormatter.format(batch.getEnrollDeadline()));
        checkTimeResponse.setMediumDeadline(dateTimeFormatter.format(batch.getMediumDeadline()));
        checkTimeResponse.setDefendDeadline(dateTimeFormatter.format(batch.getDefendDeadline()));
        return checkTimeResponse;
    }
}
