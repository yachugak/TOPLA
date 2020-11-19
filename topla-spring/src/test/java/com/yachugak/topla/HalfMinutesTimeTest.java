package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yachugak.topla.util.HalfMinutesTime;

@SpringBootTest
public class HalfMinutesTimeTest {
	@Test
	public void isHalfTest() {
		for(int i=0; i<=1440; i+=30) {
			assertTrue(HalfMinutesTime.isHalfMinutesTime(i));
			assertFalse(HalfMinutesTime.isHalfMinutesTime(i+15));
			assertFalse(HalfMinutesTime.isHalfMinutesTime(i-8));
		}
	}
	
	@Test
	public void roundUpAndDownTest() {
		assertEquals(60, HalfMinutesTime.roundUpAndDown(59));
		assertEquals(60, HalfMinutesTime.roundUpAndDown(73));

		assertEquals(0, HalfMinutesTime.roundUpAndDown(0));
		assertEquals(0, HalfMinutesTime.roundUpAndDown(-5));

		assertEquals(120, HalfMinutesTime.roundUpAndDown(105));
		assertEquals(120, HalfMinutesTime.roundUpAndDown(123));

		assertEquals(30, HalfMinutesTime.roundUpAndDown(35));
		assertEquals(30, HalfMinutesTime.roundUpAndDown(25));

		assertEquals(-30, HalfMinutesTime.roundUpAndDown(-30));
		assertEquals(-30, HalfMinutesTime.roundUpAndDown(-25));
		assertEquals(-30, HalfMinutesTime.roundUpAndDown(-35));
	}
	
	@Test
	public void roundDownTest() {
		assertEquals(30, HalfMinutesTime.roundDown(30));
		assertEquals(30, HalfMinutesTime.roundDown(31));
		assertEquals(30, HalfMinutesTime.roundDown(35));
		assertEquals(30, HalfMinutesTime.roundDown(39));
	}

	@Test
	public void roundUpTest() {
		assertEquals(0, HalfMinutesTime.roundUp(0));
		assertEquals(30, HalfMinutesTime.roundUp(1));
		assertEquals(30, HalfMinutesTime.roundUp(5));
		assertEquals(30, HalfMinutesTime.roundUp(15));
		assertEquals(30, HalfMinutesTime.roundUp(29));
		assertEquals(30, HalfMinutesTime.roundUp(30));
	}
}
