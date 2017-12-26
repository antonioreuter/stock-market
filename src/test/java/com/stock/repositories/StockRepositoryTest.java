package com.stock.repositories;


import com.stock.domain.Currency;
import com.stock.domain.CurrencyCode;
import com.stock.domain.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class StockRepositoryTest {

    @Autowired
    private StockRepository subject;

    @Test
    public void whenTryToSaveANewStock() {

        Stock petro = new Stock();
        petro.setName("PETRO26");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        try {
            subject.save(petro);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenTryToSaveTwoStocksWithSameName() {
        Stock petro = new Stock();
        petro.setName("PETRO27");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        subject.save(petro);

        Stock petro2 = new Stock();
        petro2.setName("PETRO27");
        petro2.setPrice(Currency.builder().amount(BigDecimal.valueOf(90.67D)).currencyCode(CurrencyCode.USD).build());

        subject.save(petro2);
    }

    @Test
    public void whenTryToFindById() {
        Stock petro = new Stock();
        petro.setName("PETRO51");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        petro = subject.save(petro);

        Optional<Stock> result = subject.findById(petro.getId());

        assertTrue(result.isPresent());
        assertEquals(petro, result.get());
    }

    @Test
    public void whenTryToFindByName() {
        Stock petro = new Stock();
        petro.setName("PETRO51");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        subject.save(petro);

        Stock apple = new Stock();
        apple.setName("APL");
        apple.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        subject.save(apple);

        Stock apg = new Stock();
        apg.setName("APG");
        apg.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());

        apg = subject.save(apg);

        Pageable pageable = new PageRequest(0,10);

        Page<Stock> result = subject.findByNameStartsWith("AP", pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.getContent().contains(apg));
    }
}
