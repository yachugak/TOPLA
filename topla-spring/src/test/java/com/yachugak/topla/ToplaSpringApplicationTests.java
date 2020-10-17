package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yachugak.topla.repository.TestRepository;

@SpringBootTest
class ToplaSpringApplicationTests {
	@Autowired
	private TestRepository testRepository;

	@Test
	void contextLoads() {
	}
	
	@Test
	void dbLinkTest() {
		Optional<com.yachugak.topla.entity.Test> opRecode = testRepository.findById(1L);
		if(opRecode.isPresent() == false) {
			fail("검색 결과 없어서 연결 테스트 불가");
			return;
		}
		com.yachugak.topla.entity.Test recode = opRecode.get();
		assertEquals("이승준승이", recode.getvVarchar());
	}
}
