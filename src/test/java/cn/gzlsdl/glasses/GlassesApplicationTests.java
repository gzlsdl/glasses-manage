package cn.gzlsdl.glasses;

import cn.gzlsdl.glasses.modules.dao.UserMapper;
import cn.gzlsdl.glasses.modules.dao.WhiteListMapper;
import cn.gzlsdl.glasses.modules.entity.User;
import cn.gzlsdl.glasses.modules.entity.WhiteList;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GlassesApplicationTests {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private WhiteListMapper whiteListDao;

    @Test
    public void contextLoads() {
        WhiteList whiteList=new WhiteList();
        whiteList.setIp("218.19.204.169");
         whiteList=whiteListDao.selectOne(whiteList);

        if (whiteList==null){
            log.info("此ip未在白名单内");
        }else {
            log.info("此ip获得许可进入");

        }
    }

}
