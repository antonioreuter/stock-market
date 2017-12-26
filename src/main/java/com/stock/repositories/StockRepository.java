package com.stock.repositories;

import com.stock.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "stocks", path = "stocks")
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {

    Optional<Stock> findById(Long id);

    @Query("select s from Stock s where s.name like ?1%")
    Page<Stock> findByNameStartsWith(String name, Pageable pageable);

}
