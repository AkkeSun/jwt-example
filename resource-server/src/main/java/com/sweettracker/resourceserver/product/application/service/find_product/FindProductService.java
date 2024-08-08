package com.sweettracker.resourceserver.product.application.service.find_product;

import com.sweettracker.resourceserver.product.application.port.in.FindProductUseCase;
import com.sweettracker.resourceserver.product.application.port.out.FindProductPort;
import com.sweettracker.resourceserver.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProductService implements FindProductUseCase {

    private final FindProductPort findProductPort;

    @Override
    public FindProductServiceResponse findProduct(Integer id) {
        Product product = findProductPort.findById(id);
        return FindProductServiceResponse.builder()
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .build();
    }
}
