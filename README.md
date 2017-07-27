# 测验项目
> 时间：2017年7月

## 1 知识点
### 1.1 概述
* 命名规范
* 目录规范
* RESTful API设计规范
* Spring Boot/Swagger/Lombok
* 数据库索引
* 代码规范
### 1.2 必需
* 代码简化：Lombok的@Getter/@Setter/@ToString/@Slf4j注解
* API文档：Swagger的@ApiResponses/@ApiResponse/@ApiOperation/@ApiModel/@ApiParam注解

## 2 项目信息
### 2.1 概述
用户和城市的关联。

### 2.2 数据
> 参考[数据文件](http://192.168.0.148:8056/yuanzhen/documents/raw/master/test.sql)，可下载后导入本地PostgreSQL数据库。

#### 2.2.1 城市
##### 2.2.1.1 参考建表语句
```
#新建城市表
CREATE TABLE cities (city_code integer PRIMARY KEY,city_name text NOT NULL);
#新建城市名称索引
CREATE INDEX CONCURRENTLY cities_city_name_index ON cities (city_name);
```

##### 2.2.1.2 数据来源
来自国家统计局网站资料[最新县及县以上行政区划代码（截止2016年7月31日）](http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html)。

#### 2.2.2 用户
##### 2.2.2.1 参考建表语句
```
#新建用户表
CREATE TABLE users(id BIGSERIAL, uuid varchar(36) PRIMARY KEY, user_name varchar(240) NOT NULL, email varchar(50) NOT NULL, password varchar(96) NOT NULL, city_code integer REFERENCES cities (city_code));
#新建UUID索引
CREATE INDEX CONCURRENTLY users_uuid_index ON users USING hash (uuid);
#新建用户名索引
CREATE INDEX CONCURRENTLY users_user_name_index ON users (user_name);
#新建密码索引
CREATE INDEX CONCURRENTLY users_password_index ON users USING hash (password);
```

##### 2.2.2.2 数据来源
无。

### 2.3 对象
#### 2.3.1 用户
##### 2.3.1.1 实体
|属性名|类型|
|:-:|:-:|
|UUID|字符串|
|用户名|字符串|
|电子邮箱|字符串|
|密码|字符串|
|城市|城市对象|

##### 2.3.1.2 DTO
|属性名|类型|
|:-:|:-:|
|用户名|字符串|
|电子邮箱|字符串|
|密码|字符串|
|城市名称|字符串|

##### 2.3.1.3 VO
|属性名|类型|
|:-:|:-:|
|UUID|字符串|
|用户名|字符串|
|电子邮箱|字符串|
|城市名称|字符串|

#### 2.3.2 城市
##### 2.3.2.1 实体
|属性名|类型|
|:-:|:-:|
|编号|数字|
|名称|字符串|

### 2.4 API
> 响应对象统一使用cn.com.zdht.pavilion.message.dosser.DosserReturnBody，将用户对象（或对象列表）使用DosserReturnBodyBuilder#collectionItem方法传入响应对象（对象列表使用DosserReturnBodyBuilder#collection方法），使用DosserReturnBodyBuilder#message方法传递响应描述的信息。

#### 2.4.1 用户
|功能|方法|URL|参数|响应描述|响应类型|curl请求示例|
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|新建|POST|/users|body：用户信息（用户DTO的Json，必须）|若不存在UserDTO中的用户名对应的用户，则新建，响应200：创建用户'{uuid}'成功；若存在，则相应400：创建失败，已有同名用户'{uuid}'|只响应执行状态|curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{"cityName": "天津市", "email": "jay%40gmail.com", "password": "1234sdfghj", "userName": "Jay" }' 'http://localhost:8080/user'|
|删除|DELETE|/users/{uuid}|uuid：用户UUID(路径参数，必须)|若存在路径参数对应的用户，则删除，响应200：删除用户'{uuid}'成功；若不存在该用户名，响应404：删除用户失败，不存在用户'{uuid}'|只响应执行状态|curl -X DELETE --header 'Accept: application/json' 'http://localhost:8005/users/0cdc1980-316d-49ea-9443-7059b2e88469'|
|更新|PUT|/users/{uuid}|uuid：用户UUID(路径参数，必须)；body：用户信息（Json，必须）|若路径参数uuid对应的用户存在且UserDTO中的用户名没有被其他用户注册，则更新，响应200：更新用户'{uuid}'成功；若不存在路径参数用户名，响应404：更新失败，不存在用户'{uuid}；若UserDTO中的用户名已被其他用户注册，响应403：更新失败，已有同名用户'{uuid}'|只响应执行状态|curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"cityName": "天津市", "email": "mike%40gmail.com", "password": "sasasas", "userName": "Mike"}' 'http://localhost:8005/users/6b26a804-2286-4423-b325-bcaeda5fbc7c'|
|获取单个|GET|/users/{uuid}|uuid：用户UUID(路径参数，必须)|若存在用户名对应的用户，响应200：获取用户成功；若不存在用户，响应404：不存在用户'{uuid}'|若存在UUID对应用户，响应该用户VO对象和执行状态；若不存在，只响应执行状态|curl -X GET --header 'Accept: application/json' 'http://localhost:8005/users/6b26a804-2286-4423-b325-bcaeda5fbc7c'|
|获取全部|GET|/user/all|无|获取数据库中的用户列表，若结果数量不为0,则响应200：获取用户列表成功；若结果数量为0,则响应404：用户列表为空|若结果数量不为0，则响应用户VO列表；若为0，只响应执行状态|curl -X GET --header 'Accept: application/json' 'http://localhost:8005/users/all'|
|查询|GET|/users|keyword：关键词（查询参数，必须）；type：关键词类型（查询参数，非必须）|根据提供的关键词类型，若没有关键词类型（type）指定，则查询关键词（keyword）匹配用户名和城市名称；若指定关键词类型，则根据指定的关键词查询；查询后返回匹配的用户列表，若查询结果列表长度为0，相应404：没有匹配的用户；若不为0,则响应200：查询用户成功|若结果数量不为0，则响应用户VO列表；若为0，只响应执行状态|有type参数：curl -X GET --header 'Accept: application/json' 'http://localhost:8005/users?keyword=%E5%A4%A9&type=cityName' 无type参数：curl -X GET --header 'Accept: application/json' 'http://localhost:8005/users?keyword=%E5%A4%A9'|

### 2.5 示例Swagger UI
http://yziyz.xin:8005/swagger-ui.html