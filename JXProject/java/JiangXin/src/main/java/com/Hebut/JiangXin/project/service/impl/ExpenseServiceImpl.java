package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Expense;
import com.Hebut.JiangXin.project.entity.Project;
import com.Hebut.JiangXin.project.entity.ProjectUser;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.ExpenseMapper;
import com.Hebut.JiangXin.project.mapper.ProjectMapper;
import com.Hebut.JiangXin.project.mapper.ProjectUserMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IExpenseService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 报销表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-31
 */
@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense> implements IExpenseService {

    @Resource
    ExpenseMapper expenseMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;

    @Override
    public void addExpense(AddExpenseRequest addExpenseRequest) {
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, addExpenseRequest.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
        }
        if (project.getRemainingAmount().compareTo(addExpenseRequest.getAmount()) < 0) {
            throw new CustomException(ErrorEnum.NUMBER_OVERFLOW.getCode(), "您的剩余金额已不够报销，请联系您的指导老师。");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addExpenseRequest.getApplicantId())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "提交人不存在");
        }
        if (addExpenseRequest.getCertificate() == null) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "必须上交凭证");
        }
        if (addExpenseRequest.getAmount().intValue() <= 0) {
            throw new CustomException(ErrorEnum.NUMBER_OVERFLOW.getCode(), "请输入正确的报销金额");
        }
        Expense expense = new Expense();
        String expenseId = "expense-" + UUID.randomUUID().toString().replace("\\-", "");
        expense.setExpenseId(expenseId);
        expense.setIsPass(ExpenseEnum.WAIT_AUDIT.getCode());
        expense.setApplicantId(addExpenseRequest.getApplicantId());
        expense.setProjectId(addExpenseRequest.getProjectId());
        expense.setAmount(addExpenseRequest.getAmount());
        expense.setIntroduction(addExpenseRequest.getExpenseIntroduction());
        expense.setCertificate(addExpenseRequest.getCertificate());
        expenseMapper.insert(expense);
    }


    @Override
    public PageUtil<CheckExpenseResponse> checkExpense(CheckExpenseRequest checkExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkExpenseRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (checkExpenseRequest.getPageRequest().getPageSize() < 1 || checkExpenseRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getApplicantId, checkExpenseRequest.getCommonRequest().getUserId())
        );
        List<CheckExpenseResponse> checkExpenseResponseList = new ArrayList<>();
        for (Expense expense : expenseList) {
            CheckExpenseResponse checkExpenseResponse = new CheckExpenseResponse();
            checkExpenseResponse.setExpenseId(expense.getExpenseId());
            checkExpenseResponse.setExpenseTime(dateTimeFormatter.format(expense.getUpdateTime()));
            checkExpenseResponse.setExpenseIntroduction(expense.getIntroduction());
            checkExpenseResponse.setCertificate(expense.getCertificate());
            checkExpenseResponse.setAmount(expense.getAmount());
            String expenseIsPass;
            switch (expense.getIsPass()) {
                case "0":
                    expenseIsPass = "待审核";
                    break;
                case "1":
                    expenseIsPass = "审核中";
                    break;
                case "2":
                    expenseIsPass = "未通过";
                    break;
                case "3":
                    expenseIsPass = "已通过";
                    break;
                default:
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "状态码出错，请联系管理员");
            }
            checkExpenseResponse.setExpenseStage(expenseIsPass);
            checkExpenseResponse.setExpenseIsPass(expense.getIsPass());
            checkExpenseResponseList.add(checkExpenseResponse);
        }
        checkExpenseResponseList.sort(CheckExpenseResponse.expensePass);
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = new PageUtil<>(checkExpenseRequest.getPageRequest().getCurrentPage(), checkExpenseRequest.getPageRequest().getPageSize(), checkExpenseResponseList);
        return checkExpenseResponsePage;

    }

    @Override
    public void cancelExpense(CancelExpenseRequest cancelExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, cancelExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, cancelExpenseRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        Expense expense = expenseMapper.selectOne(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getApplicantId, cancelExpenseRequest.getCommonRequest().getUserId())
                        .eq(Expense::getExpenseId, cancelExpenseRequest.getExpenseId())
                        .between(Expense::getIsPass, ExpenseEnum.WAIT_AUDIT.getCode(), ExpenseEnum.AUDITING.getCode())
        );
        if (expense == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "没有撤销权利或没有该报销");
        }
        File file = new File(PathEnum.FilePath.getMsg() + "img/certificate/" + expense.getCertificate());
        if (file.exists()) {
            file.delete();
            expenseMapper.deleteById(expense);
        } else {
            throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败" + file.getAbsolutePath());
        }
    }

    @Override
    public InquireExpenseResponse inquireExpense(CommonRequest commonRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "验证错误");
        }
        List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, commonRequest.getUserId())
                        .le(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
        );
        Project project = new Project();
        for (ProjectUser ProjectUser : ProjectUserList) {
            project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, ProjectUser.getProjectId())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
            );
            if (project != null) {
                break;
            }
        }
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getProjectId, project.getProjectId())
                        .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
        );
        InquireExpenseResponse inquireExpenseResponse = new InquireExpenseResponse();
        inquireExpenseResponse.setRemainAmount(project.getRemainingAmount());
        BigDecimal expenseAmount = new BigDecimal(0);
        for (Expense expense : expenseList) {
            expenseAmount = expenseAmount.add(expense.getAmount());
        }
        inquireExpenseResponse.setExpenseAmount(expenseAmount);
        return inquireExpenseResponse;
    }

    @Override
    public PageUtil<CheckExpenseResponse> searchExpense(SearchExpenseRequest searchExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, searchExpenseRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (searchExpenseRequest.getPageRequest().getPageSize() < 1 || searchExpenseRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getApplicantId, searchExpenseRequest.getCommonRequest().getUserId())
                        .like(Expense::getIntroduction, searchExpenseRequest.getExpense())
        );
        List<CheckExpenseResponse> checkExpenseResponseList = new ArrayList<>();
        for (Expense expense : expenseList) {
            if (!"".equals(searchExpenseRequest.getYear()) && expense.getUpdateTime().getYear() != Integer.parseInt(searchExpenseRequest.getYear())) {
                continue;
            }
            if (!"".equals(searchExpenseRequest.getMonth()) && expense.getUpdateTime().getMonthValue() != Integer.parseInt(searchExpenseRequest.getMonth())) {
                continue;
            }
            if (!"".equals(searchExpenseRequest.getDay()) && expense.getUpdateTime().getDayOfMonth() != Integer.parseInt(searchExpenseRequest.getDay())) {
                continue;
            }
            CheckExpenseResponse checkExpenseResponse = new CheckExpenseResponse();
            checkExpenseResponse.setExpenseId(expense.getExpenseId());
            checkExpenseResponse.setExpenseTime(dateTimeFormatter.format(expense.getUpdateTime()));
            checkExpenseResponse.setExpenseIntroduction(expense.getIntroduction());
            checkExpenseResponse.setCertificate(expense.getCertificate());
            checkExpenseResponse.setAmount(expense.getAmount());
            String expenseIsPass;
            switch (expense.getIsPass()) {
                case "0":
                    expenseIsPass = "待审核";
                    break;
                case "1":
                    expenseIsPass = "审核中";
                    break;
                case "2":
                    expenseIsPass = "未通过";
                    break;
                case "3":
                    expenseIsPass = "已通过";
                    break;
                default:
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "状态码出错，请联系管理员");
            }
            checkExpenseResponse.setExpenseStage(expenseIsPass);
            checkExpenseResponse.setExpenseIsPass(expense.getIsPass());
            checkExpenseResponseList.add(checkExpenseResponse);
        }
        checkExpenseResponseList.sort(CheckExpenseResponse.expensePass);
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = new PageUtil<>(searchExpenseRequest.getPageRequest().getCurrentPage(), searchExpenseRequest.getPageRequest().getPageSize(), checkExpenseResponseList);
        return checkExpenseResponsePage;
    }

    @Override
    public DownloadResponse downloadExpense(DownloadExpenseRequest downloadExpenseRequest) {
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/certificate/");
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        Expense expense = expenseMapper.selectOne(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getExpenseId, downloadExpenseRequest.getExpenseId())
        );
        if (expense == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到数据库数据");
        }
        downloadResponse.setFileName(expense.getCertificate());
        return downloadResponse;
    }

    @Override
    public PageUtil<ScanExpenseResponse> scanExpense(SearchAuditExpenseRequest searchAuditExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchAuditExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, searchAuditExpenseRequest.getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (searchAuditExpenseRequest.getPageRequest().getPageSize() < 1 || searchAuditExpenseRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getProjectId, searchAuditExpenseRequest.getProjectId())
        );
        List<ScanExpenseResponse> scanExpenseResponseList = new ArrayList<>();
        for (Expense expense : expenseList) {
            ScanExpenseResponse scanExpenseResponse = new ScanExpenseResponse();
            scanExpenseResponse.setExpenseId(expense.getExpenseId());
            scanExpenseResponse.setExpenseTime(dateTimeFormatter.format(expense.getUpdateTime()));
            scanExpenseResponse.setExpenseIntroduction(expense.getIntroduction());
            scanExpenseResponse.setCertificate(expense.getCertificate());
            scanExpenseResponse.setAmount(expense.getAmount());
            scanExpenseResponse.setProjectId(expense.getProjectId());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, expense.getProjectId())
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            scanExpenseResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            scanExpenseResponse.setProjectName(project.getProjectName().substring(index + 1));
            String expenseIsPass;
            switch (expense.getIsPass()) {
                case "0":
                    expenseIsPass = "待审核";
                    break;
                case "1":
                    expenseIsPass = "审核中";
                    break;
                case "2":
                    expenseIsPass = "未通过";
                    break;
                case "3":
                    expenseIsPass = "已通过";
                    break;
                default:
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "状态码出错，请联系管理员");
            }
            scanExpenseResponse.setStageId(expense.getIsPass());
            scanExpenseResponse.setStage(expenseIsPass);
            scanExpenseResponseList.add(scanExpenseResponse);
        }
        scanExpenseResponseList.sort(ScanExpenseResponse.expensePass);
        PageUtil<ScanExpenseResponse> scanExpenseResponsePage = new PageUtil<>(searchAuditExpenseRequest.getPageRequest().getCurrentPage(), searchAuditExpenseRequest.getPageRequest().getPageSize(), scanExpenseResponseList);

        return scanExpenseResponsePage;
    }

    @Override
    public void auditExpense(AuditExpenseRequest auditExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditExpenseRequest.getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您没有权限");
        }
        Expense expense = expenseMapper.selectOne(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getExpenseId, auditExpenseRequest.getExpenseId())
        );
        if (expense == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "报销不存在");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, expense.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "数据库错误");
        }
        if (auditExpenseRequest.getIsPass().equals(PassEnum.REFUSE.getCode())) {
            expense.setIsPass(ExpenseEnum.REFUSE.getCode());
            expenseMapper.updateById(expense);
        } else if (auditExpenseRequest.getIsPass().equals(PassEnum.PASS.getCode())) {
            if (project.getRemainingAmount().compareTo(expense.getAmount()) < 0) {
                throw new CustomException(ErrorEnum.NUMBER_OVERFLOW.getCode(), "项目剩余金额不够，无法同意报销");
            }
            expense.setIsPass(ExpenseEnum.PASSING.getCode());
            BigDecimal decimal;
            decimal = project.getRemainingAmount().subtract(expense.getAmount());
            project.setRemainingAmount(decimal);
            expenseMapper.updateById(expense);
            projectMapper.updateById(project);
        } else {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "状态码错误");
        }
    }

    @Override
    public PageUtil<SearchAuditExpenseResponse> checkAuditExpense(CheckRequest checkRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkRequest.getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (checkRequest.getPageRequest().getPageSize() < 1 || checkRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .between(Expense::getIsPass, ExpenseEnum.WAIT_AUDIT.getCode(), ExpenseEnum.AUDITING.getCode())
        );
        List<SearchAuditExpenseResponse> searchAuditExpenseResponseList = new ArrayList<>();
        for (Expense expense : expenseList) {
            SearchAuditExpenseResponse searchAuditExpenseResponse = new SearchAuditExpenseResponse();
            searchAuditExpenseResponse.setExpenseId(expense.getExpenseId());
            searchAuditExpenseResponse.setExpenseTime(dateTimeFormatter.format(expense.getUpdateTime()));
            searchAuditExpenseResponse.setExpenseIntroduction(expense.getIntroduction());
            searchAuditExpenseResponse.setCertificate(expense.getCertificate());
            searchAuditExpenseResponse.setAmount(expense.getAmount());
            searchAuditExpenseResponse.setProjectId(expense.getProjectId());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, expense.getProjectId())
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            searchAuditExpenseResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            searchAuditExpenseResponse.setProjectName(project.getProjectName().substring(index + 1));
            searchAuditExpenseResponseList.add(searchAuditExpenseResponse);
        }
        searchAuditExpenseResponseList.sort(SearchAuditExpenseResponse.expensePass);
        PageUtil<SearchAuditExpenseResponse> searchAuditExpenseResponsePage = new PageUtil<>(checkRequest.getPageRequest().getCurrentPage(), checkRequest.getPageRequest().getPageSize(), searchAuditExpenseResponseList);

        List<Expense> expenseLists = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getIsPass, ExpenseEnum.WAIT_AUDIT.getCode())
        );
        for (Expense expense : expenseLists) {
            expense.setIsPass(ExpenseEnum.AUDITING.getCode());
            expenseMapper.updateById(expense);
        }
        return searchAuditExpenseResponsePage;
    }

    @Override
    public InquireExpenseResponse inquireUserExpense(InquireUserExpenseRequest inquireUserExpenseRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, inquireUserExpenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, inquireUserExpenseRequest.getCommonRequest().getToken())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "验证错误");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, inquireUserExpenseRequest.getProjectId())
                        .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
        );
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getProjectId, project.getProjectId())
                        .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
        );
        InquireExpenseResponse inquireExpenseResponse = new InquireExpenseResponse();
        inquireExpenseResponse.setRemainAmount(project.getRemainingAmount());
        BigDecimal expenseAmount = new BigDecimal(0);
        for (Expense expense : expenseList) {
            expenseAmount = expenseAmount.add(expense.getAmount());
        }
        inquireExpenseResponse.setExpenseAmount(expenseAmount);
        return inquireExpenseResponse;
    }

    @Override
    public PageUtil<CheckExpenseResponse> searchProjectExpense(SearchProjectExpenseRequest searchProjectExpenseRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchProjectExpenseRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchProjectExpenseRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (searchProjectExpenseRequest.getCheckRequest().getPageRequest().getPageSize() < 1 || searchProjectExpenseRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Expense> expenseList = expenseMapper.selectList(
                Wrappers.lambdaQuery(Expense.class)
                        .eq(Expense::getProjectId, searchProjectExpenseRequest.getProjectId())
                        .like(Expense::getIntroduction, searchProjectExpenseRequest.getExpense())
        );
        List<CheckExpenseResponse> checkExpenseResponseList = new ArrayList<>();
        for (Expense expense : expenseList) {
            if (!"".equals(searchProjectExpenseRequest.getYear()) && expense.getUpdateTime().getYear() != Integer.parseInt(searchProjectExpenseRequest.getYear())) {
                continue;
            }
            if (!"".equals(searchProjectExpenseRequest.getMonth()) && expense.getUpdateTime().getMonthValue() != Integer.parseInt(searchProjectExpenseRequest.getMonth())) {
                continue;
            }
            if (!"".equals(searchProjectExpenseRequest.getDay()) && expense.getUpdateTime().getDayOfMonth() != Integer.parseInt(searchProjectExpenseRequest.getDay())) {
                continue;
            }
            CheckExpenseResponse checkExpenseResponse = new CheckExpenseResponse();
            checkExpenseResponse.setExpenseId(expense.getExpenseId());
            checkExpenseResponse.setExpenseTime(dateTimeFormatter.format(expense.getUpdateTime()));
            checkExpenseResponse.setExpenseIntroduction(expense.getIntroduction());
            checkExpenseResponse.setCertificate(expense.getCertificate());
            checkExpenseResponse.setAmount(expense.getAmount());
            String expenseIsPass;
            switch (expense.getIsPass()) {
                case "0":
                    expenseIsPass = "待审核";
                    break;
                case "1":
                    expenseIsPass = "审核中";
                    break;
                case "2":
                    expenseIsPass = "未通过";
                    break;
                case "3":
                    expenseIsPass = "已通过";
                    break;
                default:
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "状态码出错，请联系管理员");
            }
            checkExpenseResponse.setExpenseStage(expenseIsPass);
            checkExpenseResponse.setExpenseIsPass(expense.getIsPass());
            checkExpenseResponseList.add(checkExpenseResponse);
        }
        checkExpenseResponseList.sort(CheckExpenseResponse.expensePass);
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = new PageUtil<>(searchProjectExpenseRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchProjectExpenseRequest.getCheckRequest().getPageRequest().getPageSize(), checkExpenseResponseList);
        return checkExpenseResponsePage;
    }
}
