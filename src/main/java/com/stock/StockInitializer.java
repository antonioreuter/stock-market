package com.stock;

import com.stock.domain.Currency;
import com.stock.domain.CurrencyCode;
import com.stock.domain.Stock;
import com.stock.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class StockInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private StockService stockService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Stock petro = new Stock();
        petro.setName("PETRO");
        petro.setPrice(Currency.builder().amount(BigDecimal.valueOf(50.67D)).currencyCode(CurrencyCode.BRL).build());
        stockService.save(petro);

        Stock petro2 = new Stock();
        petro2.setName("PETRO2");
        petro2.setPrice(Currency.builder().amount(BigDecimal.valueOf(71.37D)).currencyCode(CurrencyCode.BRL).build());
        stockService.save(petro2);

        Stock apple = new Stock();
        apple.setName("APPLE");
        apple.setPrice(Currency.builder().amount(BigDecimal.valueOf(98.57D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(apple);

        Stock oracle = new Stock();
        oracle.setName("ORCL");
        oracle.setPrice(Currency.builder().amount(BigDecimal.valueOf(72.8D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(oracle);

        Stock amazon = new Stock();
        amazon.setName("AMZN");
        amazon.setPrice(Currency.builder().amount(BigDecimal.valueOf(102.8D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(amazon);

        Stock google = new Stock();
        google.setName("GOGL");
        google.setPrice(Currency.builder().amount(BigDecimal.valueOf(94.8D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(google);

        Stock tesla = new Stock();
        tesla.setName("TSL");
        tesla.setPrice(Currency.builder().amount(BigDecimal.valueOf(68.92D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(tesla);

        Stock booking = new Stock();
        booking.setName("BKNG");
        booking.setPrice(Currency.builder().amount(BigDecimal.valueOf(78.34D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(booking);

        Stock ikea = new Stock();
        ikea.setName("IKA");
        ikea.setPrice(Currency.builder().amount(BigDecimal.valueOf(45.62D)).currencyCode(CurrencyCode.USD).build());
        stockService.save(ikea);
    }
}
