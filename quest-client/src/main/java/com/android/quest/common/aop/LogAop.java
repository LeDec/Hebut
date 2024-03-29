package com.android.quest.common.aop;


import com.android.quest.common.ApiResponse;
import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 日志打印切面
 *
 * @author zuozhiwei
 */
@Aspect
@Component
@Slf4j
public class LogAop {

    /**
     * 切入点是控制器
     */
    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void log() {
    }

    /**
     * 方法执行前、后
     *
     * @param point 切入信息
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        // 取请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        // 准备打印
        StringBuilder sb = new StringBuilder();
        sb.append("\n【请求类名】:").append(point.getSignature().getDeclaringTypeName());
        sb.append("\n【请求方法名】:").append(point.getSignature().getName());
        // 因过滤器将request缓存，所以args可能有多个元素
        String body = "{}";
        Object[] argArray = point.getArgs();
        List<Object> argPrint = new ArrayList<>();
        if (argArray != null && argArray.length > 0) {
            for (Object arg : argArray) {
                if (!(arg instanceof ContentCachingRequestWrapper)) {
                    argPrint.add(arg);
                }
            }
            try {
                body = JsonUtil.toJson(argPrint);
            }catch (Exception e){
                body = "file";
            }
        }
        sb.append("\n【body】:").append(body);
        Object result;
        try {
            // 继续执行方法，得到返回值
            result = point.proceed();
        } catch (CustomException e) {
            // 发生异常则返回通用错误
            sb.append("\n【自定义异常】：系统返回自定义异常，信息为[").append(e.getMsg()).append("]");
            result = ApiResponse.error(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            // 发生异常则返回通用错误
            sb.append("\n【异常】：系统发生异常，异常信息为[").append(ExceptionUtils.getStackTrace(e)).append("]");
            result = ApiResponse.error(ErrorEnum.INNER_ERROR);
            log.debug("系统异常", e);
        }
        sb.append("\n【返回值】:").append(JsonUtil.toJson(result));
        request.setAttribute("log", sb);
        return result;
    }
}
