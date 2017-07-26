package cn.com.zdht.quiz.vo;

import cn.com.zdht.quiz.domain.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
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
    @ApiModelProperty(value = "用户名", dataType = "字符串", required = true, example = "Mike")
    @NotEmpty
    @Length(min = 3, max = 10)
    private String userName;

    @ApiModelProperty(value = "电子邮件", dataType = "字符串", required = true, example = "mike@gmail.com")
    @NotEmpty
    @Length(min = 7, max = 50)
    private String email;

    @ApiModelProperty(value = "城市名称", dataType = "字符串", required = true, example = "北京")
    @NotEmpty
    @Length(min = 2, max = 10)
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
