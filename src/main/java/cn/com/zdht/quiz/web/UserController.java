package cn.com.zdht.quiz.web;

import cn.com.zdht.pavilion.message.dosser.DosserReturnBody;
import cn.com.zdht.pavilion.message.dosser.DosserReturnBodyBuilder;
import cn.com.zdht.quiz.domain.CityRepository;
import cn.com.zdht.quiz.domain.UserRepository;
import cn.com.zdht.quiz.domain.entity.User;
import cn.com.zdht.quiz.dto.UserDTO;
import cn.com.zdht.quiz.service.UserService;
import cn.com.zdht.quiz.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author 袁臻
 * 7/25/17
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @GetMapping(value = "list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取用户列表成功", response = UserVO.class),
            @ApiResponse(code = 404, message = "用户列表为空", response = DosserReturnBody.class)})
    @ApiOperation(value = "获取列表",
            tags = "用户",
            response = UserVO.class,
            notes = "获取数据库中的用户列表。")
    public DosserReturnBody list() {
        /*
        curl -X GET --header 'Accept: application/json' 'http://localhost:8080/user/list'
         */
        List<User> userList = (List<User>) userRepository.findAll();
        //判断列表是否长度为0
        if (userList.size() == 0) {
            //长度为0
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message("用户列表为空")
                    .build();
        } else {
            //长度不为0
            List<UserVO> userVOList = UserService.getVOListByEntityList(userList);
            return new DosserReturnBodyBuilder()
                    .collection(userVOList)
                    .statusOk()
                    .message("获取用户列表成功")
                    .build();
        }
    }

    @PostMapping
    @ApiOperation(value = "新建",
            tags = "用户",
            response = String.class,
            notes = "若不存在UserDTO中的用户名对应的用户，则新建，响应200：创建用户'{username}'成功；若存在，则相应400：创建失败，已有同名用户'{username}'")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "新建用户成功", response = String.class),
            @ApiResponse(code = 400, message = "新建用户失败，已存在该用户", response = DosserReturnBody.class)})
    public DosserReturnBody create(@ApiParam(value = "用户DTO", required = true) @RequestBody final UserDTO userDTO) {
        /*
        curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{"cityName": "天津市", "email": "jay%40gmail.com", "password": "1234sdfghj", "userName": "Jay" }' 'http://localhost:8080/user'
         */
        log.info(String.format("请求新建：%s", userDTO.toString()));
        //检验用户名是否存在
        if (userRepository.existByUserName(userDTO.getUserName())) {
            //存在，返回信息
            return new DosserReturnBodyBuilder()
                    .statusBadRequest()
                    .message(String.format("新建用户失败，已有同名用户'%s'", userDTO.getUserName()))
                    .build();
        } else {
            //不存在，保存到数据库中
            User user = new User(userDTO, cityRepository.getByCityName(userDTO.getCityName()));
            userRepository.save(user);
            return new DosserReturnBodyBuilder()
                    .statusCreated()
                    .message(String.format("新建用户'%s'成功", userDTO.getUserName()))
                    .build();
        }
    }

    @DeleteMapping(value = "{userName}")
    @ApiOperation(value = "删除",
            tags = "用户",
            response = String.class,
            notes = "若存在路径参数对应的用户，则删除，响应200：删除用户'{username}'成功；若不存在该用户名，响应404：删除用户失败，不存在用户'{username}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除用户成功", response = String.class),
            @ApiResponse(code = 404, message = "删除用户失败，不存在该用户", response = DosserReturnBody.class)})
    public DosserReturnBody delete(@ApiParam(value = "需要删除的用户的用户名", required = true) @PathVariable("userName") final String userName) {
        /*
        curl -X DELETE --header 'Accept: application/json' 'http://localhost:8080/user/Mike'
         */
        log.info(String.format("请求删除：%s", userName));
        //检验用户名是否存在
        if (userRepository.existByUserName(userName)) {
            //存在
            userRepository.deleteByUserName(userName);
            return new DosserReturnBodyBuilder()
                    .statusOk()
                    .message(String.format("删除用户'%s'成功", userName))
                    .build();
        } else {
            //不存在
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message(String.format("删除用户失败，不存在用户'%s'", userName))
                    .build();
        }
    }

    @PutMapping(value = "{userName}")
    @ApiOperation(value = "更新",
            tags = "用户",
            response = String.class,
            notes = "若路径参数用户名对应的用户存在且UserDTO中的用户名没有被其他用户注册，则更新，响应200：更新用户'{username}'成功；若不存在路径参数用户名，响应404：更新失败，不存在用户'{username}；若UserDTO中的用户名已被其他用户注册，响应403：更新失败，已有同名用户'{username}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新用户成功", response = String.class),
            @ApiResponse(code = 404, message = "更新失败，不存在该用户", response = DosserReturnBody.class),
            @ApiResponse(code = 403, message = "更新失败，已有同名用户", response = DosserReturnBody.class)})
    public DosserReturnBody update(@ApiParam(value = "需要更新的用户的用户名", required = true) @PathVariable("userName") final String userName,
                                   @ApiParam(value = "目的用户DTO", required = true) @RequestBody final UserDTO userDTO) {
        /*
        curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"cityName": "天津市", "email": "mike%40gmail.com", "password": "1234sdfghj", "userName": "Mike"}' 'http://localhost:8080/user/Mike'
         */
        log.info(String.format("请求更新：%s", userName));
        //检验用户名是否存在
        if (userRepository.existByUserName(userName)) {
            //检测目的用户名（userDTO中的userName）是否存在
            if (userRepository.existByUserNameAndExcludedName(userDTO.getUserName(), userName)) {
                //已存在
                return new DosserReturnBodyBuilder()
                        .statusUnAuthorized()
                        .message(String.format("更新失败，已有同名用户'%s'", userDTO.getUserName()))
                        .build();
            }
            //目的用户名不存在
            User user = userRepository.getByUserName(userName);
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            //比较user和userDTO的城市是否相同
            if (!Objects.equals(user.getCity().getCityName(), userDTO.getCityName())) {
                //不同，则以userDTO的城市覆盖user的城市
                user.setCity(cityRepository.getByCityName(userDTO.getCityName()));
            }
            userRepository.save(user);
            return new DosserReturnBodyBuilder()
                    .statusOk()
                    .message(String.format("更新用户'%s'成功", userName))
                    .build();
        }
        //不存在用户名
        return new DosserReturnBodyBuilder()
                .statusNotFound()
                .message(String.format("不存在用户'%s'", userName))
                .build();
    }

    @GetMapping(value = "{userName}")
    @ApiOperation(value = "获取",
            tags = "用户",
            response = UserVO.class,
            notes = "若存在用户名对应的用户，响应200：获取用户成功；若不存在用户，响应404：不存在用户'{username}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取用户成功", response = UserVO.class),
            @ApiResponse(code = 404, message = "获取用户失败，不存在用户", response = DosserReturnBody.class)})
    public DosserReturnBody get(@ApiParam(value = "需要获取的用户的用户名", required = true) @PathVariable("userName") final String userName) {
        /*
        curl -X GET --header 'Accept: application/json' 'http://localhost:8080/user/Mike'
         */
        log.info(String.format("请求获取：%s", userName));
        //检验用户名是否存在
        if (userRepository.existByUserName(userName)) {
            //存在
            return new DosserReturnBodyBuilder()
                    .collectionItem(new UserVO(userRepository.getByUserName(userName)))
                    .statusOk()
                    .message(String.format("获取用户'%s'成功", userName))
                    .build();
        } else {
            //不存在
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message(String.format("不存在用户'%s'", userName))
                    .build();
        }
    }

    @GetMapping(value = "search")
    @ApiOperation(value = "查询",
            tags = "用户",
            response = UserVO.class,
            notes = "根据提供的关键词，返回用户名中含有该关键词的用户列表，若查询结果列表长度为0，相应404：没有匹配的用户；若不为0,则响应200：查询用户成功")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询用户成功", response = UserVO.class),
            @ApiResponse(code = 404, message = "没有匹配的用户", response = String.class)})
    public DosserReturnBody search(@ApiParam(value = "需要查询的关键词", required = true) @RequestParam("keyword") final String keyword) {
        /*
        curl -X GET --header 'Accept: application/json' 'http://localhost:8080/user/search?keyword=i'
         */
        List<User> userList = userRepository.searchByUserName(keyword);
        //判断查询结果列表长度是否为0
        if (userList.size() == 0) {
            //长度为0
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message("没有匹配的用户")
                    .build();
        } else {
            //长度不为0
            List<UserVO> userVOList = UserService.getVOListByEntityList(userList);
            return new DosserReturnBodyBuilder()
                    .collection(userVOList)
                    .statusOk()
                    .message("查询用户成功")
                    .build();
        }
    }
}
