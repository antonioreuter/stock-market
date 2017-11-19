package com.stock.services;

import com.stock.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockService {

    Stock findById(long id);

    Page<Stock> findAll(Pageable pageable);

    Page<Stock> findByName(String name, Pageable pageable);

    Stock save(Stock stock);

    void delete(long id);

}
