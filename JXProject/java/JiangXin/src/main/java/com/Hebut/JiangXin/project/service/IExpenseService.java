package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Expense;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报销表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-31
 */
public interface IExpenseService extends IService<Expense> {

    /**
     * 增加关系
     *
     * @param addExpenseRequest
     */
    void addExpense(AddExpenseRequest addExpenseRequest);

    /**
     * 查询报销
     *
     * @param checkExpenseRequest
     * @return 报销
     */
    PageUtil<CheckExpenseResponse> checkExpense(CheckExpenseRequest checkExpenseRequest);

    /**
     * 撤销报销
     *
     * @param cancelExpenseRequest
     */
    void cancelExpense(CancelExpenseRequest cancelExpenseRequest);

    /**
     * 查询报销
     *
     * @param commonRequest
     * @return
     */
    InquireExpenseResponse inquireExpense(CommonRequest commonRequest);

    /**
     * 搜索报销
     *
     * @param searchExpenseRequest
     * @return
     */
    PageUtil<CheckExpenseResponse> searchExpense(SearchExpenseRequest searchExpenseRequest);

    /**
     * 下载报销凭证
     *
     * @param downloadExpenseRequest
     * @return
     */
    DownloadResponse downloadExpense(DownloadExpenseRequest downloadExpenseRequest);

    /**
     * 查看审核报销表
     *
     * @param searchAuditExpenseRequest
     * @return
     */
    PageUtil<ScanExpenseResponse> scanExpense(SearchAuditExpenseRequest searchAuditExpenseRequest);

    /**
     * 审核报销
     *
     * @param auditExpenseRequest
     */
    void auditExpense(AuditExpenseRequest auditExpenseRequest);

    /**
     * 查看总审核报销表
     *
     * @param checkRequest
     * @return
     */
    PageUtil<SearchAuditExpenseResponse> checkAuditExpense(CheckRequest checkRequest);

    /**
     * 查询用户报销金额
     *
     * @param inquireUserExpenseRequest
     * @return
     */
    InquireExpenseResponse inquireUserExpense(InquireUserExpenseRequest inquireUserExpenseRequest);

    /**
     * 搜索项目报销
     *
     * @param searchProjectExpenseRequest
     * @return
     */
    PageUtil<CheckExpenseResponse> searchProjectExpense(SearchProjectExpenseRequest searchProjectExpenseRequest);
}
