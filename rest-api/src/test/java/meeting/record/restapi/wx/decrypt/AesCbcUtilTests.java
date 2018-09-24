package wx.decrypt;


mport meeting.record.restapi.wx.decrypt.AesCbcUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AesCbcUtilTests {

    @Autowired
    private AesCbcUtil aesCbcUtil;

    @Test
    public void decrypt() {
        AesCbcUtil.decrypt(data, key, iv, encodingFormat);
    }

}
