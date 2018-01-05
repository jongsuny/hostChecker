package com.jongsuny.monitor.hostChecker.controller.pages;

import com.jongsuny.monitor.hostChecker.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jongsuny on 18/1/3.
 */
@Controller
@RequestMapping("/pages/config")
public class BasicPageController {
    @Autowired
    protected ConfigService configService;
    protected void setCurrentDomain(ModelAndView mav, String domain, String subMenu){
        mav.addObject("currentDomain", domain);
        mav.addObject("subMenu", subMenu);
    }
}
