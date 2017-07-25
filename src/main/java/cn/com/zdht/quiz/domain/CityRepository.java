package cn.com.zdht.quiz.domain;

import cn.com.zdht.quiz.domain.entity.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author 袁臻
 * 7/25/17
 */
public interface CityRepository extends CrudRepository<City, Integer> {
    @Query(value = "SELECT * FROM cities WHERE city_name = :cityName LIMIT 1;" ,nativeQuery = true)
    City getByCityName(@Param("cityName") final String cityName);
}
