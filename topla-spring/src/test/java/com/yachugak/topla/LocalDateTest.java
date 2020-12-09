package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocalDateTest {
	@Test
	public void cloneTest() {
		LocalDate date = LocalDate.of(2020, 12, 9);
		LocalDate copiedDate = date;
		copiedDate = copiedDate.plusDays(1);
		
		assertEquals(9, date.getDayOfMonth());
	}
}
