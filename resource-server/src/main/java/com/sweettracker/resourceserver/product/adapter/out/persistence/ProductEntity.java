package com.sweettracker.resourceserver.product.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "PRODUCTS")
@NoArgsConstructor
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Integer id;

    @Column(name = "PRODUCT_NAME")
    private String name;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "DESCRIPTION")
    private String description;

    @Builder
    ProductEntity(int id, String name, int price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
