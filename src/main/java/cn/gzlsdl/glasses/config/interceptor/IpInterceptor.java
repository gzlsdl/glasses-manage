package cn.gzlsdl.glasses.config.interceptor;



import cn.gzlsdl.glasses.common.util.IPAddressUtil;
import cn.gzlsdl.glasses.modules.dao.WhiteListMapper;
import cn.gzlsdl.glasses.modules.entity.WhiteList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Class IpInterceptor
 * Description:ip拦截器，启用ip白名单
 * @author luxiaobo
 * Created on 2019/5/17
 */
@Slf4j
public class IpInterceptor  implements HandlerInterceptor{

    @Autowired
    private WhiteListMapper whiteListDao;


    /**
     * Method preHandle
     *
     *@author luxiaobo
     *@date 2019/6/1
     * 在api请求之前拦截，查看客户端的ip是否在ip白名单里面
     * @param request (类型：HttpServletRequest)
     * @param response (类型：HttpServletResponse)
     * @param handler (类型：Object)
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip= IPAddressUtil.getClientIpAddress(request);
        log.info("客户端ip为："+ip);
        if (!StringUtils.isNotBlank(ip)){
            log.error("ip有空白！");
            return false;
        }

        WhiteList whiteList=new WhiteList();
        whiteList.setIp(ip);
        whiteList=whiteListDao.selectOne(whiteList);

        if (whiteList==null){
            log.info("此ip未在白名单内");
            return false;
        }else {
            log.info(ip+",此ip获得许可进入");
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
