package zenuo.demo.springboot.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zenuo.demo.springboot.common.constant.UserControllerConstant;
import zenuo.demo.springboot.domain.CityRepository;
import zenuo.demo.springboot.domain.UserRepository;
import zenuo.demo.springboot.domain.entity.User;
import zenuo.demo.springboot.dto.UserDTO;
import zenuo.demo.springboot.vo.UserVO;

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
            @ApiResponse(code = 400, message = "新建用户失败，已有同名用户", response = String.class),
            @ApiResponse(code = 404, message = "新建用户失败，不存在城市", response = String.class)})
    public User create(@ApiParam(value = "用户DTO", required = true) @RequestBody final UserDTO userDTO) {
        log.info(String.format("请求新建：%s", userDTO.getUserName()));
        User user = new User(userDTO, cityRepository.getByCityName(userDTO.getCityName()));
        userRepository.save(user);
        return user;
    }

    @DeleteMapping(value = "{uuid}")
    @ApiOperation(value = "删除",
            tags = "用户",
            response = String.class,
            notes = "若存在路径参数对应的用户，则删除，响应200：删除用户'{uuid}'成功；若不存在该用户名，响应404：删除用户失败，不存在用户'{uuid}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除用户成功"),
            @ApiResponse(code = 404, message = "删除用户失败，不存在该用户", response = Boolean.class)})
    public boolean delete(@ApiParam(value = "需要删除的用户的UUID", required = true)
                                   @PathVariable("uuid") final String uuid) {
        log.info(String.format("请求删除：%s", uuid));
        //检验用户名是否存在
        if (userRepository.existByUuid(uuid)) {
            //存在
            userRepository.deleteByUuid(uuid);
            return true;
        } else {
            //不存在
            return false;
        }
    }

    @GetMapping(value = "{uuid}")
    @ApiOperation(value = "获取单个",
            tags = "用户",
            response = UserVO.class,
            notes = "若存在用户名对应的用户，响应200：获取用户成功；若不存在用户，响应404：不存在用户'{uuid}'")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取用户成功", response = UserVO.class)})
    public UserVO get(@ApiParam(value = "需要获取的用户的UUID", required = true) @PathVariable("uuid") final String uuid) {
        log.info(String.format("请求获取：%s", uuid));
        //检验UUID对应的用户是否存在
        if (userRepository.existByUuid(uuid)) {
            //存在
            return new UserVO(userRepository.getByUuid(uuid));
        } else {
            //不存在
            return null;
        }
    }
}
