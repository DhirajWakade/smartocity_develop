package com.allinone.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinone.model.AddressDetails;
import com.allinone.model.CustomerUser;
import com.allinone.model.User;
import com.allinone.model.customer.Cart;
import com.allinone.model.customer.CartProduct;
import com.allinone.model.customer.HomePageMenu;
import com.allinone.model.customer.HomePageOffersDetails;
import com.allinone.model.product.ProductDetails;
import com.allinone.model.sample.AddAddressToCustomer;
import com.allinone.model.sample.AddToCart;
import com.allinone.model.sample.RemoveFromCart;
import com.allinone.repository.AddressDetailsRepository;
import com.allinone.repository.CustomerRepository;
import com.allinone.repository.customer.CartProductRepository;
import com.allinone.repository.customer.CartRepository;
import com.allinone.repository.customer.HomePageMenuRepository;
import com.allinone.repository.customer.HomePageOffersDetailsRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserServices userServices;
	

	@Autowired
	private HomePageOffersDetailsRepository offersDetailsRepository;
	
	@Autowired
	private HomePageMenuRepository homePageMenuRepository;
	
	@Autowired
	private CartProductRepository cartProductRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductServices productService;
	
	@Autowired
	private AddressDetailsRepository addressDetailsRepository; 
	
	
	public CustomerUser save(CustomerUser u)
	{
		u.setCustomerUsername(u.getMobileNo()+"_C");
		User user=new User(u.getCustomerUsername(),u.getPassword(),"CUSTOMER");
		userServices.save(user);
		CustomerUser us=customerRepository.save(u);
		return us;
	}
	public CustomerUser findByCustomerUserName(String u)
	{
		CustomerUser us=customerRepository.findByCustomerUsername(u);
		return us;
	}
	public CustomerUser findByCustomerId(Long u)
	{
		Optional<CustomerUser> us=customerRepository.findById(u);
		return us.get();
	}
	public CustomerUser findByMobileNo(String mobileNo)
	{
		CustomerUser us=customerRepository.findByMobileNo(mobileNo);
		return us;
	}
	
	
	public Iterable<HomePageOffersDetails> getActiveOffers()
	{
		return offersDetailsRepository.findofferDetailsByStatus(HomePageOffersDetails.OFR_STATUS_ACTIVE);
	}
	public Iterable<HomePageMenu> getActiveMenu()
	{
		return homePageMenuRepository.findMenuByStatus(HomePageMenu.MN_STATUS_ACTIVE);
	}
	public Cart createOrGetCart(CustomerUser customer)
	{
		if (customer == null) {
			throw new NullPointerException("Customer should not Null in createCard()");
		}
		if (customer.getCustomerId() == null) {
			throw new NullPointerException("Customer Id should not Null in createCard()");
		}
		Long customerId = customer.getCustomerId();
		Cart exting_cart = cartRepository.findCartByCustomerId(customerId);
		if (exting_cart != null) {
			return exting_cart;
		} else {
			Cart ct = new Cart();
			ct.setCustomer(customer);
			ct.setTotalAmount(0.0);
			return cartRepository.save(ct);
		}
	}
	
	
	public Cart addProductCart(AddToCart atc)
	{
		try
		{
		if(atc.getCustomer()==null&&atc.getCustomer().getCustomerId()==null)
		{
			throw new NullPointerException("Customer should not Null in addProductCart()");
		}if(atc.getProductDetail()==null||atc.getProductDetail().getProductDetailId()==null)
		{
			throw new NullPointerException("Product should not Null in addProductCart()");
		}
		ProductDetails product=productService.getProductDetailsById(atc.getProductDetail().getProductDetailId());
		Long custId=atc.getCustomer().getCustomerId();
		CustomerUser customer=findByCustomerId(custId);
		
		Cart cart=createOrGetCart(customer);
		CartProduct cartProduct=new CartProduct();
		cartProduct.setProductDetails(product);
		cartProduct.setQuantity(atc.getQuantity());
		
		Double productPrice=product.getFinalPrice();
		Integer qantity=cartProduct.getQuantity();
		Double price=productPrice*qantity;
		cartProduct.setTotalAmount(price);
		
		CartProduct saveCartProduct=cartProductRepository.saveAndFlush(cartProduct);
		cart.addCardProducts(saveCartProduct);
		Double carttotal=cart.getTotalAmount()+saveCartProduct.getTotalAmount();
		
		cart.setTotalAmount(carttotal);
		Cart updatedCart=cartRepository.saveAndFlush(cart);
		
		return updatedCart;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Cart remoteProductFromCart(RemoveFromCart rmp)
	{
		try
		{
			if(rmp.getCustomer()==null&&rmp.getCustomer().getCustomerId()==null)
			{
				throw new NullPointerException("Customer should not Null in remoteProductFromCart()");
			}
			if(rmp.getCartProduct()==null||rmp.getCartProduct().getCpId()==null)
			{
				throw new NullPointerException("Cart Product should not Null in remoteProductFromCart()");
			}
			
			CartProduct removeProduct=productService.findCartProductById(rmp.getCartProduct().getCpId());
			
			Long custId=rmp.getCustomer().getCustomerId();
			CustomerUser customer=findByCustomerId(custId);
			
			Cart cart=createOrGetCart(customer);
			
			cart.removeCartProduct(removeProduct);
			
			Cart updatedCart=cartRepository.saveAndFlush(cart);
			
			return updatedCart;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Cart getCartDetailsByCustId(Long customerId) {
		if(customerId==null)
		{
			throw new NullPointerException("Customer should not Null in getCartDetailsByCustId()");
		}
		
		CustomerUser customer=findByCustomerId(customerId);
		
		Cart cart=createOrGetCart(customer);
		return cart;
	}
	
	public AddressDetails saveCustomerAddress(AddAddressToCustomer aac)
	{
		if(aac!=null&&aac.getCustomer()==null&&aac.getCustomer().getCustomerId()==null)
		{
			throw new NullPointerException("Customer should not Null in saveCustomerAddress()");
		}
		if(aac.getAddressDetail()==null)
		{
			throw new NullPointerException("Address details should not Null in saveCustomerAddress()");
		}
		Long custId=aac.getCustomer().getCustomerId();
		CustomerUser customer=findByCustomerId(custId);
		AddressDetails ad=addressDetailsRepository.save(aac.getAddressDetail());
		customer.addAddress(ad);
		customerRepository.saveAndFlush(customer);
		return ad;
	}
	public List<AddressDetails> getCustomerAddress(Long customerId)
	{
		if(customerId==null)
		{
			throw new NullPointerException("CustomerId should not Null in getCustomerAddress()");
		}		
		CustomerUser customer=findByCustomerId(customerId);
		return customer.getAddressDetails();
	}
	

}
