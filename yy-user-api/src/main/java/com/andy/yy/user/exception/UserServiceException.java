package com.andy.yy.user.exception;

import com.andy.yy.base.core.ExceptionCode;
import com.andy.yy.base.core.ServiceException;

/**
 * @author richard
 * @since 2018/1/30 18:09
 */
public class UserServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public UserServiceException(ExceptionCode code) {
        super(code);
    }
    public UserServiceException(ExceptionCode code, Throwable cause) {
        super(code, cause);
    }
}
