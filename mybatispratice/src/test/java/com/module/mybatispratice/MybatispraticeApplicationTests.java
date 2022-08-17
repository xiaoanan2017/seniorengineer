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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = MybatispraticeApplication.class)
@Slf4j
@EnableTransactionManagement
class MybatispraticeApplicationTests {

	private CountDownLatch countDownLatch;

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
	void testTwoLevelCache(){
		countDownLatch = new CountDownLatch(2);
		Thread t = new Thread(() -> {
			this.selectEO();
			countDownLatch.countDown();
		});

		Thread t1 = new Thread(() -> {
			this.selectEO2();
			countDownLatch.countDown();
		});
		t.start();
		t1.start();
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("全部执行完毕");
	}
	/**
	 * 测试结果集 1对10
	 */
	@Test
	@Transactional
	void selectEO() {
//		Page<Object> page = PageMethod.startPage(1, 10);
		StudentEO studentEO = studentMapper.selectEO(2L);
//		System.out.println("总记录数 " + page.getTotal());
		System.out.println(JSON.toJSONString(studentEO));
		StudentEO studentEO2 = studentMapper.selectEO(2L);
		System.out.println(JSON.toJSONString(studentEO2));

//		StudentDO studentDO = new StudentDO();
//		studentDO.setId(2L);
//		studentDO.setAddress("地址999");
//		studentMapper.updateStudentById(studentDO);
//		StudentEO studentEO3 = studentMapper.selectEO(2L);
//		System.out.println(JSON.toJSONString(studentEO3));
		log.info("完成");
	}

	/**
	 * 测试结果集 1对10
	 */
	@Test
	@Transactional
	void selectEO2() {
		Page<Object> page = PageMethod.startPage(1, 10);
		StudentEO studentEO = studentMapper.selectEO(2L);
		System.out.println("总记录数 " + page.getTotal());
		System.out.println(JSON.toJSONString(studentEO));
		studentEO.setAge(13);
		StudentEO studentEO2 = studentMapper.selectEO(2L);
		System.out.println(JSON.toJSONString(studentEO2));
		StudentDO studentDO = new StudentDO();
		studentDO.setId(2L);
		studentDO.setAddress("上海市浦东新区999");
		studentMapper.updateStudentById(studentDO);
		StudentEO studentEO3 = studentMapper.selectEO(2L);
		System.out.println(JSON.toJSONString(studentEO3));
		log.info("完成");
	}
	/**
	 * 测试结果集 1对 多
	 */
	@Test
	void selectEOlist() {
		Page<Object> page = PageMethod.startPage(1, 10);
		StudentEO studentEO = studentMapper.selectEOlist(1L);
		System.out.println("总记录数 " + page.getTotal());
		System.out.println(JSON.toJSONString(studentEO));
		log.info("完成");
	}

	
}
