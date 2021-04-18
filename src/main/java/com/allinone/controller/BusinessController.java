package com.allinone.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.allinone.configuration.Response;
import com.allinone.exception.FieldRequiredException;
import com.allinone.model.BusinessDetails;
import com.allinone.model.BusinessmanUser;
import com.allinone.model.CustomerUser;
import com.allinone.model.business.BusinessTypes;
import com.allinone.model.business.BusinessTypesWithProductType;
import com.allinone.model.product.ProductDetails;
import com.allinone.model.product.ProductMaster;
import com.allinone.model.product.ProductSizeType;
import com.allinone.model.product.ProductTypes;
import com.allinone.repository.TaxRateMasterRepository;
import com.allinone.repository.business.BusinessTypesRepository;
import com.allinone.repository.product.ProductSizeTypeRepository;
import com.allinone.repository.product.ProductTypesRepository;
import com.allinone.services.BusinessDetailsService;
import com.allinone.services.BusinessServices;
import com.allinone.services.ProductServices;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/business")
public class BusinessController {

	@Autowired
	private BusinessServices businessServices;
	
	@Autowired
	private BusinessDetailsService businessDetailsService;
	
	@Autowired
	private ProductTypesRepository  productTypesRepository;
	
	@Autowired
	private ProductSizeTypeRepository productSizeTypeRepository;
	
	@Autowired
	private BusinessTypesRepository businessTypesRepository;
	
	@Autowired
	private TaxRateMasterRepository taxRateMasterRepository;
	
