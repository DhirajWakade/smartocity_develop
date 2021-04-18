package com.allinone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.allinone.admin.service.AdminServices;
import com.allinone.configuration.Response;
import com.allinone.model.AddressDetails;
import com.allinone.model.CustomerUser;
import com.allinone.model.business.BusinessCategory;
import com.allinone.model.customer.Cart;
import com.allinone.model.customer.OrderDetails;
import com.allinone.model.product.ProductDetails;
import com.allinone.model.sample.AddAddressToCustomer;
import com.allinone.model.sample.AddToCart;
import com.allinone.model.sample.RemoveFromCart;
import com.allinone.repository.business.BusinessCategoryRepository;
import com.allinone.repository.business.BusinessTypesRepository;
import com.allinone.repository.product.ProductDetailsRepository;
import com.allinone.services.BusinessServices;
import com.allinone.services.CustomerService;
import com.allinone.services.OrderServices;
import com.allinone.services.ProductServices;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;
	
	@Autowired
	private AdminServices adminServices;
	
	@Autowired
	private OrderServices orderService;
	
	@Autowired
	private BusinessServices businessService;
	
	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> saveUser(@RequestBody CustomerUser user) throws Exception 
	{
		Response<?> respone=null;
		CustomerUser umobile=customerService.findByMobileNo(user.getMobileNo());
		if(umobile!=null)
		{
			respone=new Response<CustomerUser>(null,"0","Mobile No already Existed");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
		CustomerUser u=customerService.save(user);
		if (u != null) 
		{
			respone=new Response<CustomerUser>(u,"1","Successfuly Registered");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<CustomerUser>(null,"0","failed...try again");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/searchProduct/{desc}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> searchProduct(@PathVariable("desc")String desc) 	
	{
		Response<?> respone=null;
			List<ProductDetails>prodcCatList=productDetailsRepository.searchProductDetails(desc);
			if (prodcCatList== null) 
			{
				respone=new Response<List<ProductDetails>>(null,"0","Products Not Found");
				return new ResponseEntity<>(respone,HttpStatus.OK);
						
			} else {
				respone=new Response<List<ProductDetails>>(prodcCatList,"1","Successfully");
				return new ResponseEntity<>(respone,HttpStatus.OK);
			}			
	}
	
	@RequestMapping(value = "/HomeAPICustomer", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> homeAPI() throws Exception 
	{
		Response<?> respone=null;
		Map<String,Object>map=new HashMap<String, Object>();
		
		map.put("TopBusinessType", businessService.getActiveTopBusiness());
		map.put("OfferedProduct",productServices.activeOfferedproduct());
		
		respone=new Response<Map>(map,"1","Successfull");
		return new ResponseEntity<>(respone,HttpStatus.OK);
	}
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getCatogies() throws Exception 
	{
		Response<?> respone=null;
		List<BusinessCategory>bc=businessCategoryRepository.findAll();
		if (bc== null) 
		{
			respone=new Response<List<?>>(null,"0","Business Category Not Found");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<List<?>>(bc,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}	
		
	}
	
	
	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> placeOrder(@RequestBody OrderDetails order) throws Exception 
	{
		Response<?> respone=null;
		OrderDetails orderNew=orderService.saveOrderDetails(order);
		if (orderNew== null) 
		{
			respone=new Response<OrderDetails>(null,"0","Failed to place Order");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<OrderDetails>(orderNew,"1","Successfully Placed Order");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/addToCart", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> addToCart(@RequestBody AddToCart adtc) throws Exception 
	{
		Response<?> respone=null;
		Cart cart=customerService.addProductCart(adtc);
		if (cart== null) 
		{
			respone=new Response<Cart>(null,"0","Failed");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<Cart>(cart,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
		
	}
	@RequestMapping(value = "/removeFromCart", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> removeFromCart(@RequestBody RemoveFromCart rm) throws Exception 
	{
		Response<?> respone=null;
		Cart cart=customerService.remoteProductFromCart(rm);
		if (cart== null) 
		{
			respone=new Response<Cart>(null,"0","Failed");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<Cart>(cart,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
		
	}
	@RequestMapping(value = "/getCartDetails/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getCartDetails(@PathVariable("customerId") Long customerId) throws Exception 
	{
		Response<?> respone=null;
		Cart cart=customerService.getCartDetailsByCustId(customerId);
		if (cart== null) 
		{
			respone=new Response<Cart>(null,"0","Failed");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		} else {
			respone=new Response<Cart>(cart,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value = "/getProductByBusinessType/{businessTypeId}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getProductByBusinessType(@PathVariable("businessTypeId") Long businessTypeId) throws Exception 
	{
		Response<?> respone=null;
		List<ProductDetails>pdList=productServices.getProductByBusinessTypeId(businessTypeId);
		if (pdList.size()==0||pdList== null) 
		{
			respone=new Response<List<?>>(null,"0","Product Not Found");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		}
		else {
			respone=new Response<List<?>>(pdList,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}	
		
	}
	@RequestMapping(value = "/addCustomerAddress", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> addCustomerAddress(@RequestBody AddAddressToCustomer aac)
	{
		Response<?> respone=null;
		AddressDetails ad=customerService.saveCustomerAddress(aac);
		if (ad==null) 
		{
			respone=new Response<AddressDetails>(null,"0","Failed...to Address");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		}
		else {
			respone=new Response<AddressDetails>(ad,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/getCustomerAddress/{custId}", method = RequestMethod.GET)
	public ResponseEntity<Response<?>> getCustomerAddress(@PathVariable Long custId)
	{
		Response<?> respone=null;
		List<AddressDetails> ad=customerService.getCustomerAddress(custId);
		if (ad==null) 
		{
			respone=new Response<AddressDetails>(null,"0","No Address Found");
			return new ResponseEntity<>(respone,HttpStatus.OK);
					
		}
		else {
			respone=new Response<List<AddressDetails>>(ad,"1","Successfully");
			return new ResponseEntity<>(respone,HttpStatus.OK);
		}
	}
	

}
