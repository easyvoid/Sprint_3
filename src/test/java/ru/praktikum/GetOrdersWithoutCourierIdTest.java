package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class GetOrdersWithoutCourierIdTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void getAvailableOrdersWithoutCourierId() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?limit=10&page=0");
        response.then().assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(200);

        ArrayList<String> orders = response.then().extract().body().path("orders");
        boolean actual = !orders.isEmpty();
        assertTrue(actual);

    }
}
