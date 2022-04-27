package ru.praktikum;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CreateNewCourierPositiveAndSameLoginTest {

    Courier courier = new Courier("densky", "password", "lalka");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createNewCourierPositive() {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    public void createNewCourierWithSameLogin() {
        //запрос на создание курьера
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);

        //запрос на создание курьера с тем же логином
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }


    @After
    public void tearDown() {
        LoginId loginId = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login")
                .body().as(LoginId.class);

        given()
                .header("Content-type", "application/json")
                .body(loginId)
                .when()
                .delete("/api/v1/courier/" + loginId.getId())
                .then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }
}