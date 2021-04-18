package com.allinone.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinone.constants.Constants;
import com.allinone.exception.FieldRequiredException;
import com.allinone.model.CustomerUser;
import com.allinone.model.customer.OrderDetails;
import com.allinone.repository.customer.OrderDetailsRepository;

@Service
public class OrderServices 
{
	Logger logger = LoggerFactory.getLogger(OrderServices.class);
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepositoy;
	
	@Autowired
	private CustomerService customerService;
	
	public String getOrderCode()
	{
		Integer count=orderDetailsRepositoy.findCount();
		count++;
		return Constants.ORDER_CODE+count;
	}
	
	public OrderDetails saveOrderDetails(OrderDetails orderd)
	{
		Long customerId=orderd.getCustomer().getCustomerId();
		CustomerUser customer=null;
		if (customerId == null) {
			throw new FieldRequiredException("Customer Id Should not null");
		} else {
			customer = customerService.findByCustomerId(customerId);
		}
		logger.info("***OrderServices=>saveOrderDetails started***CustomerID = "+customerId);
		orderd.setOrderStatus(Constants.ORDER_STATUS_CREATED);
		orderd.setOrderCode(getOrderCode());
		orderd.setCustomer(customer);
		OrderDetails placeOrder=orderDetailsRepositoy.save(orderd);
		logger.info("***OrderServices=>saveOrderDetails End***Created order = "+placeOrder.getOrderCode());
		return placeOrder;
		
	}

}
