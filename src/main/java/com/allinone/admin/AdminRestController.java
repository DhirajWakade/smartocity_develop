package com.allinone.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.allinone.admin.model.AdminUser;
import com.allinone.admin.service.AdminServices;
import com.allinone.configuration.Response;
import com.allinone.exception.FieldRequiredException;
import com.allinone.model.product.ProductDetails;

@RestController
@RequestMapping("/restAdmin")
public class AdminRestController 
{

	@Autowired
	private AdminServices service;
	
	@RequestMapping(value = "/hello")
	public String helo()	
	{
		return "Hello admin";
	}
	
	@RequestMapping(value = "/saveAdmin", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> saveAdmin(@RequestBody AdminUser user) throws Exception 	
	{
		System.out.println("H#########################");
		Response<?> respone=null;	
		AdminUser us=service.saveAdmin(user);
		if(us!=null)
		{
				respone=new Response<AdminUser>(us,"1","Successfuly Added");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			
		}
		else {
				respone=new Response<ProductDetails>(null,"0","Failed Try Again");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}
		
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> adminLogin(@RequestBody AdminUser user) throws Exception 	
	{
		Response<?> respone=null;	
		AdminUser us=service.adminLogin(user);
		if(us!=null)
		{
			if(us.getAdUserMail().equals(user.getAdUserMail())&&us.getAdUserPass().equals(user.getAdUserPass()))
			{
				respone=new Response<AdminUser>(us,"1","Successfuly Login");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}
			else
			{
				respone=new Response<AdminUser>(null,"0","Invalid Password");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}
		}
		else {
				respone=new Response<ProductDetails>(null,"0","User not found");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}
		
	}
	
	
}



