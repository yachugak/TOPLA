package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JUnitTest {
	
	@Test
	@Disabled
	public void mustFailTest() {
		assertTrue(false);
	}
	
	@Test
	@Disabled
	public void mustThorwExceptionTest() {
		assertTrue(true);
		throw new Error("나능햐 에러");
	}
}
