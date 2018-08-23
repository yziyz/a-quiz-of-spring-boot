package zenuo.demo.springboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import zenuo.demo.springboot.domain.entity.City;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
@ToString
@ApiModel
public class CityVO {
    @ApiModelProperty(value = "城市编号", dataType = "数字", required = true, example = "110000", reference = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html")
    private Integer cityCode;

    @ApiModelProperty(value = "城市名称", dataType = "字符串", required = true, example = "北京市", reference = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html")
    @Length(min = 2, max = 10)
    private String cityName;

    /**
     * 空构造方法
     */
    public CityVO() {
    }

    /**
     * 由实体类构造
     * @param city 一个实体类的对象
     */
    public CityVO(final City city) {
        this.cityCode = city.getCityCode();
        this.cityName = city.getCityName();
    }
}
