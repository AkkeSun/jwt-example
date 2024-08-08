package com.sweettracker.resourceserver.product.application.port.in;

import com.sweettracker.resourceserver.product.application.service.find_product_list.FindProductListServiceResponse;

public interface FindProductListUseCase {

    FindProductListServiceResponse findProductList(int page);
}
