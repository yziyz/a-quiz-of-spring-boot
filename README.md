# 测验项目
> 2017年7月

## 1 知识点
* 命名规范
* 目录规范
* RESTful API设计规范
* SpringBoot/Swagger/Lombok
* 数据库索引

## 2 项目信息
### 2.1 概述
用户和城市的关联。

### 2.2 实体
#### 2.2.1 用户
|属性名|类型|键|
|:-:|:-:|:-:|
|编号|数字|主键|
|用户名|字符串||
|电子邮箱|字符串||
|密码|字符串||
|城市编号|数字|外键|

#### 2.2.2 城市
|属性名|类型|键|
|:-:|:-:|:-:|
|编号|数字|主键|
|名称|字符串||

### 2.3 API
#### 2.3.1 用户
|功能|方法|URL|参数|响应|示例|
|:-:|:-:|:-:|:-:|:-:|:-:|
|新建用户|POST|/user|body：用户信息Json|若不存在UserDTO中的用户名对应的用户，则新建，响应200：创建用户'{userName}'成功；若存在，则相应400：创建失败，已有同名用户'{userName}'|curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{"cityName": "天津市", "email": "jay%40gmail.com", "password": "1234sdfghj", "userName": "Jay" }' 'http://yziyz.xin:8005/user'|
|删除用户|DELETE|/user/{userName}：用户名|userName(路径参数)|若存在路径参数对应的用户，则删除，响应200：删除用户'{userName}'成功；若不存在该用户名，响应404：删除用户失败，不存在用户'{userName}'|curl -X DELETE --header 'Accept: application/json' 'http://yziyz.xin:8005/user/Mike'|
|更新用户|PUT|/user/{userName}|userName：用户名(路径参数)/body：用户信息|若路径参数用户名对应的用户存在且UserDTO中的用户名没有被其他用户注册，则更新，响应200：更新用户'{userName}'成功；若不存在路径参数用户名，响应404：更新失败，不存在用户'{userName}；若UserDTO中的用户名已被其他用户注册，响应403：更新失败，已有同名用户'{userName}'|curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"cityName": "天津市", "email": "mike%40gmail.com", "password": "1234sdfghj", "userName": "Mike"}' 'http://yziyz.xin:8005/user/Mike'|
|获取用户|GET|/user/{userName}|userName：用户名(路径参数)|若存在用户名对应的用户，响应200：获取用户成功；若不存在用户，响应404：不存在用户'{userName}'|curl -X GET --header 'Accept: application/json' 'http://yziyz.xin:8005/user/Mike'|
|以用户名查询用户|GET|/user/search|keyword：关键词（查询参数）|根据提供的关键词，返回用户名中含有该关键词的用户列表，若查询结果列表长度为0，相应404：没有匹配的用户；若不为0,则响应200：查询用户成功|curl -X GET --header 'Accept: application/json' 'http://yziyz.xin:8005/user/search?keyword=i'|

### 2.4 数据
> 参考[数据文件](http://192.168.0.148:8056/yuanzhen/documents/raw/master/test.sql)，可下载后导入本地PostgreSQL数据库。

#### 2.4.1 城市
##### 2.4.1.1 参考建表语句
```
CREATE TABLE cities (city_code integer PRIMARY KEY,city_name text NOT NULL);
```
##### 2.4.1.2 数据来源
http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html

#### 2.4.2 用户
##### 2.4.2.1 参考建表语句
```
CREATE TABLE users(id integer PRIMARY KEY, user_name text UNIQUE NOT NULL,email text NOT NULL, password text NOT NULL, city_code integer REFERENCES cities (city_code));
```

##### 2.4.2.2 数据来源
用户名随机。

### 2.5 示例链接
http://yziyz.xin:8005/swagger-ui.html