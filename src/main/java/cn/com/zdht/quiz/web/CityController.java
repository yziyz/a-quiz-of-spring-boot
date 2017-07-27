package cn.com.zdht.quiz.web;

import cn.com.zdht.pavilion.message.dosser.DosserReturnBody;
import cn.com.zdht.pavilion.message.dosser.DosserReturnBodyBuilder;
import cn.com.zdht.quiz.common.constant.CityControllerConstant;
import cn.com.zdht.quiz.domain.CityRepository;
import cn.com.zdht.quiz.domain.entity.City;
import cn.com.zdht.quiz.dto.CityDTO;
import cn.com.zdht.quiz.service.CityService;
import cn.com.zdht.quiz.vo.CityVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取城市列表成功", response = CityVO.class),
            @ApiResponse(code = 404, message = "城市列表为空", response = DosserReturnBody.class)})
    @ApiOperation(value = "获取列表",
            response = CityVO.class,
            tags = "城市",
            notes = "获取数据库中的城市列表，若列表不为空，响应200：获取城市列表成功；若列表为空，响应404：城市列表为空")
    public DosserReturnBody list() {
        /*
        curl -X GET --header 'Accept: application/json' 'http://localhost:8005/cities/list'
         */
        log.info("请求获取列表");
        List<City> cityList = (List<City>) cityRepository.findAll();

        //判断列表是否为0
        if (cityList.size() == 0) {
            return new DosserReturnBodyBuilder()
                    .statusNotFound()
                    .message("城市列表为空")
                    .build();
        } else {
            List<CityVO> cityVOList = CityService.getVOListByEntityList(cityList);
            return new DosserReturnBodyBuilder()
                    .statusOk()
                    .message("获取城市列表成功")
                    .collection(cityVOList)
                    .build();
        }
    }

    @PostMapping()
    @ApiResponses(value = {@ApiResponse(code = 201, message = "新建城市成功", response = String.class),
            @ApiResponse(code = 400, message = "新建失败，已有同名城市", response = DosserReturnBody.class)})
    @ApiOperation(value = "新增",
            response = String.class,
            tags = "城市",
            notes = "若提交的城市不存在，则保存到数据库中；若已经存在，则不保存。")
    public DosserReturnBody create(@ApiParam(value = "城市DTO") @RequestBody final CityDTO cityDTO) {
        /*
        curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"cityCode": 110000, "cityName": "北京市"}' 'http://localhost:8005/cities'
         */
        log.info(String.format("请求新建：%d %s", cityDTO.getCityCode(), cityDTO.getCityName()));
        if (!cityRepository.exists(cityDTO.getCityCode())) {
            City city = new City(cityDTO);
            cityRepository.save(city);
            return new DosserReturnBodyBuilder()
                    .statusOk()
                    .message(String.format("新建城市'%s'成功", cityDTO.getCityName()))
                    .build();
        } else {
            return new DosserReturnBodyBuilder()
                    .statusBadRequest()
                    .message(String.format("新建失败，已有同名城市'%s'", cityDTO.getCityName()))
                    .build();
        }
    }
}
