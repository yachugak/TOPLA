package com.yachugak.topla.util;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = DayCalculator.makeLocalDate(year, monthWithStart1, date);
        Date temp = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
		return temp;
	}
	
	public static LocalDate makeLocalDate(int year, int monthWithStart1, int date) {
		return LocalDate.of(year, monthWithStart1, date);
	}
	
	public static LocalDate DateToLocalDate(Date date) {
		try {
			return date.toInstant()
					  .atZone(ZoneId.systemDefault())
					  .toLocalDate();
		}
		catch(Exception e) {
			return Instant.ofEpochMilli(date.getTime())
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate();
		}
	}
	
	public static Date LocalDateToDate(LocalDate localDate) {
		return DayCalculator.makeDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
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

	public static Date getTodayDate() {
		return DayCalculator.LocalDateToDate(DayCalculator.getTodayLocalDate());
	}
	
	public static LocalDate getTodayLocalDate() {
		return LocalDate.now();
	}
	
	public static String dateFormat(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public static String dateFormat(Date date) {
		return DayCalculator.dateFormat(DayCalculator.DateToLocalDate(date));
	}
	
	public static int getYear(Date date) {
		return DayCalculator.DateToLocalDate(date).getYear();
	}
	
	//1월이 1임
	public static int getMonth(Date date) {
		return DayCalculator.DateToLocalDate(date).getMonthValue();
	}
	
	public static int getDate(Date date) {
		return DayCalculator.DateToLocalDate(date).getDayOfMonth();
	}

	public static int getYear(LocalDate localDate) {
		return localDate.getYear();
	}
	
	//1월이 1임
	public static int getMonth(LocalDate localDate) {
		return localDate.getMonthValue();
	}
	
	public static int getDate(LocalDate localDate) {
		return localDate.getDayOfMonth();
	}
}
