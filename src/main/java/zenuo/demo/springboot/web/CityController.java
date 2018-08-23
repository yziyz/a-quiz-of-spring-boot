package zenuo.demo.springboot.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zenuo.demo.springboot.common.constant.CityControllerConstant;
import zenuo.demo.springboot.domain.CityRepository;
import zenuo.demo.springboot.domain.entity.City;
import zenuo.demo.springboot.dto.CityDTO;
import zenuo.demo.springboot.vo.CityVO;

import java.util.List;

/**
 * @author 袁臻
 * 7/25/17
 */
@RestController
@RequestMapping(value = CityControllerConstant.URL)
@Slf4j
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @GetMapping
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取城市列表成功", response = CityVO.class),
            @ApiResponse(code = 404, message = "城市列表为空", response = List.class)})
    @ApiOperation(value = "获取列表",
            response = CityVO.class,
            tags = "城市",
            notes = "获取数据库中的城市列表，若列表不为空，响应200：获取城市列表成功；若列表为空，响应404：城市列表为空")
    public List<City> list() {
        log.info("请求获取列表");
        return (List<City>) cityRepository.findAll();
    }

    @PostMapping()
    @ApiResponses(value = {@ApiResponse(code = 201, message = "新建城市成功", response = CityVO.class),
            @ApiResponse(code = 400, message = "新建失败，已有同名城市", response = City.class)})
    @ApiOperation(value = "新增",
            response = String.class,
            tags = "城市",
            notes = "若提交的城市不存在，则保存到数据库中；若已经存在，则不保存。")
    public City create(@ApiParam(value = "城市DTO") @RequestBody final CityDTO cityDTO) {
        log.info(String.format("请求新建：%d %s", cityDTO.getCityCode(), cityDTO.getCityName()));
        City city = new City(cityDTO);
        cityRepository.save(city);
        return city;
    }
}
