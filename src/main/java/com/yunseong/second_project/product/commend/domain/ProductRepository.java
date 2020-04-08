package com.yunseong.second_project.product.commend.domain;

import com.yunseong.second_project.product.query.application.dto.ProductResponse;
import com.yunseong.second_project.product.query.repository.ProductQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryRepository {

    @Query("select distinct p from Product p left join fetch p.productReferees referee where p.id = :id and p.delete = false")
    Optional<Product> findRecommendById(Long id);

    @Query("select distinct p from Product p join fetch p.types type where p.id = :id and p.delete = false")
    Optional<Product> findTypesById(Long id);

    @Query("select distinct new com.yunseong.second_project.product.query.application.dto.ProductResponse(p) from Product p left join p.productReferees referee left join referee.referee recommend " +
            "join p.types type join type.type t where p.id = :id and p.delete = false")
    Optional<ProductResponse> findDtoById(Long id);

    @Query(value = "select distinct new com.yunseong.second_project.product.query.application.dto.ProductResponse(p) from Product p left join p.productReferees referee left join referee.referee recommend " +
            "join p.types type join type.type t where p.delete = false", countQuery = "select distinct new com.yunseong.second_project.product.query.application.dto.ProductResponse(p) from Product p where p.delete = false")
    Page<ProductResponse> findAllByPage(Pageable pageable);
}
