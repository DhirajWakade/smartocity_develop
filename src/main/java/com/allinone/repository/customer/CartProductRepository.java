package com.allinone.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allinone.model.customer.CartProduct;
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

}
