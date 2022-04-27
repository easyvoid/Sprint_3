package ru.praktikum;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetOrdersWithoutCourierIdTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void getAvailableOrdersWithoutCourierId() {
        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?limit=10&page=0").then().assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(200);
    }
}
