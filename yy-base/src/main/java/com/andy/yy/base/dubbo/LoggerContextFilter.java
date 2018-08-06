package com.andy.yy.base.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.andy.yy.base.log.LoggerUtils;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER}, order = -9000)
public class LoggerContextFilter implements Filter {

	private static final LoggerUtils logger = LoggerUtils.newLogger(LoggerContextFilter.class);

	private static final String REQUEST_ID_KEY = "request_id";
	private static final String SEQUENCE_KEY = "sequence";
	private static final String USER_ID_KEY = "user_id";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try {
			if (RpcContext.getContext().getAttachment(REQUEST_ID_KEY) == null) {
				String requestId = LoggerUtils.getRequestId();
				RpcContext.getContext().setAttachment(REQUEST_ID_KEY, requestId);
				Integer se = LoggerUtils.getSequence();
				RpcContext.getContext().setAttachment(SEQUENCE_KEY, String.valueOf(se));
				String userId = LoggerUtils.getUserId();
				RpcContext.getContext().setAttachment(USER_ID_KEY, userId);
			} else {
				LoggerUtils.setRequestId(RpcContext.getContext().getAttachment(REQUEST_ID_KEY));
				LoggerUtils.setSequence(Integer.parseInt(RpcContext.getContext().getAttachment(SEQUENCE_KEY)));
				LoggerUtils.setUserId(RpcContext.getContext().getAttachment(USER_ID_KEY));
			}
		} catch (Exception e) {
			logger.error("exception in dubboContextFilter :{}", e.getMessage());
		}
		return invoker.invoke(invocation);
	}
}
