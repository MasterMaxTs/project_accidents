package ru.job4j.accidents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный исполняемый класс приложения
 */
@SpringBootApplication
public class Job4jAccidentsApplication {

    /**
     * Главный исполняемый метод приложения Автонарушители
     * @param args массив из аргументов командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(Job4jAccidentsApplication.class, args);
        System.out.println("Go to http://localhost:8080/index");
    }
}
