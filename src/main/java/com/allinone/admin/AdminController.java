package com.allinone.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.allinone.controller.RestController;
import com.allinone.model.business.BusinessTypes;
import com.allinone.model.business.BusinessTypesWithProductType;
import com.allinone.model.product.ProductTypes;
import com.allinone.repository.BusinessmanRepository;
import com.allinone.repository.CustomerRepository;
import com.allinone.repository.business.BusinessTypesRepository;
import com.allinone.repository.product.ProductTypesRepository;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private BusinessTypesRepository businessTypesRepository;
	@Autowired
	private ProductTypesRepository productTypesRepository;
	@Autowired
	private BusinessmanRepository businessmanRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping("/login")
	public String login() throws IOException
	{
		return "login.html";
	}
	@RequestMapping("/index")
	public ModelAndView index()
	{
		ModelAndView model = new ModelAndView("index");
		model.addObject("businessManCount", businessmanRepository.findAll().size());
		model.addObject("businessDetailsCount", businessTypesRepository.findAll().size());
		model.addObject("customerCount", customerRepository.findAll().size());
		return model;
	}
	@RequestMapping("/businessTypes")
	public ModelAndView getBusinessTypes()
	{
		ModelAndView model = new ModelAndView("business-types");
		List<BusinessTypes>businessTypeList=businessTypesRepository.findAll();
		model.addObject("businessTypes", businessTypeList);
		return model;
	}
	
	@RequestMapping("/productTypes")
	public ModelAndView productTypes()
	{
		ModelAndView model = new ModelAndView("product-types");
		List<ProductTypes>ptypes=productTypesRepository.findAll();
		model.addObject("productTypes", ptypes);
		return model;
	}
}
