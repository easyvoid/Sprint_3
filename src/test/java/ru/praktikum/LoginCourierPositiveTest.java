package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class LoginCourierPositiveTest {

    Courier courier = new Courier("densky", "password", "lalka");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        CourierClient.create(courier);
    }

    @Test
    public void loginCourierPositive() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                        .and()
                        .statusCode(200);
    }

    @After
    public void tearDown() {
        CourierClient.delete(CourierClient.login(courier));
    }
}