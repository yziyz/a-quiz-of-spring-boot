package zenuo.demo.springboot.service;

import org.springframework.beans.BeanUtils;
import zenuo.demo.springboot.domain.entity.User;
import zenuo.demo.springboot.vo.UserVO;

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
