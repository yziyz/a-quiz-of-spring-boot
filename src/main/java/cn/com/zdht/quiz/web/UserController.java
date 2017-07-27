package cn.com.zdht.quiz.web;

import cn.com.zdht.pavilion.message.dosser.DosserReturnBody;
import cn.com.zdht.pavilion.message.dosser.DosserReturnBodyBuilder;
import cn.com.zdht.quiz.common.constant.UserControllerConstant;
import cn.com.zdht.quiz.common.util.HashUtil;
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

import java.util.*;

/**
 * @author 袁臻
 * 7/25/17
 */
@RestController
@RequestMapping(UserControllerConstant.URL)
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @PostMapping
    @ApiOperation(value = "新建",
            tags = "用户",
            response = String.class,
            notes = "若不存在UserDTO中的用户名对应的用户且存在UserDTO的城市名称对应的城市，则新建，响应200：创建用户'{uuid}'成功；若存在，则相应400：创建失败，已有同名用户'{username}；若不存在城市，响应404：新建用户'{username}'失败，不存在城市'{cityname}'")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "新建用户成功", response = UserVO.class),
            @ApiResponse(code = 400, message = "新建用户失败，已有同名用户", response = DosserReturnBody.class),
            @ApiResponse(code = 404, message = "新建用户失败，不存在城市", response = DosserReturnBody.class)})
    public DosserReturnBody create(@ApiParam(value = "用户DTO", required = true) @RequestBody final UserDTO userDTO) {
        log.info(String.format("请求新建：%s", userDTO.getUserName()));
        //检验用户名是否存在
        if (userRepository.existByUserName(userDTO.getUserName())) {
            //存在，返回信息
            return new DosserReturnBodyBuilder()
                    .statusBadRequest()
                    .message(String.format("新建用户失败，已有同名用户'%s'", userDTO.getUserName()))
                    .build();
        } else {
            //不存在
            //检验用户DTO中城市是否存在
            if (cityRepository.existByCityName(userDTO.getCityName())) {
                //存在该城市，保存到数据库中
                User user = new User(userDTO, cityRepository.getByCityName(userDTO.getCityName()));
                userRepository.save(user);
                return new DosserReturnBodyBuilder()
                        .collectionItem(new UserVO(user))
                        .statusCreated()
                        .message(String.format("新建用户'%s'成功", user.getUuid()))
                        .build();
            } else {
                //不存在该城市
                return new DosserReturnBodyBuilder()
                        .statusBadRequest()
                        .message(String.format("新建用户'%s'失败，不存在城市'%s'", userDTO.getUserName(), userDTO.getCityName()))
                        .build();
            }
        }
    }

    @DeleteMapping(value = "{uuid}")
    @ApiOperation(value = "删除",
            tags = "用户",
            response = String.class,
            notes = "若存在路径参数对应的用户，则删除，响应200：删除用户'{uuid}'成功；若不存在该用户名，响应404：删除用户失败，不存在用户'{uuid}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除用户成功"),
            @ApiResponse(code = 404, message = "删除用户失败，不存在该用户", response = DosserReturnBody.class)})
    public DosserReturnBody delete(@ApiParam(value = "需要删除的用户的UUID", required = true)
                                   @PathVariable("uuid") final String uuid) {
        log.info(String.format("请求删除：%s", uuid));
        //检验用户名是否存在
        if (userRepository.existByUuid(uuid)) {
            //存在
            userRepository.deleteByUuid(uuid);
            return new DosserReturnBodyBuilder()
                    .statusOk()
                    .message(String.format("删除用户'%s'成功", uuid))
                    .build();
        } else {
            //不存在
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message(String.format("删除用户失败，不存在用户'%s'", uuid))
                    .build();
        }
    }

    @PutMapping(value = "{uuid}")
    @ApiOperation(value = "更新",
            tags = "用户",
            response = String.class,
            notes = "若路径参数uuid对应的用户存在且UserDTO中的用户名没有被其他用户注册，则更新，响应200：更新用户'{uuid}'成功；若不存在路径参数用户名，响应404：更新失败，不存在用户'{uuid}；若UserDTO中的用户名已被其他用户注册，响应403：更新失败，已有同名用户'{uuid}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新用户成功", response = String.class),
            @ApiResponse(code = 404, message = "更新失败，不存在该用户", response = DosserReturnBody.class),
            @ApiResponse(code = 403, message = "更新失败，已有同名用户", response = DosserReturnBody.class)})
    public DosserReturnBody update(@ApiParam(value = "需要更新的用户的UUID", required = true) @PathVariable("uuid") final String uuid,
                                   @ApiParam(value = "目的用户DTO", required = true) @RequestBody final UserDTO userDTO) {
        log.info(String.format("请求更新：%s", uuid));
        //检验用户名是否存在
        if (userRepository.existByUuid(uuid)) {
            //检测目的用户名（userDTO中的userName）是否存在
            if (userRepository.existByUserNameAndExcludedUuid(userDTO.getUserName(), uuid)) {
                //已存在
                return new DosserReturnBodyBuilder()
                        .statusUnAuthorized()
                        .message(String.format("更新失败，已有同名用户'%s'", userDTO.getUserName()))
                        .build();
            }
            //目的用户名不存在
            User user = userRepository.getByUuid(uuid);
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(HashUtil.hashSha384(userDTO.getPassword()));
            //比较user和userDTO的城市是否相同
            if (!Objects.equals(user.getCity().getCityName(), userDTO.getCityName())) {
                //不同，则以userDTO的城市覆盖user的城市
                user.setCity(cityRepository.getByCityName(userDTO.getCityName()));
            }
            userRepository.save(user);
            return new DosserReturnBodyBuilder()
                    .collectionItem(new UserVO(user))
                    .statusOk()
                    .message(String.format("更新用户'%s'成功", uuid))
                    .build();
        }
        //不存在用户名
        return new DosserReturnBodyBuilder()
                .statusNotFound()
                .message(String.format("不存在用户'%s'", uuid))
                .build();
    }

    @GetMapping(value = "{uuid}")
    @ApiOperation(value = "获取单个",
            tags = "用户",
            response = UserVO.class,
            notes = "若存在用户名对应的用户，响应200：获取用户成功；若不存在用户，响应404：不存在用户'{uuid}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取用户成功", response = UserVO.class),
            @ApiResponse(code = 404, message = "获取用户失败，不存在用户", response = DosserReturnBody.class)})
    public DosserReturnBody get(@ApiParam(value = "需要获取的用户的UUID", required = true) @PathVariable("uuid") final String uuid) {
        /*
        curl -X GET --header 'Accept: application/json' 'http://localhost:8005/users/6b26a804-2286-4423-b325-bcaeda5fbc7c'
         */
        log.info(String.format("请求获取：%s", uuid));
        //检验UUID对应的用户是否存在
        if (userRepository.existByUuid(uuid)) {
            //存在
            return new DosserReturnBodyBuilder()
                    .collectionItem(new UserVO(userRepository.getByUuid(uuid)))
                    .statusOk()
                    .message(String.format("获取用户'%s'成功", uuid))
                    .build();
        } else {
            //不存在
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message(String.format("不存在用户'%s'", uuid))
                    .build();
        }
    }

    @GetMapping
    @ApiOperation(value = "查询",
            tags = "用户",
            response = UserVO.class,
            notes = "根据提供的关键词类型，若两个参数否都为空，响应200：查询用户成功；若没有关键词类型（type）指定，则查询关键词（keyword）匹配用户名和城市名称；若指定关键词类型，则根据指定的关键词查询；查询后返回匹配的用户列表，若查询结果列表长度为0，相应404：没有匹配的用户；若不为0,则响应200：查询用户成功")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询用户成功", response = UserVO.class),
            @ApiResponse(code = 404, message = "没有匹配的用户", response = String.class)})
    public DosserReturnBody search(@ApiParam(value = "需要查询的关键词")
                                   @RequestParam(value = "keyword", required = false) final String keyword,
                                   @ApiParam(value = "关键词类型", allowableValues = "cityName, userName")
                                   @RequestParam(value = "type", required = false) final String type) {
        log.info(String.format("请求查询，关键词'%s'，类型'%s'", keyword, type));
        //因为结果需要排重，所以使用Set存放查询结果
        Set<User> userSet = new HashSet<>(0);
        //判断关键词是否为空
        if (Objects.equals(null, keyword)) {
            //关键词为空，返回所有用户
            userSet.addAll((List<User>) userRepository.findAll());
        } else {
            //关键词不为空，判断关键词类型是否为空
            if (Objects.equals(null, type)) {
                //关键词为空，则使用关键词在用户名和城市名中检索
                userSet.addAll(userRepository.searchByCityName(keyword));
                userSet.addAll(userRepository.searchByUserName(keyword));
            } else {
                //关键词不为空
                if (Objects.equals(UserControllerConstant.CITY_NAME, type)) {
                    //关键词为城市名的情况
                    userSet.addAll(userRepository.searchByCityName(keyword));
                } else if (Objects.equals(UserControllerConstant.USER_NAME, type)) {
                    //关键词类型为用户名的情况
                    userSet.addAll(userRepository.searchByUserName(keyword));
                } else {
                    //关键词类型不是用户名和城市名的情况
                    return new DosserReturnBodyBuilder()
                            .statusNotFound()
                            .message("类型参数错误，可选项为\"cityName\"或\"userName\"")
                            .build();
                }
            }
        }

        //判断查询结果列表长度是否为0
        if (userSet.size() == 0) {
            //长度为0
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message("没有匹配的用户")
                    .build();
        } else {
            //长度不为0
            List<User> userList = new ArrayList<>(userSet);
            List<UserVO> userVOList = UserService.getVOListByEntityList(userList);
            return new DosserReturnBodyBuilder()
                    .collection(userVOList)
                    .statusOk()
                    .message("查询用户成功")
                    .build();
        }
    }
}
