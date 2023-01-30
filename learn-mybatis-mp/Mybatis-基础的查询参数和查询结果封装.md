# 总体概述

使用`MyBatis`个人认为分为以下几大块

* 配置，包括所扫描的mapper位置、是否开启驼峰、数据库连接池信息等
* 如何使用查询参数
* 如何处理返回结果
* 动态`SQL`，方便编写查询条件和批量操作

其中如何使用查询参数和处理返回结果部分都在`xml`文件中进行定义。

# 查询参数部分

## 如何定义查询参数

查询参数一般有两种情况：

* 直接传入一个对象;
* 传入多个字段；

第一种情况我们不需要任何注解，直接在`mapper`方法中传入对象，然后在`sql`语句中直接使用该对象的属性即可;

```java
// 接口声明，根据实体查询会议
Meet queryByMeet(Meet meet);

// 在xml文件中使用
<!-- 传入一个实体，直接使用实体字段 -->
<select id="queryByMeet" resultType="com.hjyp.mybatislearning.entity.Meet">
    select * from meettable where meet_id = #{meetId}
</select>
```



第二种情况可以使用`@Param()`注解表示每个参数，底层会封装为一个`map`对象，这样可以在`xml`文件中根据名称直接使用。

```java
// 接口声明，根据id查询会议
Meet queryById(@Param("id") Integer id);

// 在xml文件中使用
<!-- 根据id查询 -->
<select id="queryById" resultType="com.hjyp.mybatislearning.entity.Meet">
    select * from meettable where meet_id = #{id}
</select>
```

## 如何使用查询参数

使用查询参数一般通过两种方式`#{}, ${}`。

`#{}`会根据参数的类型自动填充，如果是字符串会自动加上引号，不会引起`SQL注入`，日常情况下多使用这种方式。

`${}`是字符串拼接，`不会自动加引号`，如果是字符串类型或者日期类型的字段进行赋值时，需要手动加引号。常用的方式有三种：

* **模糊查询**：因为模糊查询的条件一般是字符串类型，但我们不能使用#{}，因为这样会自动带上引号。若使用`like %#{content}%`则匹配条件为`like %'content'%`，应该使用`like %${content}%`,匹配条件为`like %content%`,这样才是正确的。
* **批量删除**：批量删除一般会传过来一个string类型的字符串，内容为删除内容的id，比如`"1, 2, 3"`。这个时候只能使用代码`delete from t_table where id in (${ids})`。因为这样不会加引号，可以正确删除。
* **动态设置表名**：有时表名会作为参数传入，此时也只能使用`${}`，只有这样才不会额外多一个引号。`select * from ${table_name}`

其实还有另一种模糊查询方法更常见也更安全，不会`sql注入`，即使用字符串的自动拼接功能，代码为`like "%"#{content}"%"`

# 结果映射

## 返回结果是单条已有实体类

如果返回结果已有对应的实体类，那么直接在`sql`文件中指定`resultType`就可以了。`Mybatis`会自动为实体类属性赋值。

```xml
<!-- 根据id查询 -->
<select id="queryById" resultType="com.hjyp.mybatislearning.entity.Meet">
	select * from meettable where meet_id = #{id}
</select> 
```

注意：对于没有查询出来的字段会直接设为`null`。

## 返回结果是多条已有实体类

对于返回结果是多条时，接口返回值用`List<Entity>`接收，`xml`文件还是指定`实体类型`即可。

## 返回结果没有对应实体类

### 将返回结果封装为Map

任何查询都可以用Map来接收，因为对象本身也是一个map。这时接口的返回值类型为`Map<String, Object>`,`xml`文件的`resultType='java.util.Map'`

接口定义

```java
// 根据实体查询会议
Map<String, Object> queryByMeet(Meet meet);
```

`xml`文件定义

```xml
<!-- 传入一个实体，直接使用实体字段 -->
<select id="queryByMeet" resultType="java.util.Map">
	select * from meettable where meet_id = #{meetId}
</select>
```

### 使用`ResultMap`解决数据库表字段与实体类属性不匹配

我们可以使用`resultMap`自定义返回结果，在`resultMap`中绑定属性名与字段名。

`resultMap`是一个标签，该标签有`id属性`：用来唯一标识自定义结果映射; `type属性`：用来指定实体类型。

`id`是`resultMap`的子标签，用来标识主键；`result`也是`resultMap`的子标签。

子标签拥有`property属性`：用来指定实体类的字段名；`column属性`：用来指定表的列名；

> 注意：使用自定义`resultMap`时，即使属性名和列名一致，也是需要显式指定的，不可以省略！

```xml
<resultMap id="empResultMap" type="Emp">
	<id property="eid" column="eid"></id>
	<result property="empName" column="emp_name"></result>
	<result property="age" column="age"></result>
	<result property="sex" column="sex"></result>
	<result property="email" column="email"></result>
</resultMap>
<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultMap="empResultMap">
	select * from t_emp
</select>

```

### 一对多结果映射

#### 方式一：使用collection处理一对多映射关系

* `ofType`：指定集合里的实体类型

