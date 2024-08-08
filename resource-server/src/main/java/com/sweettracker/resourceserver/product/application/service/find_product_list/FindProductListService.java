package com.sweettracker.resourceserver.product.application.service.find_product_list;

import com.sweettracker.resourceserver.product.application.port.in.FindProductListUseCase;
import com.sweettracker.resourceserver.product.application.port.out.FindProductPort;
import com.sweettracker.resourceserver.product.domain.ProductPage;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductListService implements FindProductListUseCase {

    private final FindProductPort findProductPort;

    @Override
    public FindProductListServiceResponse findProductList(int page) {
        ProductPage productPage = findProductPort.findProductList(page);
        return FindProductListServiceResponse.builder()
            .totalPage(productPage.getTotalPage())
            .productList(productPage.getProductList().stream()
                .map(product ->
                    FindProductListServiceResponseItem.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .build())
                .collect(Collectors.toList()))
            .build();
    }
}
