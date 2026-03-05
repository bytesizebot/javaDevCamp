package za.co.entelect.devcamp.productservice;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;

public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        Logger logger = LoggerFactory.getLogger("Product Service");

        logger.info("This is the Products application.");
    }
}