```xml
<resultMap id="meetAttachResultMap" type="com.hjyp.mybatislearning.entity.Meet">
        <id property="meetId" column="meet_id"></id>
        <result property="meetTime" column="meet_time"></result> 
        <result property="meetPlace" column="meet_place"></result> 
        <result property="meetTheme" column="meet_theme"></result> 
        <result property="meetReporter" column="meet_reporter"></result> 
        <result property="meetReporterId" column="meet_reportor_id"></result> 
        <result property="meetBelongRole" column="meet_belong_role"></result> 
        <result property="lastUpdatetime" column="last_updatetime"></result> 
        <!-- 使用collection-->
        <collection property="attachs" ofType="com.hjyp.mybatislearning.entity.Attach">
            <id property="attachId" column="attach_id"></id>
            <result property="attachName" column="attach_name"></result> 
            <result property="downloadUrl" column="download_url"></result> 
            <result property="lastUpdatetime" column="last_updatetime"></result> 
        </collection>
        
    </resultMap>

    <!-- 根据id查询会议 -->
    <select id="queryById" resultMap="meetAttachResultMap">
        select * from meettable inner join attachtable on attachtable.meet_id = meettable.meet_id where meettable.meet_id = #{id}
    </select>
```



#### 方式二：使用分步查询

和多对一类似，只不过子标签换成了`collection`

* `property`: 对应的属性字段
* `select`：第二个查询语句的mapper所在包和方法
* `column`：第二个查询语句所用到的条件

```xml
<resultMap id="meetAttachResultMap" type="com.hjyp.mybatislearning.entity.Meet">
        <id property="meetId" column="meet_id"></id>
        <result property="meetTime" column="meet_time"></result> 
        <result property="meetPlace" column="meet_place"></result> 
        <result property="meetTheme" column="meet_theme"></result> 
        <result property="meetReporter" column="meet_reporter"></result> 
        <result property="meetReporterId" column="meet_reportor_id"></result> 
        <result property="meetBelongRole" column="meet_belong_role"></result> 
        <result property="lastUpdatetime" column="last_updatetime"></result> 
        <!-- 使用collection的分阶段查询-->
        <collection property="attachs"
                select="com.hjyp.mybatislearning.mapper.AttachMapper.queryById"
                column="meet_id">
        </collection>
        
    </resultMap>

    <!-- 根据id查询会议 -->
    <select id="queryById" resultMap="meetAttachResultMap">
        select * from meettable where meet_id = #{id}
    </select>
    
    <!-- 根据id查询附件 -->
    <select id="queryById" resultType="com.hjyp.mybatislearning.entity.Attach">
        select * from attachtable where meet_id = #{meetId}
    </select>
```



### 多对一结果映射

#### 方式一：级联属性赋值

使用`字段.属性`的方法来指定，还是用`result`子标签,`property`指定属性名，`column`指定列名

```xml
<resultMap id="meetAttachResultMap" type="com.hjyp.mybatislearning.entity.Meet">
        <id property="meetId" column="meet_id"></id>
        <result property="meetTime" column="meet_time"></result> 
        <result property="meetPlace" column="meet_place"></result> 
        <result property="meetTheme" column="meet_theme"></result> 
        <result property="meetReporter" column="meet_reporter"></result> 
        <result property="meetReporterId" column="meet_reportor_id"></result> 
        <result property="meetBelongRole" column="meet_belong_role"></result> 
        <result property="lastUpdatetime" column="last_updatetime"></result> 
        <!-- 级联方式映射 -->
        <result property="attach.attachName" column="attach_name"></result> 
        <result property="attach.downloadUrl" column="download_url"></result> 
        <result property="attach.lastUpdatetime" column="last_updatetime"></result> 
    </resultMap>

    <!-- 根据id连表查询 -->
    <select id="queryById" resultMap="meetAttachResultMap">
        select * from meettable inner join attachtable on attachtable.meet_id = meettable.meet_id where meettable.meet_id = #{id}
    </select> 
```

#### 方式二：使用association处理映射关系

* `association`: 子标签专门用来处理多对一关系
* `property`：需要处理的映射关系的实体属性名
* `javaType`：该属性的类型

> 注意：使用`association`需要通过`javaType`指定属性的类型

```xml
<resultMap id="meetAttachResultMap" type="com.hjyp.mybatislearning.entity.Meet">
        <id property="meetId" column="meet_id"></id>
        <result property="meetTime" column="meet_time"></result> 
        <result property="meetPlace" column="meet_place"></result> 
        <result property="meetTheme" column="meet_theme"></result> 
        <result property="meetReporter" column="meet_reporter"></result> 
        <result property="meetReporterId" column="meet_reportor_id"></result> 
        <result property="meetBelongRole" column="meet_belong_role"></result> 
        <result property="lastUpdatetime" column="last_updatetime"></result> 
        <!-- association子标签处理 ， javaType是显式指定该实体属性的类型-->
        <association property="attach" javaType="com.hjyp.mybatislearning.entity.Attach">
            <id property="attachId" column="attach_id"></id>
            <result property="attachName" column="attach_name"></result> 
            <result property="downloadUrl" column="download_url"></result> 
            <result property="lastUpdatetime" column="last_updatetime"></result> 
        </association>
        
    </resultMap>

    <!-- 根据id连表查询 -->
    <select id="queryById" resultMap="meetAttachResultMap">
        select * from meettable inner join attachtable on attachtable.meet_id = meettable.meet_id where meettable.meet_id = #{id}
    </select>  
```

