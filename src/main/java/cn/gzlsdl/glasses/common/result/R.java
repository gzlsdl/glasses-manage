package cn.gzlsdl.glasses.common.result;

import java.util.HashMap;
import java.util.Map;


/**
 * Class R
 * Description:跟前端约定的返回数据格式集合
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class R extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R sign(){
        return error(-1,"sign签名校验失败");
    }

    public static R mysql(){
        return error(-4,"数据库操作失败");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R ok(String message){
        return R.ok(0,message);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(int code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }


    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
