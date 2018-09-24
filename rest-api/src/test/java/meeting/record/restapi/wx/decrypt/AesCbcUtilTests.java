package meeting.record.restapi.wx.decrypt;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AesCbcUtilTests {



    @Test
    public void decrypt() {
        String data = "getPhoneNumber";
        String key = "wx11edd7f03d34623d";
        String iv = "nPYdOlaqthnoI3p7Tm5SYQ==";
        String encodingFormat = "UTF-8";
        try {
            String deString = WXBizDataCrypt.getInstance().decrypt(data, key, iv, "utf-8");
            System.out.println(deString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
