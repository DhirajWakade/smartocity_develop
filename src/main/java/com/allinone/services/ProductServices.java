package com.allinone.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.allinone.constants.Constants;
import com.allinone.exception.FieldRequiredException;
import com.allinone.model.AddressDetails;
import com.allinone.model.BusinessDetails;
import com.allinone.model.customer.CartProduct;
import com.allinone.model.product.MultiSizeProductPrice;
import com.allinone.model.product.OfferedProduct;
import com.allinone.model.product.ProductDetails;
import com.allinone.model.product.ProductMaster;
import com.allinone.model.product.ProductTypes;
import com.allinone.model.sample.AddAddressToCustomer;
import com.allinone.repository.customer.CartProductRepository;
import com.allinone.repository.product.MultiSizeProductPriceRepository;
import com.allinone.repository.product.OfferedProductRepository;
import com.allinone.repository.product.ProductDetailsRepository;
import com.allinone.repository.product.ProductMasterRepository;
import com.allinone.repository.product.ProductSubImage;
import com.allinone.repository.product.ProductSubImageRepository;
import com.allinone.repository.product.ProductTypesRepository;
import com.google.common.io.Files;

@Service
public class ProductServices {
	
	public static String uploadFolder = System.getProperty("user.dir"); //+ "/target/classes/static/productImages";

	@Value("${upload.path}")	
	private String productImgPath;
	
	@Autowired
	private ProductMasterRepository  productMasterRepository;
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;
	
	@Autowired
	private BusinessDetailsService businessDetailsService;
	
	@Autowired
	private MultiSizeProductPriceRepository multiSizeProductPriceRepository;
	
	@Autowired
	private ProductSubImageRepository productSubImageRepository;
	
	@Autowired
	private OfferedProductRepository offeredProductRepository;
	
	@Autowired
	private ProductTypesRepository productTypesRepository;
	
	@Autowired
	private CartProductRepository cartProductRepository; 
	
	private final Logger log =LoggerFactory.getLogger(this.getClass());
	
	
	public ProductDetails saveProductDetails(ProductDetails pd)
	{
		log.info("Add Product : ProductServices.saveProductDetails()");
		BusinessDetails bd=businessDetailsService.findBusinessDetailsById(pd.getBusinessDetails().getBusinessId());
		if(bd==null)
		{
			throw new FieldRequiredException("Business Details Not Found");
		}
		if(pd.getProductMaster().getProdId()==null) {
		pd.setProductMaster(saveProductMaster(pd.getProductMaster()));
		}
		
		bd.addProductDetail(pd);
		businessDetailsService.saveBusiness(bd);
		ProductDetails newPd=productDetailsRepository.save(pd);
		log.info("Add Product : Succeccfull :Product Details ID="+newPd.getProductDetailId());
		return newPd;
	}
	public ProductMaster saveProductMaster(ProductMaster pm)
	{
		pm.setProductCode(assingProductCode());
		if(pm.getMultiSizeProductPrices()!=null) {
		pm.setMultiSizeProductPrices(saveMultiSizeProductPrices(pm.getMultiSizeProductPrices()));
		}
		if(pm.getProductSubImages()!=null)
		{
			pm.setProductSubImages(saveProductSubImagess(pm.getProductSubImages()));
		}
		return productMasterRepository.save(pm);
	}
	public List<MultiSizeProductPrice> saveMultiSizeProductPrices(List<MultiSizeProductPrice> ms)
	{
		List<MultiSizeProductPrice> mlist=new ArrayList<MultiSizeProductPrice>();
		for(MultiSizeProductPrice m:ms) {
			mlist.add(multiSizeProductPriceRepository.save(m));
		}
		return mlist;
	}
	public List<ProductSubImage> saveProductSubImagess(List<ProductSubImage> psList)
	{
		List<ProductSubImage> mlist=new ArrayList<ProductSubImage>();
		for(ProductSubImage m:psList) {
			//System.out.println("suunnnnn+"+);
			if(m.getSubImgId()==null)
			mlist.add(productSubImageRepository.save(m));
			
		}
		return mlist;
	}
	public MultiSizeProductPrice saveMultiSizeProductPrice(MultiSizeProductPrice m)
	{
		return multiSizeProductPriceRepository.save(m);
	}

	
	public String uploadProductImage(MultipartFile mfile)
	{
		String path=uploadFolder+productImgPath;		
		
		File f = new File(path);
		String cm=Long.valueOf(System.currentTimeMillis()).toString();
		String ext=Files.getFileExtension(mfile.getOriginalFilename());
		String fileNamechange=mfile.getOriginalFilename().substring(0,4).concat(cm).concat("."+ext);
		File convertfile = new File(path + "\\" + fileNamechange);
		try {
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			
			convertfile.createNewFile();
			String fileName = convertfile.getName();
			FileOutputStream fout = new FileOutputStream(convertfile);
			fout.write(mfile.getBytes());
			fout.close();
			return fileName;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/*public List<ProductDetails>getProductListByBusinessId(BusinessDetails bsid)
	{
		return productDetailsRepository.findByBusinessDetails(bsid);
	}*/
	public ProductDetails getProductDetailsById(Long pdid)
	{
		return productDetailsRepository.findById(pdid).get();
	}
	
	
	public List<ProductMaster> searchProductByDesc(String productDesc)
	{
		return productMasterRepository.searchProduct(productDesc);
	}
	
	public String assingProductCode()
	{
		Integer count=productMasterRepository.findCount();
		count++;
		return Constants.PRODUCT_CODE+count;
	}
	
	public List<ProductDetails> findProductDetailsByBusinessId(Long bdId,Integer pageNo)
	{
		//BusinessDetails b=new BusinessDetails();
		//b.setBusinessId(bdId);
		return productDetailsRepository.findByBusinessDetails(bdId);
	}
	
	public List<ProductDetails>activeOfferedproduct()
	{
		return productDetailsRepository.findProductDetailsByStatus("Y");
	}
	
	public List<ProductTypes> getProductTypeByBusinessTypeId(Long Businesstypeid)
	{
		return productTypesRepository.findAllByBusinessTypeId(Businesstypeid);
	}
	public List<ProductMaster>getProductMasterByProductTypeId(Long productTypeid)
	{
		return productMasterRepository.findAllProductTypeId(productTypeid);
	}
	public List<ProductDetails>getProductDetailsByProductMasterId(Long pmid)
	{
		return productDetailsRepository.findByProductMasterId(pmid);
	}
	
	public List<ProductDetails> getProductByBusinessTypeId(Long BusinessTypeId)
	{
		System.out.println("BusinessTypeId="+BusinessTypeId);
		List<ProductDetails>pdList=new ArrayList<ProductDetails>();
		List<ProductTypes> productTypes=getProductTypeByBusinessTypeId(BusinessTypeId);
		System.out.println("productTypes="+productTypes);
		for(ProductTypes pt:productTypes)
		{
			System.out.println("pt="+pt.getProductTypeId()+" , "+pt.getProductType());
			List<ProductMaster>pmList=getProductMasterByProductTypeId(pt.getProductTypeId());
			for(ProductMaster pm:pmList)
			{
				System.out.println("pm="+pm.getProductName()+" , "+pm.getProductTypes().getProductType());
				List<ProductDetails>pdListnew=getProductDetailsByProductMasterId(pm.getProdId());
				pdList.addAll(pdListnew);
			}
		}
		return pdList;
	}
	public CartProduct findCartProductById(Long cpid)
	{
		Optional<CartProduct> cpOptional=cartProductRepository.findById(cpid);
		if(cpOptional.isPresent())
			return cpOptional.get();
		else
			return null;
	}
	
}

