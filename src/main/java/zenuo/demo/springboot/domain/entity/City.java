package zenuo.demo.springboot.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zenuo.demo.springboot.dto.CityDTO;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author 袁臻
 * 7/25/17
 */
@Getter
@Setter
@ToString
@Entity
@Embeddable
@Table(name = "cities", indexes = {@Index(name = "city_name_index", columnList = "city_name")})
public class City {
    @Id//http://viralpatel.net/blogs/org-hibernate-annotationexception-no-identifier-specified/
    @Column(name = "city_code", unique = true, nullable = false)
    private Integer cityCode;

    @Column(name = "city_name", unique = true, nullable = false)
    private String cityName;

    /**
     * 空构造方法
     */
    public City() {
    }

    /**
     * 由CityDTO构造City对象的方法
     * @param cityDTO 一个CityDTO对象
     */
    public City(final CityDTO cityDTO) {
        this.cityCode = cityDTO.getCityCode();
        this.cityName = cityDTO.getCityName();
    }
}
