package com.yachugak.topla.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DayCalculator {
	public static int calDayOffset(Date dayStart, Date dayEnd) {
		return DayCalculator.calDayOffset(DayCalculator.DateToLocalDate(dayStart), DayCalculator.DateToLocalDate(dayEnd));
	}
	
	public static int calDayOffset(LocalDate dayStart, LocalDate dayEnd) {
		return (int) ChronoUnit.DAYS.between(dayStart, dayEnd);
	}
	
	public static Date makeDate(int year, int monthWithStart1, int date) {
		Date temp = new Date(year, monthWithStart1-1, date, 12, 0, 0);
		return temp;
	}
	
	public static LocalDate DateToLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth()+1, date.getDate());
	}
	
	public static Date LocalDateToDate(LocalDate localDate) {
		return DayCalculator.makeDate(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
	}
	
	public static int getDay(Date date) {
		LocalDate localDate = DayCalculator.DateToLocalDate(date);
		int localDateDay = localDate.getDayOfWeek().getValue();
		
		if(localDateDay == 7) {
			return 0;
		}
		
		return localDateDay;
	}
}
