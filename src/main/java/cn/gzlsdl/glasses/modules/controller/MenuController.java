package cn.gzlsdl.glasses.modules.controller;


import cn.gzlsdl.glasses.common.result.R;
import cn.gzlsdl.glasses.common.util.BaseController;
import cn.gzlsdl.glasses.modules.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;






}

