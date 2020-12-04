package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yachugak.topla.util.DayCalculator;

@SpringBootTest
public class DayCalculatorTest {

	@Test
	public void dayOffsetTest() {
		Date planStartDate = DayCalculator.makeDate(2020, 11, 23);
		Date offset0 = DayCalculator.makeDate(2020, 11, 23);
		Date offset1 = DayCalculator.makeDate(2020, 11, 24);
		Date offset2 = DayCalculator.makeDate(2020, 11, 25);
		Date offsetMinus1 = DayCalculator.makeDate(2020, 11, 22);
		
		Date year2020 = DayCalculator.makeDate(2020, 12, 25);
		Date year2021 = DayCalculator.makeDate(2021, 1, 2);
		
		assertEquals(23, planStartDate.getDate());
		assertEquals(23, offset0.getDate());
		assertEquals(24, offset1.getDate());
		assertEquals(25, offset2.getDate());
		assertEquals(22, offsetMinus1.getDate());
		assertEquals(25, year2020.getDate());
		assertEquals(2, year2021.getDate());
		
		assertEquals(0, DayCalculator.calDayOffset(planStartDate, offset0));
		assertEquals(1, DayCalculator.calDayOffset(planStartDate, offset1));
		assertEquals(2, DayCalculator.calDayOffset(planStartDate, offset2));
		assertEquals(-1, DayCalculator.calDayOffset(planStartDate, offsetMinus1));
		assertEquals(8, DayCalculator.calDayOffset(year2020, year2021));
	}
	
	
	@Test
	public void dayTest() {
		Date testTarget = DayCalculator.makeDate(2020, 6, 18);
		assertEquals(4, DayCalculator.getDay(testTarget)); //목요일 테스트

		Date testTarget2 = DayCalculator.makeDate(2020, 6, 7);
		assertEquals(0, DayCalculator.getDay(testTarget2)); //일요일 테스트
	}
}
