package br.ufpb.dcx.apps4society.educapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@SpringBootApplication
public class EducAPIApplication {

	@Value("${app.version}")
	private String version;
	
	public static void main(String[] args) {
		SpringApplication.run(EducAPIApplication.class, args);	
	}

	@GetMapping("/")
    @ResponseBody
	public String index() {
      return String.format("Welcome to EducAPI! | VERSION: v%s", this.version);
    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
				registry.addMapping("/**").allowedMethods("*");
				registry.addMapping("/**").allowedHeaders("*");
			}
		};
	}

}
