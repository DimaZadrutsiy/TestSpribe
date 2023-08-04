package utils;

import com.github.javafaker.Faker;

public class NameGenerator {
    private static final Faker faker = new Faker();

    public static String getFullName() {
        return faker.name().fullName();
    }

    public static String getName() {
        return faker.name().firstName();
    }

    public static String getNickname() {
        return faker.funnyName().name();
    }
}