package com.sweettracker.resourceserver.product.application.port.out;

import com.sweettracker.resourceserver.product.domain.Product;
import com.sweettracker.resourceserver.product.domain.ProductPage;

public interface FindProductPort {

    Product findById(Integer id);

    ProductPage findProductList(int page);
}
