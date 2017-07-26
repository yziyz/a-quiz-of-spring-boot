package cn.com.zdht.quiz.vo;

import cn.com.zdht.quiz.domain.entity.City;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
public class CityVO {
    private Integer cityCode;

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
