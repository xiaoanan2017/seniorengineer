package com.module.mybatispratice;

import com.module.mybatispratice.mapper.StudentMapper;
import com.module.mybatispratice.po.StudentDO;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = MybatispraticeApplication.class)
@Slf4j
class MybatispraticeApplicationTests {

	@Resource
	private StudentMapper studentMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void studentTest() {
		StudentDO studentDO = studentMapper.select(1L);
//		log.info(studentDO.toString());

		StudentDO student = StudentDO.builder()
				.age(10)
				.name("张三")
				.address("中国大陆")
				.ownPhone("151123456789")
				.birthday(new Date())
				.build();
		Long result = studentMapper.insert(student);
		System.out.println(student);
		System.out.println(result);
	}
}
