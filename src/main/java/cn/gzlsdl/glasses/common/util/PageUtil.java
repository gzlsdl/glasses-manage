package cn.gzlsdl.glasses.common.util;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;

import java.util.List;

/**
 * Class PageUtil
 * Description:返回给前端的page对象分页封装类
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Data
public class PageUtil {

    //总记录数
    private int totalCount;

    //每页记录数
    private int pageSize;

    //总页数
    private int totalPage;

    //当前页数
    private int currentPage;

    //列表数据
    private List<?> list;


    public PageUtil(int totalCount, int pageSize, int currentPage, List<?> list) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage=(int) Math.ceil((double) totalCount/pageSize);
    }


    /**
     * 类：PageUtil
     * 分页处理
     * @author luxiaobo
     * @date 2019/5/29
     *
     * @param page (类型：Page<?>)
     */
    public PageUtil(Page<?> page) {
        this.list=page.getRecords();
        this.totalCount=page.getTotal();
        this.pageSize=page.getSize();
        this.currentPage=page.getCurrent();
        this.totalPage=page.getPages();
    }
}
