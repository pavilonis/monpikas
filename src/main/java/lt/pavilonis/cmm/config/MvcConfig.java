package lt.pavilonis.cmm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EnableSwagger2
@Configuration
public class MvcConfig {

   @Bean
   public Docket rest() {

      return new Docket(DocumentationType.SWAGGER_2)
            .directModelSubstitute(LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/rest/.*"))

            .build()
//            .pathProvider(new BasePathProvider())
            .apiInfo(restInfo());
   }

   private ApiInfo restInfo() {
      return new ApiInfoBuilder()
            .title("CMM REST programavimo sąsaja")
            .description("DESCRIPTION")
            .version("VERSION")
            .termsOfServiceUrl("http://terms.todo.lt")
            .license("LICENSE")
            .licenseUrl("http://license.todo.lt")
            .build();
   }
}
