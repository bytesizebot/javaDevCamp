package za.co.entelect.java_devcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class JavaDevcampApplication {
	private static Logger logger = LoggerFactory.getLogger(JavaDevcampApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JavaDevcampApplication.class, args);
		logger.info("This is the Product Shop application");
	}

}
