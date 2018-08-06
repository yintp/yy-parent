package com.andy.yy.base.db;

import java.lang.annotation.*;

/**
 * @author richard
 * @since 2018/1/31 15:59
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceKey {
	String value() default "";
}
