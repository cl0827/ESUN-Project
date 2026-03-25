package ESUN.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hello{
    public static void main(String[] args) {
        // http://127.0.0.1:8080/
        SpringApplication.run(Hello.class, args);
    }
}