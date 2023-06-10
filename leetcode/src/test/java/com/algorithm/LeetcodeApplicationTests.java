package com.algorithm;

import com.algorithm.leetcode.LeetcodeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;


@SpringBootTest(classes = LeetcodeApplication.class)
class LeetcodeApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("aa");
	}

	public static void main(String[] args) {
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		Arrays.stream(threadMXBean.dumpAllThreads(false, false)).forEach(t-> {
			System.out.println("[" + t.getThreadId() + "]," + t.getThreadName());
		});
	}
}
