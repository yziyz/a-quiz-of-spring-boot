package cn.com.zdht.quiz.vo;

import cn.com.zdht.quiz.domain.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
@ToString
@ApiModel
public class UserVO {
    private String userName;
    private String email;
    private String cityName;

    /**
     * 空构造方法
     */
    public UserVO() {
    }

    /**
     * 由User对象构造
     *
     * @param user User对象
     */
    public UserVO(final User user) {
        BeanUtils.copyProperties(user, this);
        this.cityName = user.getCity().getCityName();
    }
}