#### 方式三：分步查询

先根据会议ID查出会议，再根据附件ID查出附件，其实是两个查询语句

* `property`: 对应的属性字段
* `select`：第二个查询语句的mapper所在包和方法
* `column`：第二个查询语句所用到的条件

```xml
<resultMap id="meetAttachResultMap" type="com.hjyp.mybatislearning.entity.Meet">
        <id property="meetId" column="meet_id"></id>
        <result property="meetTime" column="meet_time"></result> 
        <result property="meetPlace" column="meet_place"></result> 
        <result property="meetTheme" column="meet_theme"></result> 
        <result property="meetReporter" column="meet_reporter"></result> 
        <result property="meetReporterId" column="meet_reportor_id"></result> 
        <result property="meetBelongRole" column="meet_belong_role"></result> 
        <result property="lastUpdatetime" column="last_updatetime"></result> 
        <!-- 使用分步查询 -->
        <association property="attach"
                select="com.hjyp.mybatislearning.mapper.AttachMapper.queryById"
                column="meet_id">
        </association>
        
    </resultMap>

    <!-- 根据id查询会议 -->
    <select id="queryById" resultMap="meetAttachResultMap">
        select * from meettable where meet_id = #{id}
    </select>

	<!-- 根据id查询附件 -->
    <select id="queryById" resultType="com.hjyp.mybatislearning.entity.Attach">
        select * from attachtable where meet_id = #{meetId}
    </select>  
```

## 其他问题

* 当实体类中的某个字段在数据库中不存在时，只要保证插入语句不出现该字段就可以了。但是在`MybatisPlus`中由于是以实体作为操作单元，所以要使用注解忽略不存在的属性。
* 当数据库没有查询出该字段时，实体类一般为`null`

# 动态SQL

动态`SQL`可以根据特定条件动态拼接`SQL`语句，比如查询时`where`语句后面的`各种条件`。常见的标签有`if`；`where`；`trim`；`choose、when、otherwise`；`foreach`、

## `if标签`

if标签用来判断某个属性是否为空，如果不为空可以拼接相应的语句。

使用`if`时可以在`where`语句后加上一个恒成立条件，比如`1=1`，然后把每条分支语句前加个 `and`

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp where 1=1
	<if test="empName != null and empName !=''">
		and emp_name = #{empName}
	</if>
	<if test="age != null and age !=''">
		and age = #{age}
	</if>
	<if test="sex != null and sex !=''">
		and sex = #{sex}
	</if>
	<if test="email != null and email !=''">
		and email = #{email}
	</if>
</select>
```

## `where 标签`

前面只使用if标签时，我们加了一个恒成立条件，但如果配合使用where标签，就不需要加恒成立的条件了。

* 如果`where标签`的内容为空，那么不会在最后的`sql`语句中加`where条件`。

* 若where标签中的if条件满足，则where标签会自动添加where关键字，并将条件最前方多余的`and/or`去掉

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp
	<where>
		<if test="empName != null and empName !=''">
			emp_name = #{empName}
		</if>
		<if test="age != null and age !=''">
			and age = #{age}
		</if>
		<if test="sex != null and sex !=''">
			and sex = #{sex}
		</if>
		<if test="email != null and email !=''">
			and email = #{email}
		</if>
	</where>
</select>
```

## `choose、when、otherwise标签`

作用和if...else if... else一样，选择一个合适的条件执行

```xml
<select id="getEmpByChoose" resultType="Emp">
	select * from t_emp
	<where>
		<choose>
			<when test="empName != null and empName != ''">
				emp_name = #{empName}
			</when>
			<when test="age != null and age != ''">
				age = #{age}
			</when>
			<when test="sex != null and sex != ''">
				sex = #{sex}
			</when>
			<when test="email != null and email != ''">
				email = #{email}
			</when>
			<otherwise>
				did = 1
			</otherwise>
		</choose>
	</where>
</select>
```

## `foreach标签`

* collection：设置要循环的数组或集合
* item：表示数组或集合的每一项数据
* separator：每一项之间的分隔符
* open：设置`foreach`标签中内容的开始符
* close：设置`foreach`标签中内容的结束符

```xml
<!--int deleteMoreByArray(Integer[] eids);-->
<!--批量删除-->
<delete id="deleteMoreByArray">
	delete from t_emp where eid in
	<foreach collection="eids" item="eid" separator="," open="(" close=")">
		#{eid}
	</foreach>
</delete>

<!--int insertMoreByList(@Param("emps") List<Emp> emps);-->
<!--批量新增-->
<insert id="insertMoreByList">
	insert into t_emp values
	<foreach collection="emps" item="emp" separator=",">
		(null,#{emp.empName},#{emp.age},#{emp.sex},#{emp.email},null)
	</foreach>
</insert>

```

## `SQL片段`



