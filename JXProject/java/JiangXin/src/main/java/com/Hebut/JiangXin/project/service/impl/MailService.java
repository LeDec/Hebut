package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.MD5Utils;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.ResetPasswordRequest;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Lenovo
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Resource
    UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String from;

    private String code;

    /**
     * @param to      接收者
     * @param subject 主题
     * @Value注解读取配置文件中同名的配置值
     */
    public String sendSimpleMail(String to, String subject) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, to)
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "学号不存在");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        code = getRandom();
        message.setTo(user.getEMail());
        message.setSubject(subject);
        message.setText("您的验证码是：" + code + ", 请注意保管好您的验证码。");
        message.setFrom(from);
        mailSender.send(message);
        return code;
    }

    public static String getRandom() {
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            code = code + r;
        }
        return code;
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, resetPasswordRequest.getUserId())
        );
        if (resetPasswordRequest.getCode().equals(code)) {
            user.setUserPasswords(MD5Utils.inputPassToFormPass("hebut" + user.getUserId()));
            userMapper.updateById(user);
        } else {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证码错误");
        }
    }
}
