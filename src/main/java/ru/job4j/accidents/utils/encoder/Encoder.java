package ru.job4j.accidents.utils.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Утилитарный класс, позволяющий просматривать закодированные пароли
 * разными кодировщиками
 */
public class Encoder {

    /**
     * Показывает закодированный пароль BCrypt-кодировщиком
     * @param password входной тестовый пароль
     */
    public static void showEncodedPasswordByBcrypt(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.print(encoder.encode(password));
    }

    /**
     * Главный исполняемый метод для утилитарного класса
     * @param args массив из аргументов командной строки
     */
    public static void main(String[] args) {
        showEncodedPasswordByBcrypt("secret");
    }
}
