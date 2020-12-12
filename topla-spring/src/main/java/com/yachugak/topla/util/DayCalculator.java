package com.yachugak.topla.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	// LocalDate -> Date
	public static Date convertToDateViaInstant(LocalDate localDateToConvert) {
	    return java.util.Date.from(localDateToConvert.atStartOfDay()
	      .atZone(ZoneId.systemDefault())
	      .toInstant());
	}
	
	// 당일 기준 7일전 날과 오늘의 날짜를 <List>LocalDate 형태로 반환
	public static List<Date> getStartAndEndDateOf7Days() {
		LocalDate endDate = LocalDate.now().plusDays(1);	// 오늘까지 포함할 수 있도록.
		LocalDate startDate = endDate.minusDays(8);			// 7일전까지
		
		Date start = convertToDateViaInstant(startDate);
		Date end = convertToDateViaInstant(endDate);
		
		ArrayList<Date> dateList = new ArrayList<>();
		dateList.add(start);
		dateList.add(end);
		return dateList;
	}
}
