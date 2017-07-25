package cn.com.zdht.quiz.service;

import cn.com.zdht.quiz.domain.entity.User;
import cn.com.zdht.quiz.vo.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 袁臻
 * 7/25/17
 */
public class UserService {
    /**
     * 根据实体对象列表获取VO对象列表
     * @param source 实体对象列表
     * @return VO对象列表
     */
    public static List<UserVO> getVOListByEntityList(final List<User> source) {
        List<UserVO> target = new ArrayList<>(source.size());
        for (User user : source) {
            UserVO userVO = new UserVO();
            userVO.setCityName(user.getCity().getCityName());
            BeanUtils.copyProperties(user, userVO);
            target.add(userVO);
        }
        return target;
    }
}
