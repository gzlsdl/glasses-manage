package cn.gzlsdl.glasses.common.util;

import org.apache.commons.lang.StringUtils;
import java.util.Random;


/**
 * Class RandomVerCodeUtil
 * Description:生成一个六位数的随机验证码
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class RandomVerCodeUtil {

    public static String getVerCode(){
        Random r = new Random();
        return StringUtils.substring(String.valueOf(r.nextInt()*-10),3,9);
    }


    public static void main(String[] args) {

    }
}
