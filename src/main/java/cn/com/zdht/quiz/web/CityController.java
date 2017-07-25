package cn.com.zdht.quiz.web;

import cn.com.zdht.quiz.domain.CityRepository;
import cn.com.zdht.quiz.domain.entity.City;
import cn.com.zdht.quiz.dto.CityDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 袁臻
 * 7/25/17
 */
@RestController
@RequestMapping(value = "city")
@Slf4j
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @GetMapping(value = "list")
    @ApiOperation(value = "获取列表", tags = "城市", httpMethod = "GET", notes = "获取数据库中的城市列表。", response = List.class)
    public ResponseEntity<List<City>> list() {
        return new ResponseEntity<>((List<City>) cityRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "新增", tags = "城市", httpMethod = "POST", notes = "若提交的城市不存在，则保存到数据库中；若已经存在，则不保存。", response = String.class)
    public ResponseEntity<String> create(@RequestBody final CityDTO cityDTO) {
        /*
        curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{"cityCode": 140100,"cityName": "太原市"}' 'http://localhost:8080/city'
         */
        log.info(String.format("新建：%d %s", cityDTO.getCityCode(), cityDTO.getCityName()));
        if (!cityRepository.exists(cityDTO.getCityCode())) {
            City city = new City(cityDTO);
            cityRepository.save(city);
            return new ResponseEntity<>(String.format("创建城市'%s'成功", cityDTO.getCityName()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("创建失败，已有同名城市'%s'", cityDTO.getCityName()), HttpStatus.BAD_REQUEST);
        }
    }
}
