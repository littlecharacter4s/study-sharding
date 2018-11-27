import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class BaseTest {
    @Test
    public void test() {
        Assert.assertTrue("这只是一个测试！", LocalDate.now().getYear() < Integer.MAX_VALUE);
    }
}
