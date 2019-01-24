package com.cloudside.config.monitor.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cloudside.common.util.ResultMsg;
import com.cloudside.config.monitor.service.CloudService;

/**
 * 云市场
 * @author Administrator
 *
 */
@RestController
@RequestMapping(value = "/cloud")
public class CloudController {
	
	@Autowired
	private CloudService cloudSvc;

	ResultMsg result;
	
	private static Logger log = LoggerFactory.getLogger(CloudController.class);

	/**
	 * 资源市场首页
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/scr/list")
	public ModelAndView homePage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
		mv.addObject("author", "Poci");
		mv.setViewName("/views/cloud/source/list.html");
		return mv;
	}
    
    /**
	 * 扩展首页
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/exp/list")
	public ModelAndView expandPage(ModelAndView mv,HttpServletRequest request,HttpServletResponse response) {
		mv.addObject("author", "Poci");
		mv.setViewName("/views/cloud/expend/list.html");
		return mv;
	}
    
}
