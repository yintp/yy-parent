package com.andy.yy.base.log;

import com.alibaba.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
public class LoggerUtils {

    private Logger logger;

    private static final String LOG_KEY = "requestId={}|sequence={}|userId={}|clientIp={}|message={}";
    private static final String METHOD_KEY = "requestId={}|sequence={}|userId={}|clientIp={}|time={}|params={}|result={}";

    private static final ThreadLocal<String> requestIdContext = new ThreadLocal<>();
    private static final ThreadLocal<Integer> sequenceContext = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdContext = new ThreadLocal<>();

    private LoggerUtils (String serviceName, Logger logger) {
        this.logger = logger;
    }

    public static LoggerUtils newLogger(Class<?> clazz) {
        return new LoggerUtils(clazz.getSimpleName(), LoggerFactory.getLogger(clazz));
    }
    public static LoggerUtils newLogger(String serviceName) {
        return new LoggerUtils(serviceName, LoggerFactory.getLogger(serviceName));
    }

    public void info(String text) {
    	String requestId = getRequestId();
    	Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.info(LOG_KEY, requestId, sequence, userId, clientIp, text);
    }
    public void info(String text, Object... args) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.info(LOG_KEY, requestId, sequence, userId, clientIp, parse("{", "}", text, args));
    }
    public void debug(String text) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.debug(LOG_KEY, requestId, sequence, userId, clientIp, text);
    }
    public void debug(String text, Object... args) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.debug(LOG_KEY, requestId, sequence, userId, clientIp, parse("{", "}", text, args));
    }
    public void error(String text) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.error(LOG_KEY, requestId, sequence, userId, clientIp, text);
    }
    public void error(String text, Object... args) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.error(LOG_KEY, requestId, sequence, userId, clientIp, parse("{", "}", text, args));
    }

    public void infoMethod(long time, String params, String result) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.info(METHOD_KEY, requestId, sequence, userId, clientIp, time, params, result);
    }
    public void debugMethod(long time, String params, String result) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.debug(METHOD_KEY, requestId, sequence, userId, clientIp, time, params, result);
    }
    public void errorMethod(long time, String params, Throwable e) {
        String requestId = getRequestId();
        Integer sequence = getSequence();
        String userId = getUserId();
        String clientIp = this.getClientIp();
        logger.error(METHOD_KEY, requestId, sequence, userId, clientIp, time, params, e);
    }

    public static void clearContext() {
        requestIdContext.set(null);
        sequenceContext.set(0);
        userIdContext.set(null);
    }

    public static String getRequestId() {
        String id = requestIdContext.get();
        if(id == null) {
            id = UUID.randomUUID().toString().replace("-", "");
            requestIdContext.set(id);
        }
        return id;
    }
    public static void setRequestId(String requestId) {
        requestIdContext.set(requestId);
    }

    public static Integer getSequence() {
        Integer se = sequenceContext.get();
        if(se == null) {
            se = 0;
        }
        se++;
        sequenceContext.set(se);
        return se;
    }
    public static void setSequence(Integer se) {
        sequenceContext.set(se);
    }

    public static void  decrMethodSequence() {
        Integer se = sequenceContext.get();
        if(se != null) {
            se--;
            sequenceContext.set(se);
        }
    }

    public static String getUserId() {
        return userIdContext.get();
    }
    public static void setUserId(String userId) {
        userIdContext.set(userId);
    }

    private String getClientIp() {
        RpcContext rpcContent = RpcContext.getContext();
        if(rpcContent == null) {
            return "null";
        } else {
            return rpcContent.getRemoteHost();
        }
    }

    private String parse(String openToken, String closeToken, String text, Object... args) {
        if (args == null || args.length <= 0) {
            return text;
        }
        int argsIndex = 0;

        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(openToken, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    String value = (argsIndex <= args.length - 1) ?
                            (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                    builder.append(value);
                    offset = end + closeToken.length();
                    argsIndex++;
                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
