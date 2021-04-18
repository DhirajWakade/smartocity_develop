package com.allinone.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.allinone.model.customer.OrderDetails;
@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

	@Query("SELECT count(*) FROM OrderDetails od")
	public Integer findCount();
}
