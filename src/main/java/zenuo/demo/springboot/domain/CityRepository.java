package zenuo.demo.springboot.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import zenuo.demo.springboot.domain.entity.City;

/**
 * @author 袁臻
 * 7/25/17
 */
public interface CityRepository extends CrudRepository<City, Integer> {
    /**
     * 获取指定城市名称对应的城市对象
     *
     * @param cityName 获取指定城市名称
     * @return 指定城市名称对应的城市对象
     */
    City getByCityName(final String cityName);

    /**
     * 查询指定的城市名称对应的城市是否存在
     *
     * @param cityName 指定的城市名称
     * @return 指定的城市名称对应的城市是否存在
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM cities WHERE city_name = :cityName LIMIT 1)", nativeQuery = true)
    boolean existByCityName(@Param("cityName") final String cityName);
}
