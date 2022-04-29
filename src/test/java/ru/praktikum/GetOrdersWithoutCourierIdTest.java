package ru.praktikum;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class GetOrdersWithoutCourierIdTest {

    @Test
    public void getAvailableOrdersWithoutCourierId() {
        int limit = 10;
        Response response = given()
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?limit={limit}&page=0", limit);
        response.then().assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(200);

        ArrayList<String> orders = response.then().extract().body().path("orders");
        boolean actual = orders.size() == limit;
        assertTrue(actual);
    }
}
