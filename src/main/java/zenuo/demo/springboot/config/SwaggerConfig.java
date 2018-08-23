package zenuo.demo.springboot.config;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 袁臻
 * 7/25/17
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public Docket customDocket() {
        Predicate<RequestHandler> predicate = requestHandler -> requestHandler.isAnnotatedWith(ApiModel.class);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(true)
                .select()
                .apis(predicate)
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("用户与城市关联")
                .contact(new Contact("yuanzhen", "https://yziyz.xin", "zenuor@gmail.com"))
                .description("检测项目")
                .version("v1.0")
                .build();
    }
}
