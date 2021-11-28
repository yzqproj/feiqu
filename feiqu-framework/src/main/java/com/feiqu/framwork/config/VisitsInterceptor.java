package com.feiqu.framwork.config;

import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.JedisUtil;
import com.feiqu.framwork.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.commands.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanni
 * @date time 2021/11/28 13:56
 * @modified By:
 */
@Slf4j
public class VisitsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果不是ajax 代表是点击页面 则统计点击数量
        if (null == request.getHeader("X-Requested-With") || !request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            try {
                String ip = WebUtil.getIP(request);
                JedisCommands commands = JedisUtil.me();
                boolean isBlackIp = commands.sismember(CommonConstant.FQ_BLACK_LIST_REDIS_KEY, ip);
                if (isBlackIp) {
                    log.info("该ip:{} 存在于黑名单，禁止其访问！", ip);
                    response.sendRedirect(request.getContextPath() + "/blackList/denyService");
                    return false;
                }

                String clickkey = CommonConstant.FQ_IP_DATA_THIS_DAY_FORMAT;
                Double score = commands.zscore(clickkey, ip);
                if (score == null) {
                    commands.zadd(clickkey, 1, ip);
                } else {
                    commands.zadd(clickkey, score + 1, ip);
                }
                commands.expire(clickkey, 30 * 24 * 60 * 60L);//存放一个月
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
