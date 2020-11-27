package com.yachugak.topla.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DayCalculator {
	public static int calDayOffset(Date startDay, Date endDay) {
		long diffInMillies = endDay.getTime() - startDay.getTime();
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    return (int)diff;
	}
}
