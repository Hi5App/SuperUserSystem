package Tests;

import org.junit.jupiter.api.Test;

/**
 * @Author zhy
 * @Date 2023/6/29 14:27
 * @Description This is description of class
 * @Since version-1.0
 */
public class SomeTests {
    @Test
    void testParser(){
        String str="002345";
        System.out.println(Integer.parseInt(str));
    }
}
