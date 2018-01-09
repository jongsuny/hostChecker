package com.jongsuny.monitor.hostChecker.controller.pages;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.validate.critirea.Operator;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by jongsuny on 18/1/3.
 */
@Controller
public class ServicePageController extends BasicPageController {
    @GetMapping("/newService")
    public ModelAndView newServiceConfig(ModelAndView mav) {
        setCurrentDomain(mav, "useful", "createService");
        mav.setViewName("config/new_service");
        return mav;
    }

    @GetMapping("/{domain}/service")
    public ModelAndView serviceConfig(ModelAndView mav, @PathVariable String domain) {
        setCurrentDomain(mav, domain, "service");
        mav.addObject("serviceConfig", configService.readServiceConfig(domain));
        mav.setViewName("config/service_config");
        return mav;
    }

    @GetMapping("/{domain}/group")
    public ModelAndView groupConfig(ModelAndView mav, @PathVariable String domain) {
        setCurrentDomain(mav, domain, "group");
        mav.addObject("serviceConfig", configService.readServiceConfig(domain));
        mav.setViewName("config/group");
        return mav;
    }

    @GetMapping("/{domain}/checkpoint")
    public ModelAndView checkPointConfig(ModelAndView mav, @PathVariable String domain) {
        setCurrentDomain(mav, domain, "checkpoint");
        mav.addObject("serviceConfig", configService.readServiceConfig(domain));
        Map<String, Object> map = Maps.newHashMap();
        map.put("validationTypes", ValidateType.values());
        map.put("operators", Operator.values());
        mav.addObject("dictionary", map);
        mav.setViewName("config/check_point");
        return mav;
    }

    @GetMapping("/{domain}/job")
    public ModelAndView jobConfig(ModelAndView mav, @PathVariable String domain) {
        setCurrentDomain(mav, domain, "job");
        mav.addObject("serviceConfig", configService.readServiceConfigWithJobs(domain));
        mav.setViewName("config/job");
        return mav;
    }
}
