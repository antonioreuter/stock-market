package com.stock.controllers.api;

import com.stock.domain.Stock;
import com.stock.exception.StockMismatchException;
import com.stock.services.StockService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin("*")
@RepositoryRestController
@RequestMapping(value = "/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Retrieve a stock based on id.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Stock findById(@PathVariable(value = "id") long id) {
        Stock stock = stockService.findById(id);
        log.info("Retrieving stock by id: {}", stock);
        return stock;
    }

    @ApiOperation(value = "Retrieves a list of stocks.", notes = "It's only possible to search by name, otherwise we will retrieve all stocks.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    public Page<Stock> findByParams(@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = new PageRequest(page, size);
        Page<Stock> stocks = null;
        if (StringUtils.isNotEmpty(name)) {
            stocks = stockService.findByName(name, pageable);
        } else {
            stocks = stockService.findAll(pageable);
        }

        log.info("Retrieving stocks by name {}. Total founded: {}", name, stocks.getTotalElements());

        return stocks;
    }

    @ApiOperation(value = "Update a stock based on id.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    @ResponseBody
    public Stock updateStock(@PathVariable("id") long id, @Valid @RequestBody Stock stock) {
        if (stock.getId() != id) {
            throw new StockMismatchException();
        }

        Stock stockUpdated = stockService.save(stock);
        log.info("Stock updated {}", id);
        return stockUpdated;
    }

    @ApiOperation(value = "Creates a new stock")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ResponseBody
    public Stock saveStock(@Valid @RequestBody Stock stock) {
        Stock stockCreated = stockService.save(stock);
        log.info("Stock created {}", stockCreated.getId());
        return stockCreated;
    }

    @ApiOperation(value = "Delete a stock based on id.")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        log.info("Deleting stock {}", id);
        stockService.delete(id);
    }
}
