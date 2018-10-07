package meeting.record.restapi.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


import javax.servlet.MultipartConfigElement;


@Configuration
public class FileUploadConfiguration {

    Logger logger = LoggerFactory.getLogger(FileUploadConfiguration.class);

    @Value("${uploadPath:}")
    private String uploadPath;


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
        // 这样在文件上传的地方就需要进行异常信息的处理了;
        factory.setMaxFileSize("10MB"); // KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("20MB");
        // Sets the directory location where files will be stored.
        String location = StringUtils.isEmpty(uploadPath) ? "/audio_files" : uploadPath + "/audio_files";
        logger.info("location: {}", location);
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}

