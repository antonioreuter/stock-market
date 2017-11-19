package com.stock.services.impl;

import com.stock.domain.Stock;
import com.stock.exception.StockConflictException;
import com.stock.exception.StockDataException;
import com.stock.exception.StockNotFoundException;
import com.stock.repositories.StockRepository;
import com.stock.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public Stock findById(long id) {
        return stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException());
    }

    public Page<Stock> findByName(String name, Pageable pageable) {
        return stockRepository.findByNameStartsWith(name, pageable);
    }

    public Page<Stock> findAll(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    @Override
    public Stock save(Stock stock) {
        try {
            return stockRepository.save(stock);
        } catch (DataIntegrityViolationException ex) {
            throw new StockDataException(ex.getMessage(), ex);
        } catch (OptimisticLockingFailureException ex) {
            throw new StockConflictException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void delete(long id) {
        stockRepository.delete(id);
    }
}
