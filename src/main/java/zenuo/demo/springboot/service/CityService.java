package zenuo.demo.springboot.service;

import zenuo.demo.springboot.domain.entity.City;
import zenuo.demo.springboot.vo.CityVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 袁臻
 * 7/26/17
 */
public class CityService {
    public static List<CityVO> getVOListByEntityList(final List<City> source) {
        List<CityVO> target = new ArrayList<>(source.size());
        for (City city : source) {
            target.add(new CityVO(city));
        }
        return target;
    }
}
