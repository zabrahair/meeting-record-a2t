package meeting.record.restapi.utils;

import com.alibaba.fastjson.JSON;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Voice2TextProcess {

    private static Logger logger = LoggerFactory.getLogger(Voice2TextProcess.class);
    private static final String AUDIO_RESULT_BEST_ONE_PROP = "onebest";

	private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;
//    private static final LfasrType type = LfasrType.LFASR_TELEPHONY_RECORDED_AUDIO;
    private static int sleepSecond = 20;

    public static String wav2Text(String filePath) {
        String resultRawData = "";
        LfasrClientImp lc = null;
        try {
            lc = LfasrClientImp.initLfasrClient();
        } catch (LfasrException e) {
            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.error("ecode={}",initMsg.getErr_no());
            logger.info("failed={}", initMsg.getFailed());
        }

        String task_id = "";
        HashMap<String, String> params = new HashMap<>();
        params.put("has_participle", "true");
        try {
            Message uploadMsg = lc.lfasrUpload(filePath, type, params);

            int ok = uploadMsg.getOk();
            if (ok == 0) {
                task_id = uploadMsg.getData();
                logger.info("task_id={}", task_id);
            } else {
                logger.info("ecode={}", uploadMsg.getErr_no());
                logger.info("failed={}", uploadMsg.getFailed());
            }
        } catch (LfasrException e) {
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.info("ecode={}", uploadMsg.getErr_no());
            logger.info("failed={}", uploadMsg.getFailed());
        }

        while (true) {
            try {
                Thread.sleep(sleepSecond * 1000);
                System.out.println("waiting ...");
            } catch (InterruptedException e) {
            }
            try {
                Message progressMsg = lc.lfasrGetProgress(task_id);

                if (progressMsg.getOk() != 0) {
                    logger.info("task was fail. task_id:{}", task_id);
                    logger.info("ecode={}", progressMsg.getErr_no());
                    logger.info("failed={}", progressMsg.getFailed());

                    continue;
                } else {
                    ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
                    if (progressStatus.getStatus() == 9) {
                        logger.info("task was completed. task_id:{}", task_id);
                        break;
                    } else {
                        logger.info("task was incomplete. task_id:{}, status:{}", task_id, progressStatus.getDesc());
                        continue;
                    }
                }
            } catch (LfasrException e) {
                Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
                logger.info("ecode={}", progressMsg.getErr_no());
                logger.info("failed={}", progressMsg.getFailed());
            }
        }

        try {

            // Get Convert audio text.
            Message resultMsg = lc.lfasrGetResult(task_id);

            resultRawData = resultMsg.getData();
            logger.info("result raw data: {}", resultRawData);
//            JSON resultJsonObj = JSON.parseObject(resultRawData);
//            logger.info("coverted text: {}", resultJsonObj.toJSONString());

            if (resultMsg.getOk() == 0) {
                System.out.println(resultMsg.getData());
            } else {
                logger.info("ecode={}", resultMsg.getErr_no());
                logger.info("failed={}", resultMsg.getFailed());
            }
        } catch (LfasrException e) {
            Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
            logger.info("ecode={}", resultMsg.getErr_no());
            logger.info("failed={}", resultMsg.getFailed());
        }

        return resultRawData;
    }
}
