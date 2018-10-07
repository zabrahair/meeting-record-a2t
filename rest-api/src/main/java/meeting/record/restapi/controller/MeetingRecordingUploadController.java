package meeting.record.restapi.controller;


import com.iflytek.msp.cpdb.lfasr.util.StringUtil;
import meeting.record.restapi.configuration.FileUploadConfiguration;
import meeting.record.restapi.utils.ConstantUtil;
import meeting.record.restapi.utils.Voice2TextProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MeetingRecordingUploadController {


    Logger logger = LoggerFactory.getLogger(FileUploadConfiguration.class);
    private String fileExtension = "wav";

    @Value("${uploadPath:}")
    private String uploadPath;

    @PostMapping(value="/file/upload")
    public @ResponseBody String upload(@RequestParam("audio") MultipartFile file){
        if (!file.isEmpty()) {
            String audioText = "";
            try {
                String location = uploadPath + ConstantUtil.AUDIO_UPLOAD_PATH;
                logger.info("uploadPath: {}",location);
                logger.info("filename: {}", file.getOriginalFilename());
                // 这里只是简单例子，文件直接输出到项目路径下。
                // 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
                // 还有关于文件格式限制、文件大小限制，详见：中配置。
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                String filePath = location + file.getOriginalFilename();
                String timeString = sf.format(new Date());
                filePath = String.format("%s_%s.%s",filePath, timeString, fileExtension);
                logger.info("filePath: {}", filePath);
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath)));
                out.write(file.getBytes());
                out.flush();
                out.close();

                audioText = Voice2TextProcess.wav2Text(filePath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return StringUtil.isEmpty(audioText)? "上传成功": audioText;
        } else {
            return "上传失败，因为文件是空的.";
        }
    }
}
