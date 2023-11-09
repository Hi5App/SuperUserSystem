package Tests;

import org.junit.jupiter.api.Test;

import java.io.File;

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

    @Test
    void testStringJoin(){
        String resForCropSwc=String.join(File.separator, "/test", "00029");
        System.out.println(resForCropSwc);
    }

    @Test
    void testResXYZ2String(){

    }
}
