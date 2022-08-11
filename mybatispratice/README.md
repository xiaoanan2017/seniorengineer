## 20220810
1.mybatis 使用分页插件
  导入依赖即可实现自动装配 ，在执行查询之前
  设置分页参数即可 (此设置在当前线程有效)
  ```java
    Page<Object> page = PageMethod.startPage(1, 10);
```
  ```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.3</version>
</dependency>
  ```
2.mybatis 结果集映射
  ```java
public class StudentEO extends StudentDO{

    private ScoreDO scoreDO;

    private List<ScoreDO> list;
}
  ```
  2.1  一 对 一
  StudentEO -> scoreDO
  保证结果集中， 列名以 scoreDO. 开头的即可自动映射到 scoreDO对象上
  ```xml
<select id="selectEO" resultType="com.module.mybatispratice.po.StudentEO">
  select a.*,b.id 'scoreDO.id' ,b.student_id 'scoreDO.student_id',b.subject 'scoreDO.subject', b.score 'scoreDO.score'
  from student a
  left join score b
  on a.id = b.student_id
  where a.id = #{id}
</select>
```
  
  2.2 一对多
  StudentEO -> list
  需要使用到 collection
  ```xml
<resultMap id="score" type="com.module.mybatispratice.po.ScoreDO">
    <id column="id" property="id" jdbcType="NUMERIC"/>
    <result column="student_id" property="studentId" jdbcType="NUMERIC"/>
    <result column="subject" property="subject" jdbcType="VARCHAR"/>
    <result column="score" property="score" jdbcType="INTEGER"/>
</resultMap>
<!-- 一对多映射关系 结果集中 scoreDO 对象对应的列是以 scoreDO. 开头的 ，所以这里一定要配置好 columnPrefix="scoreDO." -->
<resultMap id="studentEO" type="com.module.mybatispratice.po.StudentEO">
    <id column="id" property="id" jdbcType="INTEGER"/>
    <result column="name" property="name" jdbcType="VARCHAR"/>
    <result column="age" property="age" jdbcType="INTEGER"/>
    <result column="address" property="address" jdbcType="VARCHAR"/>
    <result column="own_phone" property="ownPhone" jdbcType="VARCHAR"/>
    <result column="birthday" property="birthday" jdbcType="DATE"/>
    <collection property="list" columnPrefix="scoreDO." resultMap="score"/>
</resultMap> 

 <select id="selectEOlist" resultMap="studentEO">
        select a.*,b.id 'scoreDO.id' ,b.student_id 'scoreDO.student_id',b.subject 'scoreDO.subject', b.score 'scoreDO.score'
        from student a
        left join score b
        on a.id = b.student_id
        where a.id = #{id}
    </select>
```
3.建议使用 fastjson做json字符串和对象之间的转换工具 ,导入
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.78</version>
</dependency>
```
如果要使用jackson,导入
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
```

##20220811 自定义插件
//yml文件中的很多参数看着跟 SqlSessionFactoryBean 中的属性名一直，可是如果是自定义了 SqlSessionFactoryBean ，实际上是无法直接通过属性赋值的
//可以借用 MybatisProperties 来进行设值
        
##20220812 引入登录机制