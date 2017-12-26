package com.stock.services.impl;

import com.stock.domain.Currency;
import com.stock.domain.CurrencyCode;
import com.stock.domain.Stock;
import com.stock.exception.StockConflictException;
import com.stock.exception.StockDataException;
import com.stock.exception.StockNotFoundException;
import com.stock.repositories.StockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Spy
    @InjectMocks
    private StockServiceImpl subject;

    @Test
    public void whenTryToFindById() {
        Stock petro = new Stock();
        petro.setId(1L);
        petro.setName("PETRO51");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());
        petro.setVersion(0L);

        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(petro));

        Stock result = subject.findById(1L);

        assertEquals(petro, result);
    }

    @Test(expected = StockNotFoundException.class)
    public void whenTryToFindByIdButThereIsNoStock() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        subject.findById(1L);
    }

    @Test
    public void whenTryToFindByName() {
        Stock petro = new Stock();
        petro.setId(1L);
        petro.setName("PETRO51");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());
        petro.setVersion(0L);

        Page<Stock> page = new PageImpl<>(Arrays.asList(petro));

        when(stockRepository.findByNameStartsWith(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Stock> result = subject.findByName("PET", new PageRequest(0,10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(petro));
    }

    @Test
    public void whenTryToFindAll() {
        Stock petro = new Stock();
        petro.setId(1L);
        petro.setName("PETRO51");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());
        petro.setVersion(0L);

        Stock apple = new Stock();
        apple.setName("APL");
        apple.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        Stock apg = new Stock();
        apg.setName("APG");
        apg.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        Page<Stock> page = new PageImpl<>(Arrays.asList(petro, apple, apg));

        when(stockRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Stock> result = subject.findAll(new PageRequest(0,10));

        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertTrue(result.getContent().contains(petro));
    }

    @Test(expected = StockDataException.class)
    public void whenTryToSaveAndAlreadyExistsAStockWithSameName() {
        doThrow(new DataIntegrityViolationException("Error")).when(stockRepository).save(any(Stock.class));

        subject.save(new Stock());
    }

    @Test(expected = StockConflictException.class)
    public void whenTryToSaveAndTheVersionOfRecordIsOutdated() {
        doThrow(new OptimisticLockingFailureException("Error")).when(stockRepository).save(any(Stock.class));

        subject.save(new Stock());
    }
}
