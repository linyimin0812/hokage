package com.hokage.biz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author linyimin
 * @date 2021/02/23 1:56am
 * @email linyimin520812@gmail.com
 * @description home controller
 */
@Controller
public class HomeController {
    /**
     * application entrance
     * @param model
     * @return
     */
    @RequestMapping({"/app/**", "/"})
    public String index(Model model) {
        return "index";
    }
}
