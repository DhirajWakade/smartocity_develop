package com.allinone.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinone.admin.model.AdminUser;
import com.allinone.admin.repository.AdminUserRepository;

@Service
public class AdminServices 
{
	@Autowired
	private AdminUserRepository adminUserRepository;
	
	
	
	public AdminUser adminLogin(AdminUser user)
	{
		AdminUser us=adminUserRepository.findByAdUserMail(user.getAdUserMail());
		return us;
	}
	public AdminUser saveAdmin(AdminUser user)
	{
		AdminUser us=adminUserRepository.save(user);
		return us;
	}
	
}
