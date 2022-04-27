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
    LoginId loginId;
    Integer id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier").then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
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
        loginId = response.body().as(LoginId.class);
        id = response.then().extract().body().path("id");
    }


    @After
    public void tearDown() {
        given()
                .header("Content-type", "application/json")
                .body(loginId)
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }
}