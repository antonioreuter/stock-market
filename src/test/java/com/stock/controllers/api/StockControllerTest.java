package com.stock.controllers.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.domain.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;


    @Test
    public void whenTryToCreateANewStock() throws IOException {
        String payload =  "{\"name\": \"PETRO32\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> entity = new HttpEntity<>(payload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Stock result = objectMapper.readValue(response.getBody(), Stock.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(result.getId());
        assertEquals("PETRO32", result.getName());
    }

    @Test
    public void whenTryToCreateATwoStocksWithSameName() throws IOException {
        String payload =  "{\"name\": \"PETRO50\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> entity = new HttpEntity<>(payload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        String payload2 =  "{\"name\": \"PETRO50\", \"price\": {\"amount\": 100.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> entity2 = new HttpEntity<>(payload2, customHeaders());


        ResponseEntity<String> responseError = restTemplate.exchange(url, HttpMethod.POST, entity2, String.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseError.getStatusCode());
    }


    @Test
    public void whenTryToUpdateAnOutdateStock() throws IOException {
        String postPayload =  "{\"name\": \"PETRO30\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> newEntity = new HttpEntity<>(postPayload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, newEntity, String.class);
        Stock stock = objectMapper.readValue(response.getBody(), Stock.class);

        String putUrl = url + "/"+stock.getId();

        // First update
        String putPayload =  "{\"id\": "+ stock.getId() +", \"name\": \"PETRO30\", \"price\": {\"amount\": 100.99,  \"currency\": \"USD\"}, \"version\": 0}";
        HttpEntity<String> updateEntity = new HttpEntity<>(putPayload, customHeaders());
        response = restTemplate.exchange(putUrl, HttpMethod.PUT, updateEntity, String.class);

        // Second update
        String outdatedPayload = "{\"id\": "+ stock.getId() +", \"name\": \"PETRO30\", \"price\": {\"amount\": 50.99,  \"currency\": \"USD\"}, \"version\": 0}";
        HttpEntity<String> outdateEntity = new HttpEntity<>(outdatedPayload, customHeaders());
        response = restTemplate.exchange(putUrl, HttpMethod.PUT, outdateEntity, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void whenTryToUpdateAStockWithAMismatchId() throws IOException {
        String postPayload =  "{\"name\": \"PETRO20\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> newEntity = new HttpEntity<>(postPayload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, newEntity, String.class);
        Stock stock = objectMapper.readValue(response.getBody(), Stock.class);

        String putUrl = url + "/"+999;

        // First update
        String putPayload =  "{\"id\": "+ stock.getId() +", \"name\": \"PETRO20\", \"price\": {\"amount\": 100.99,  \"currency\": \"USD\"}, \"version\": 0}";
        HttpEntity<String> updateEntity = new HttpEntity<>(putPayload, customHeaders());
        response = restTemplate.exchange(putUrl, HttpMethod.PUT, updateEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenTryToFindAStockById() throws IOException {
        String payload =  "{\"name\": \"PETRO90\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> entity = new HttpEntity<>(payload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Stock newStock = objectMapper.readValue(response.getBody(), Stock.class);

        String getUrl = url + "/" +newStock.getId();
        response = restTemplate.getForEntity(getUrl, String.class);
        Stock foundedStock = objectMapper.readValue(response.getBody(), Stock.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newStock, foundedStock);
    }

    @Test
    public void whenTryToFindAStockByName() throws IOException {
        String payload =  "{\"name\": \"XXX901\", \"price\": {\"amount\": 999.99,  \"currency\": \"USD\"}}";
        HttpEntity<String> entity = new HttpEntity<>(payload, customHeaders());

        String url = "http://localhost:"+port+"/api/stocks";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Stock newStock = objectMapper.readValue(response.getBody(), Stock.class);

        String getUrl = url + "?name=XXX";
        response = restTemplate.getForEntity(getUrl, String.class);

        Page<Stock> foundedResultset = objectMapper.readValue(response.getBody(), new TypeReference<RestResponsePage<Stock>>(){});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, foundedResultset.getTotalPages());
        assertEquals(1, foundedResultset.getNumberOfElements());
    }

    private HttpHeaders customHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    @TestConfiguration
    static class Config {
        @Value("${api.username}")
        private String username;

        @Value("${api.password}")
        private String password;

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder()
                    .basicAuthorization(username, password);
        }
    }
}
