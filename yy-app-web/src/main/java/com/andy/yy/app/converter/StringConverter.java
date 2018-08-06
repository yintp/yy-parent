package com.andy.yy.app.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @author richard
 * @since 2018/2/5 16:42
 */
public class StringConverter implements Converter<String, String> {
    @Override
    public String convert(String str) {
        return StringUtils.isNotEmpty(str) ? str.trim() : str;
    }
}
