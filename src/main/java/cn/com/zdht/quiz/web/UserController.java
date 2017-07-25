package cn.com.zdht.quiz.web;

import cn.com.zdht.quiz.domain.UserRepository;
import cn.com.zdht.quiz.domain.entity.User;
import cn.com.zdht.quiz.service.UserService;
import cn.com.zdht.quiz.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 袁臻
 * 7/25/17
 */
@RestController
@RequestMapping(value = "user")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "list")
    @ApiOperation(value = "获取列表", tags = "用户", httpMethod = "GET", notes = "获取数据库中的用户列表。", response = List.class)
    public ResponseEntity<List<UserVO>> list() {
        List<User> userList = (List<User>)userRepository.findAll();
        List<UserVO> userVOList = UserService.getVOListByEntityList(userList);
        return new ResponseEntity<>(userVOList, HttpStatus.OK);
    }
}
