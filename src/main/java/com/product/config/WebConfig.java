package com.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File; 
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
/*
     @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This gets the absolute path to your 'uploads' folder
        String uploadPath = Paths.get("uploads").toFile().getAbsolutePath();
        
        // Ensure the path ends with a separator for Spring to read it as a directory
        if (!uploadPath.endsWith(File.separator)) {
            uploadPath += File.separator;
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0); 
        
        System.out.println("✅ Static resources mapped to: " + uploadPath);
    }*/
     @Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 1. Get the path to the uploads folder in the root directory
    Path path = Paths.get("uploads").toAbsolutePath();
    String uploadPath = path.toString();

    // 2. Format the path for Spring (needs trailing slash)
    if (!uploadPath.endsWith(File.separator)) {
        uploadPath += File.separator;
    }

    // 3. Register the handler
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + uploadPath)
            .setCachePeriod(0);
    
    System.out.println("🚀 SERVING IMAGES FROM: " + uploadPath);
}
}
