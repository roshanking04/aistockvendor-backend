/*package com.product.config;

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
    }
     @Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path path = Paths.get("uploads").toAbsolutePath();
    String uploadPath = path.toString();
    if (!uploadPath.endsWith(File.separator)) {
        uploadPath += File.separator;
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + uploadPath)
            .setCachePeriod(0);
    
    System.out.println("🚀 SERVING IMAGES FROM: " + uploadPath);
}
}
*/
package com.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Get the absolute path to the 'uploads' directory
        Path uploadDir = Paths.get("uploads").toAbsolutePath();
        String uploadPath = uploadDir.toString();

        // 2. Create the folder automatically if it doesn't exist
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("📁 Created missing folder at: " + uploadPath);
        }

        // 3. Add trailing slash for Spring
        if (!uploadPath.endsWith(File.separator)) {
            uploadPath += File.separator;
        }

        // 4. Register the location (Use 'file:///' for Windows compatibility)
        String location = "file:" + uploadPath.replace("\\", "/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location)
                .setCachePeriod(0);

        // 5. Look at your IDE Console for this message!
        System.out.println("=================================================");
        System.out.println("🚀 IMAGE SERVER IS RUNNING!");
        System.out.println("👉 Put your images in this folder: " + uploadPath);
        System.out.println("👉 Access them at: http://localhost:3869/uploads/filename.jpg");
        System.out.println("=================================================");
    }
}
