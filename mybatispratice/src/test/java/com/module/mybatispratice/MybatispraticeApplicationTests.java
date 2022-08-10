package com.module.mybatispratice;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.module.mybatispratice.mapper.StudentMapper;
import com.module.mybatispratice.po.StudentDO;
import com.module.mybatispratice.po.StudentEO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
		int count = 10;
		for (int i = 0; i < count; i++) {
			StudentDO student = StudentDO.builder()
					.age(i)
					.name("张三")
					.address("中国大陆")
					.ownPhone("15112345678")
					.birthday(new Date())
					.build();
			Long result = studentMapper.insert(student);
		}
//		System.out.println(student);
//		System.out.println(result);
		log.info("完成");
	}

	@Test
	void select() {
		Page<Object> page = PageMethod.startPage(1, 10);
		List<StudentDO> list = studentMapper.list(20L);
		System.out.println("总记录数 " + page.getTotal());
		System.out.println(list);
		log.info("完成");
	}

	@Test
	void selectEO() {
		Page<Object> page = PageMethod.startPage(1, 10);
		StudentEO studentEO = studentMapper.selectEO(2L);
		System.out.println("总记录数 " + page.getTotal());
		System.out.println(JSON.toJSONString(studentEO));
		log.info("完成");
	}

	@Test
	void selectEOlist() {
		Page<Object> page = PageMethod.startPage(1, 10);
		StudentEO studentEO = studentMapper.selectEOlist(1L);
		System.out.println("总记录数 " + page.getTotal());
		System.out.println(JSON.toJSONString(studentEO));
		log.info("完成");
	}

}
