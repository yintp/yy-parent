package com.andy.yy.base.log;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author richard
 * @since 2018/2/1 11:07
 */
public class LevelFilter extends AbstractMatcherFilter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (!isStarted()) {
			return FilterReply.NEUTRAL;
		}

		if (event.getLevel() == Level.WARN || event.getLevel() == Level.ERROR) {
			return onMatch;
		} else {
			return onMismatch;
		}
	}

}