	@Autowired
	private ProductServices productServices;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> saveUser(@RequestBody BusinessmanUser user) throws Exception 
	{
		Response<?> respone=null;
		BusinessmanUser umobile=businessServices.findByMobileNo(user.getMobileNo());
		if(umobile!=null)
		{
			respone=new Response<CustomerUser>(null,"0","Mobile No already Existed");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
		BusinessmanUser u=businessServices.save(user);
		if (u != null) 
		{
			respone=new Response<BusinessmanUser>(u,"1","Successfuly Registered");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<BusinessmanUser>(null,"0","failed...try again");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/addNewBusiness", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> addNewBusiness(@RequestBody BusinessDetails bd) throws Exception 
	{
		Response<?> respone=null;
		if(StringUtils.isEmpty(bd.getPanCardNo()))
		{
			throw new FieldRequiredException("Pan Card Should Not Empty");
		}
		BusinessDetails newBd= businessDetailsService.saveBusiness(bd);
		if(newBd != null) 
		{
			respone=new Response<BusinessDetails>(newBd,"1","Successfuly Added Business");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<BusinessmanUser>(null,"0","failed...try again");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/HomeAPIBusiness/{businessManID}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> homeAPI(@PathVariable("businessManID") Long businessManID) throws Exception 
	{
		Response<?> respone=null;
		Map<String,Object>map=new HashMap<String, Object>();
		BusinessmanUser businesman=businessServices.findById(businessManID);
		
		if(StringUtils.isEmpty(businesman.getBmId()))
		{
			throw new FieldRequiredException("Invalid Business man Id");
		}
		
		Set<BusinessDetails> businessDetailsList= businessDetailsService.findBusinessDetailsByBusinessManId(businessManID);
		businesman.setBusinessDetails(businessDetailsList);
		
		
		//map.put("businessManDetails", businesman);
		//map.put("businessDetails", businessDetailsList);
		//map.put("productTypes", productTypesRepository.findAll());
		//map.put("productSizeTypes",productSizeTypeRepository.findAll());
		map.put("businessTypes", businessTypesRepository.findAll());
		
		List<BusinessTypes>btList= businessTypesRepository.findAll();
		List<BusinessTypesWithProductType>btWithPtList=new ArrayList<BusinessTypesWithProductType>();
		for(BusinessTypes bt:btList)
		{
			List<ProductTypes>ptList=productTypesRepository.findAllBusinessTypeId(bt.getBusinessTypeId());
			List<ProductSizeType>pst=productSizeTypeRepository.findAllBusinessTypeId(bt.getBusinessTypeId());
			BusinessTypesWithProductType btnew=new BusinessTypesWithProductType(bt.getBusinessTypeId(), bt.getBusinessTypeName(),bt.getIsGstNoRequired(),
					bt.getBusinessTypeCode(), bt.getIsMultiSelection(), bt.getIsProductSearchAllow(),
					ptList,pst);
			btWithPtList.add(btnew);
		}
		map.put("businessTypesWithProductType", btWithPtList);
		map.put("taxRates", taxRateMasterRepository.findAll());
		
		//if (businesman != null) 
		//{
			respone=new Response<Map>(map,"1","Successfull");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		//} else {
		//	respone=new Response<BusinessmanUser>(null,"0","failed...try again");
		//	return new ResponseEntity<>(respone,HttpStatus.OK);
		//}
	}
	@RequestMapping(value = "/getBusinessDetails/{businessManID}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getBusinessDetails(@PathVariable("businessManID") Long businessManID) throws Exception 
	{
		Response<?> respone=null;
		BusinessmanUser businesman=businessServices.findById(businessManID);
		
		if(businesman==null||StringUtils.isEmpty(businesman.getBmId()))
		{
			throw new FieldRequiredException("Invalid Business man Id");
		}
				
		//if (businesman != null) 
		//{
			Set<BusinessDetails> businessDetailsList= businessDetailsService.findBusinessDetailsByBusinessManId(businessManID);
//			for(BusinessDetails bs:businessDetailsList)
//			{
//				List<ProductDetails>plist=productServices.getProductListByBusinessId(bs);
//				//System.out.println("hhh="+plist.get(0).getProductDetailId());
//				bs.setProductDetails(new HashSet<>(plist));
//			}
			businesman.setBusinessDetails(businessDetailsList);
			respone=new Response<Set<BusinessDetails>>(businessDetailsList,"1","Successfull");
			return new ResponseEntity<>(respone,HttpStatus.OK);					
		//} 
	}
	
	
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> addProduct(@RequestBody ProductDetails pd) throws Exception 	
	{
		Response<?> respone=null;
		if(pd.getProductMaster()==null)
		{
			throw new FieldRequiredException("Product Master Data not Available");
		}
		if(pd.getPrice()==null)
		{
			throw new FieldRequiredException("Product Price is null");
		}
		if(pd.getBusinessDetails()==null&&pd.getBusinessDetails().getBusinessId()==null)
		{
			throw new FieldRequiredException("Business Details null");
		}
		
		ProductDetails newpd= productServices.saveProductDetails(pd);
		if (newpd != null) 
		{
			respone=new Response<ProductDetails>(newpd,"1","Successfuly Product");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<ProductDetails>(null,"0","failed...try again");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value = "/uploadProductImg", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response<?>> uploadProductImg(@RequestParam("file") MultipartFile mfile) throws Exception 	
	{
		Response<?> respone=null;
			String urlImg=productServices.uploadProductImage(mfile);
			if (urlImg== null||urlImg.equals("")) 
			{
				respone=new Response<String>(null,"0","Image not uploaded");
				return new ResponseEntity<>(respone,HttpStatus.OK);
						
			} else {
				respone=new Response<String>(urlImg,"1","Successfully uploaded");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}			
			
	}
	
	@RequestMapping(value = "/uploadProductSubImgs", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response<?>> uploadProductImg(@RequestParam("file") List<MultipartFile> mfile) throws Exception 	
	{
		Response<?> respone=null;
		List<String>ulrList=new ArrayList<>();
		for(MultipartFile m:mfile)
		{
			ulrList.add(productServices.uploadProductImage(m));
		}
			
			if (ulrList.size()==0) 
			{
				respone=new Response<String>(null,"0","Image not uploaded");
				return new ResponseEntity<>(respone,HttpStatus.OK);
						
			} else {
				respone=new Response<List<String>>(ulrList,"1","Successfully uploaded");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}			
			
	}
	
	/*@RequestMapping(value = "/getProductTypeByBusinessTypeId/{btID}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getProductTypeByBusinessTypeId(@PathVariable("btID")Long btID ) 	
	{
		Response<?> respone=null;
			List<ProductTypes>ptList=productTypesRepository.findAllBusinessTypeId(btID);
			if (ptList== null) 
			{
				respone=new Response<List<ProductSubCategory>>(null,"0","Product Types Not Found");
				return new ResponseEntity<>(respone,HttpStatus.OK);
						
			} else {
				respone=new Response<List<ProductTypes>>(ptList,"1","Successfully");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}			
			
	}*/
	@RequestMapping(value = "/getProductByBusinessId/{businessId}/{pageNo}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> searchProduct(@PathVariable("businessId")Long businessId,@PathVariable("pageNo")Integer pageNO) 	
	{
		Response<?> respone=null;
			List<ProductDetails>prodcCatList=productServices.findProductDetailsByBusinessId(businessId, pageNO);
			if (prodcCatList== null) 
			{
				respone=new Response<List<ProductDetails>>(null,"0","Products Not Found");
				return new ResponseEntity<>(respone,HttpStatus.OK);
						
			} else {
				respone=new Response<List<ProductDetails>>(prodcCatList,"1","Successfully");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}			
			
	}
	@RequestMapping(value = "/editProduct", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> editProduct(@RequestBody ProductDetails pd) throws Exception 	
	{
		Response<?> respone=null;
		if(pd.getProductMaster()==null)
		{
			throw new FieldRequiredException("Product Master Data not Available");
		}
		if(pd!=null&&pd.getProductDetailId()==null)
		{
			throw new FieldRequiredException("Product DetailId is null");
		}
		if(pd.getPrice()==null)
		{
			throw new FieldRequiredException("Product Price is null");
		}
		if(pd.getBusinessDetails()==null&&pd.getBusinessDetails().getBusinessId()==null)
		{
			throw new FieldRequiredException("Business Details null");
		}
		
		ProductDetails newpd= productServices.saveProductDetails(pd);
		if (newpd != null) 
		{
			respone=new Response<ProductDetails>(newpd,"1","Successfuly Product");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<ProductDetails>(null,"0","failed...try again");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}

	
}
