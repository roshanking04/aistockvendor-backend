package com.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded images — works on both local and Railway
        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .addResourceLocations("file:uploads/");
    }*/
  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
