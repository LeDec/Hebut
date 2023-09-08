package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.IsReadEnum;
import com.Hebut.JiangXin.common.Enum.UserTypeEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.project.entity.Inform;
import com.Hebut.JiangXin.project.entity.InformAttachment;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckInformPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckInformResponse;
import com.Hebut.JiangXin.project.entity.response.ScanInformResponse;
import com.Hebut.JiangXin.project.mapper.InformAttachmentMapper;
import com.Hebut.JiangXin.project.mapper.InformMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IInformService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-01
 */
@Service
public class InformServiceImpl extends ServiceImpl<InformMapper, Inform> implements IInformService {

    @Resource
    InformMapper informMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    InformAttachmentMapper informAttachmentMapper;

    @Override
    public void addInform(AddInformRequest addInformRequest) {
        User sender = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addInformRequest.getSenderId())
        );
        if (sender == null) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "用户不存在");
        }
        if (addInformRequest.getInformation() == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "通知不能为空");
        }
        for (String accept : addInformRequest.getAcceptorId()) {
            User acceptor = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, accept)
            );
            if (acceptor == null) {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "用户不存在");
            }
            Inform inform = new Inform();
            String informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            inform.setInformId(informId);
            inform.setTitle(addInformRequest.getTitle());
            inform.setSenderId(addInformRequest.getSenderId());
            inform.setInformation(addInformRequest.getInformation());
            inform.setAcceptorId(accept);
            informMapper.insert(inform);
            for (String attachment : addInformRequest.getAttachment()) {
                InformAttachment informAttachment = new InformAttachment();
                String attach = "attach-" + UUID.randomUUID().toString().replace("\\-", "");
                informAttachment.setRelationId(attach);
                informAttachment.setInformId(informId);
                informAttachment.setAttachment(attachment);
                informAttachmentMapper.insert(informAttachment);
            }
        }
    }

    @Override
    public Page<CheckInformResponse> checkInform(CheckInformRequest checkInformRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkInformRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkInformRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (checkInformRequest.getPageRequest().getPageSize() < 1 || checkInformRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Page<CheckInformResponse> checkInformResponsePage = new Page<>();
        Page page = new Page();
        page.setCurrent(checkInformRequest.getPageRequest().getCurrentPage());
        page.setSize(checkInformRequest.getPageRequest().getPageSize());
        page.addOrder(OrderItem.asc("is_Read"), OrderItem.desc("create_Time"));
        Page<Inform> informPage = informMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getAcceptorId, checkInformRequest.getCommonRequest().getUserId())
        );
        checkInformResponsePage.setTotal(page.getTotal());
        List<CheckInformResponse> checkInformResponses = new ArrayList<>();
        for (int i = 0; i < informPage.getRecords().size(); i++) {
            CheckInformResponse checkInformResponse = new CheckInformResponse();
            checkInformResponse.setSender(informPage.getRecords().get(i).getSenderId());
            checkInformResponse.setAcceptor(informPage.getRecords().get(i).getAcceptorId());
            checkInformResponse.setTitle(informPage.getRecords().get(i).getTitle());
            checkInformResponse.setInformTime(dateTimeFormatter.format(informPage.getRecords().get(i).getUpdateTime()));
            checkInformResponse.setInformId(informPage.getRecords().get(i).getInformId());
            if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.UNREAD.getCode()) {
                checkInformResponse.setIsRead(false);
            } else if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.READ.getCode()) {
                checkInformResponse.setIsRead(true);
            } else {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "阅读状态出错，请联系管理员");
            }
            checkInformResponses.add(checkInformResponse);
        }
        checkInformResponsePage.setRecords(checkInformResponses);
        return checkInformResponsePage;
    }

    @Override
    public ScanInformResponse scanInform(ScanInformRequest scanInformRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, scanInformRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, scanInformRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Inform inform = informMapper.selectOne(
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getInformId, scanInformRequest.getInformId())
        );
        if (inform == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "通知不存在");
        }
        ScanInformResponse scanInformResponse = new ScanInformResponse();
        scanInformResponse.setTitle(inform.getTitle());
        scanInformResponse.setInformation(inform.getInformation());
        List<InformAttachment> informAttachmentList = informAttachmentMapper.selectList(
                Wrappers.lambdaQuery(InformAttachment.class)
                        .eq(InformAttachment::getInformId, scanInformRequest.getInformId())
        );
        List<String> attach = new ArrayList<>();
        for (InformAttachment informAttachment : informAttachmentList) {
            attach.add(informAttachment.getAttachment());
        }
        scanInformResponse.setAttachment(attach);
        if (Objects.equals(user.getUserId(), inform.getAcceptorId())) {
            inform.setIsRead(IsReadEnum.READ.getCode());
        }
        informMapper.updateById(inform);
        return scanInformResponse;
    }

    @Override
    public CheckInformPageResponse checkAllInform(CheckRequest checkRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "账号不存在或者在异地登录");
        }
        if (checkRequest.getPageRequest().getPageSize() < 1 || checkRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Page<CheckInformResponse> checkInformResponsePage = new Page<>();
        CheckInformPageResponse checkInformPageResponse = new CheckInformPageResponse();
        Page page = new Page();
        page.setCurrent(checkRequest.getPageRequest().getCurrentPage());
        page.setSize(checkRequest.getPageRequest().getPageSize());
        page.addOrder(OrderItem.asc("sender_Id"), OrderItem.desc("create_Time"));
        Page<Inform> informPage = informMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Inform.class)
        );
        checkInformResponsePage.setTotal(page.getTotal());
        List<CheckInformResponse> checkInformResponses = new ArrayList<>();
        for (int i = 0; i < informPage.getRecords().size(); i++) {
            CheckInformResponse checkInformResponse = new CheckInformResponse();
            checkInformResponse.setSender(informPage.getRecords().get(i).getSenderId());
            checkInformResponse.setAcceptor(informPage.getRecords().get(i).getAcceptorId());
            checkInformResponse.setTitle(informPage.getRecords().get(i).getTitle());
            checkInformResponse.setInformTime(dateTimeFormatter.format(informPage.getRecords().get(i).getUpdateTime()));
            checkInformResponse.setInformId(informPage.getRecords().get(i).getInformId());
            if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.UNREAD.getCode()) {
                checkInformResponse.setIsRead(false);
            } else if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.READ.getCode()) {
                checkInformResponse.setIsRead(true);
            } else {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "阅读状态出错，请联系管理员");
            }
            checkInformResponses.add(checkInformResponse);
        }
        checkInformResponsePage.setRecords(checkInformResponses);
        checkInformPageResponse.setCheckInformResponse(checkInformResponsePage);
        long pages = informPage.getTotal() / informPage.getSize();
        if (informPage.getSize() == 0) {
            pages = 0L;
        }
        if (informPage.getTotal() % informPage.getSize() != 0) {
            pages++;
        }
        checkInformPageResponse.setPages(pages);
        return checkInformPageResponse;
    }

    @Override
    public void setSomeInform(SetSomeInformRequest setSomeInformRequest) {
        User sender = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, setSomeInformRequest.getSenderId())
        );
        if (sender == null) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "用户不存在");
        }
        if (setSomeInformRequest.getInformation() == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "通知不能为空");
        }
        List<User> users = userMapper.selectList(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, setSomeInformRequest.getType())
        );
        for (User acceptor : users) {
            if (acceptor == null) {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "用户不存在");
            }
            Inform inform = new Inform();
            String informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            inform.setInformId(informId);
            inform.setTitle(setSomeInformRequest.getTitle());
            inform.setSenderId(setSomeInformRequest.getSenderId());
            inform.setInformation(setSomeInformRequest.getInformation());
            inform.setAcceptorId(acceptor.getUserId());
            informMapper.insert(inform);
            for (String attachment : setSomeInformRequest.getAttachment()) {
                InformAttachment informAttachment = new InformAttachment();
                String attach = "attach-" + UUID.randomUUID().toString().replace("\\-", "");
                informAttachment.setRelationId(attach);
                informAttachment.setInformId(informId);
                informAttachment.setAttachment(attachment);
                informAttachmentMapper.insert(informAttachment);
            }
        }
    }

    @Override
    public void deleteInform(DeleteInformRequest deleteInformRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteInformRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, deleteInformRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "验证失败");
        }
        informMapper.delete(
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getInformId, deleteInformRequest.getInformId())
        );
    }

    @Override
    public CheckInformPageResponse checkInformOfMine(CheckRequest checkRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        if (checkRequest.getPageRequest().getPageSize() < 1 || checkRequest.getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Page<CheckInformResponse> checkInformResponsePage = new Page<>();
        CheckInformPageResponse checkInformPageResponse = new CheckInformPageResponse();
        Page page = new Page();
        page.setCurrent(checkRequest.getPageRequest().getCurrentPage());
        page.setSize(checkRequest.getPageRequest().getPageSize());
        page.addOrder(OrderItem.desc("create_Time"));
        Page<Inform> informPage = informMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getSenderId, checkRequest.getCommonRequest().getUserId())
        );
        checkInformResponsePage.setTotal(page.getTotal());
        List<CheckInformResponse> checkInformResponses = new ArrayList<>();
        for (int i = 0; i < informPage.getRecords().size(); i++) {
            CheckInformResponse checkInformResponse = new CheckInformResponse();
            checkInformResponse.setSender(informPage.getRecords().get(i).getSenderId());
            checkInformResponse.setAcceptor(informPage.getRecords().get(i).getAcceptorId());
            checkInformResponse.setTitle(informPage.getRecords().get(i).getTitle());
            checkInformResponse.setInformTime(dateTimeFormatter.format(informPage.getRecords().get(i).getUpdateTime()));
            checkInformResponse.setInformId(informPage.getRecords().get(i).getInformId());
            if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.UNREAD.getCode()) {
                checkInformResponse.setIsRead(false);
            } else if (informPage.getRecords().get(i).getIsRead() == IsReadEnum.READ.getCode()) {
                checkInformResponse.setIsRead(true);
            } else {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "阅读状态出错，请联系管理员");
            }
            checkInformResponses.add(checkInformResponse);
        }
        checkInformResponsePage.setRecords(checkInformResponses);
        checkInformPageResponse.setCheckInformResponse(checkInformResponsePage);
        long pages = informPage.getTotal() / informPage.getSize();
        if (informPage.getSize() == 0) {
            pages = 0L;
        }
        if (informPage.getTotal() % informPage.getSize() != 0) {
            pages++;
        }
        checkInformPageResponse.setPages(pages);
        return checkInformPageResponse;
    }

    @Override
    public Integer checkWaitReadCount(CommonRequest commonRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        return informMapper.selectCount(
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getAcceptorId, commonRequest.getUserId())
                        .eq(Inform::getIsRead, IsReadEnum.UNREAD.getCode())
        );
    }

    @Override
    public void oneClickRead(CommonRequest commonRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<Inform> informs = informMapper.selectList(
                Wrappers.lambdaQuery(Inform.class)
                        .eq(Inform::getAcceptorId, commonRequest.getUserId())
                        .eq(Inform::getIsRead, IsReadEnum.UNREAD.getCode())
        );
        for (Inform inform : informs) {
            inform.setIsRead(IsReadEnum.READ.getCode());
            informMapper.updateById(inform);
        }

    }
}
