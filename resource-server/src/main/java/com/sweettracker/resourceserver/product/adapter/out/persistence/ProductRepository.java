package com.sweettracker.resourceserver.product.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
