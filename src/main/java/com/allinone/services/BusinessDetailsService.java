package com.allinone.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinone.exception.FieldRequiredException;
import com.allinone.model.BusinessDetails;
import com.allinone.model.BusinessmanUser;
import com.allinone.model.business.BusinessTypes;
import com.allinone.repository.BusinessDetailsRepository;
import com.allinone.repository.business.BusinessTypesRepository;

@Service
public class BusinessDetailsService {
	
	 private final Logger log =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BusinessDetailsRepository businessDetailsRepository;
	
	@Autowired
	private BusinessTypesRepository businessTypesRepository;
	
	@Autowired
	private BusinessServices businessServices;
	
	
	public BusinessDetails saveBusiness(BusinessDetails businessDetails)
	{
		
		log.info("Save Business Info, BMID="+businessDetails.getBusinessmanUser().getBmId());
		try {
		Long businessManId=businessDetails.getBusinessmanUser().getBmId();
		
		if(businessManId==null)
		{
			throw new FieldRequiredException("Businessman Id is empty");
		}
		
		if(businessDetails.getBusinessType()==null)
		{
			throw new FieldRequiredException("Please select Business Type");
		}
		
		BusinessTypes businessType=businessTypesRepository.findById(businessDetails.getBusinessType().getBusinessTypeId()).get();
		if(businessType==null)
		{
			throw new FieldRequiredException("Invalid Business Type Id");
		}
		businessDetails.setBusinessType(businessType);
		BusinessmanUser existingbusinessman=businessServices.findById(businessManId);
		if(existingbusinessman==null)
		{
			throw new FieldRequiredException("Businessman is empty");
		}
		
		/*if(businessDetails.getBusinessCategory()==null)
		{
			throw new FieldRequiredException("Business Categories is Empty");
		}
		Set<BusinessCategory>bcSet=new HashSet<BusinessCategory>();
		for(BusinessCategory businessCategory:businessDetails.getBusinessCategory()) {
			try {
			bcSet.add(businessCategoryRepository.findById(businessCategory.getCatId()).get());
			}
			catch (NullPointerException e) {
				log.info("Save Business Info, Business categories null");
			}
		}*/
		//businessDetails.setBusinessCategory(bcSet);
		businessDetails.setBusinessmanUser(existingbusinessman);
		BusinessDetails bd=businessDetailsRepository.save(businessDetails);
		log.info("Save Business Info, Successfully Add Business id="+bd.getBusinessId());
		return bd;
		}
		catch(Exception e)
		{
			log.error("Save Business Info...Failed...,"+e.getMessage());
			return null;
		}
		
	}
	
	public Set<BusinessDetails> findBusinessDetailsByBusinessManId(Long businessManId)
	{
		return businessDetailsRepository.findBusinessByBusinessManId(businessManId);
	}
	
	public BusinessDetails findBusinessDetailsById(Long businessId)
	{		
		return businessDetailsRepository.findById(businessId).get();
	}
	
	

}
