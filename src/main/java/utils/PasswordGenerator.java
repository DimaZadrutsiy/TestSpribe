package utils;

import com.github.javafaker.Faker;

public class PasswordGenerator {
    public static String getPassword() {
        Faker faker = new Faker();
        String password;

        do {
            password = faker.regexify("[a-zA-Z0-9]{7,}");
        } while (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*"));

        return password;
    }
}
