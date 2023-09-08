package com.android.quest.common.interceptor;


import com.android.quest.common.util.IpUtil;
import com.android.quest.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 作者
 */
@Slf4j
public class AccessTokenInterceptor implements HandlerInterceptor {

    // 本线程存储计时器
    private static final ThreadLocal<StopWatch> TIME = new ThreadLocal<>();
    // 本线程存储日志信息
    private static final ThreadLocal<StringBuilder> LOG_INFO = new ThreadLocal<>();

    /**
     * 在请求之前需要处理的内容
     *
     * @param request  请求对象
     * @param response 返回对象
     * @param handler  handler对象
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 开始计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 计时器放在本线程中
        TIME.set(stopWatch);

        // 获取请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //获取请求头部
        Enumeration<String> enumeration = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(16);
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                headerMap.put(key, request.getHeader(key));
            }
        }
        // 生成请求id
        String requestId = String.valueOf(UUID.randomUUID()).replaceAll("-", "");
        // 请求id放在request的域中
        request.setAttribute("requestId", requestId);
        // 请求id放在log上下文中
        MDC.put("requestId", requestId);
        // 准备日志信息
        StringBuilder sb = new StringBuilder();
        sb.append("\n【request_id】：").append(requestId);
        sb.append("\n【请求 URL】：").append(request.getRequestURL());
        sb.append("\n【请求 IP】：").append(IpUtil.getIp(request));
        sb.append("\n【请求Header：】").append(JsonUtil.toJson(headerMap));
        sb.append("\n【请求参数】：").append(JsonUtil.toJson(parameterMap));
        // 日志信息存放在本线程中
        LOG_INFO.set(sb);
        return true;
    }

    /**
     * 在请求之后
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在请求全部完成之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 取出计时器
        StopWatch stopWatch = TIME.get();
        // 计时器停止
        stopWatch.stop();
        // 取计时器耗时
        long time = stopWatch.getTotalTimeMillis();
        // 计时器线程清除
        TIME.remove();

        // 取出日志信息
        StringBuilder localLog = LOG_INFO.get();
        // 日志信息线程清除
        LOG_INFO.remove();
        // 日志拼接接口耗时
        localLog.append("\n【接口耗时：】").append(time).append("ms");

        // 从request取aop打印的log信息
        StringBuilder logInfo = (StringBuilder) request.getAttribute("log");
        localLog.append(logInfo);

        // 打印请求日志
        log.info(localLog.toString());
        // 清除log上下文信息
        MDC.clear();
    }
}
