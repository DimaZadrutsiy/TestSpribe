package test.api;

import io.restassured.module.jsv.JsonSchemaValidator;

import java.io.File;

public class BaseApiTest {

    protected JsonSchemaValidator checkSchema(String pathName) {
        return JsonSchemaValidator.matchesJsonSchema(new File(pathName));
    }
}
