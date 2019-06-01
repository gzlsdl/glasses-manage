package cn.gzlsdl.glasses.common.util;

import cn.gzlsdl.glasses.common.constant.Constant;
import cn.gzlsdl.glasses.config.filter.SQLFilter;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class Query
 * Description:从前端传过来的参数初始化page对象，再分页
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Data
public class Query<T> extends LinkedHashMap<String, Object>{

    private static final long serialVersionUID = 1L;

    private Page<T> page;

    private int currPage = 1;

    private int limit = 10;

    public Query(Map<String,Object> params) {
        this.putAll(params);

        if (params.get(Constant.PAGE)!=null){
            currPage=Integer.parseInt((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT)!=null){
            limit=Integer.parseInt((String) params.get(Constant.LIMIT));
        }

        this.put(Constant.OFFSET,(currPage-1)*limit);
        this.put(Constant.PAGE,currPage);
        this.put(Constant.LIMIT,limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String)params.get(Constant.SIDX));
        String order = SQLFilter.sqlInject((String)params.get(Constant.ORDER));
        this.put(Constant.SIDX, sidx);
        this.put(Constant.ORDER, order);

        //mybatis-plus分页
        this.page = new Page<>(currPage, limit);

        //排序
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)){
            this.page.setOrderByField(sidx);
            this.page.setAsc(Constant.ASC.equalsIgnoreCase(order));
        }
    }
}
