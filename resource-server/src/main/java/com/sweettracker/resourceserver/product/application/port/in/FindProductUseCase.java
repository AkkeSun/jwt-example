package com.sweettracker.resourceserver.product.application.port.in;

import com.sweettracker.resourceserver.product.application.service.find_product.FindProductServiceResponse;

public interface FindProductUseCase {

    FindProductServiceResponse findProduct(Integer id);
}
