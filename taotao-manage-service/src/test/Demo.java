
import com.github.pagehelper.StringUtil;

import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * Created by hui on 2017/9/4.
 */
public class Demo {


    @Test
    public void test1() {
        String s = "      ";
        System.out.println("================" + StringUtils.isEmpty(s.trim()));
    }

}
