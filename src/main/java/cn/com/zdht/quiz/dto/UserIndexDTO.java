package cn.com.zdht.quiz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 袁臻
 * 2017/07/28
 */
@Getter
@Setter
@ToString
@ApiModel
public class UserIndexDTO {

    @ApiModelProperty(value = "页码", dataType = "Integer", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "最大结果数量", dataType = "Integer", required = true, example = "10")
    private Integer size;

    @ApiModelProperty(value = "城市名称", dataType = "String", example = "北京")
    private String cityName;

    @ApiModelProperty(value = "用户名", dataType = "String", example = "Mike")
    private String userName;
}
