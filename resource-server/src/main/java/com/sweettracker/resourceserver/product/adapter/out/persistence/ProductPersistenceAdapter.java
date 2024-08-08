package com.sweettracker.resourceserver.product.adapter.out.persistence;

import com.sweettracker.resourceserver.global.exception.CustomNotFoundException;
import com.sweettracker.resourceserver.global.exception.ErrorCode;
import com.sweettracker.resourceserver.product.application.port.out.FindProductPort;
import com.sweettracker.resourceserver.product.domain.Product;
import com.sweettracker.resourceserver.product.domain.ProductPage;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements FindProductPort {

    private final ProductRepository productRepository;

    @Override
    public Product findById(Integer id) {
        Optional<ProductEntity> optional = productRepository.findById(id);
        if (!optional.isPresent()) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_PRODUCT);
        }

        ProductEntity entity = optional.get();
        return Product.builder()
            .id(entity.getId())
            .name(entity.getName())
            .price(entity.getPrice())
            .description(entity.getDescription())
            .build();
    }

    @Override
    public ProductPage findProductList(int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        Page<ProductEntity> searchResult = productRepository.findAll(pageable);
        int totalPages = searchResult.getTotalPages();

        return ProductPage.builder()
            .totalPage(searchResult.getTotalPages())
            .productList(searchResult.toList().stream()
                .map(entity -> Product.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .description(entity.getDescription())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 20; i++) {
            productRepository.save(ProductEntity.builder()
                .name(i + "번 상품")
                .price(8000 + i)
                .description(i + "번 상품 설명")
                .build());
        }
    }
}
