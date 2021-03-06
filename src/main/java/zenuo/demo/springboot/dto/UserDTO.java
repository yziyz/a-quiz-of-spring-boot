package zenuo.demo.springboot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
@ToString
@ApiModel
public class UserDTO {
    @ApiModelProperty(value = "用户名", dataType = "字符串", required = true, example = "Mike", notes = "不能与已存在的用户名相同")
    @Length(min = 3, max = 240)
    private String userName;

    @ApiModelProperty(value = "电子邮箱", dataType = "字符串", required = true, example = "mike@gmail.com")
    @Length(min = 7, max = 50)
    private String email;

    @ApiModelProperty(value = "密码", dataType = "字符串", required = true, example = "1234sdfghj", notes = "尽量增加密码的复杂度以提高安全性")
    @Length(min = 5, max = 20)
    private String password;

    @ApiModelProperty(value = "城市名称", dataType = "字符串", required = true, example = "天津市", notes = "城市名称需要真实存在")
    @Length(min = 2, max = 10)
    private String cityName;
}
