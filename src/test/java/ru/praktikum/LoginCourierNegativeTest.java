package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierNegativeTest {

    Courier courier = new Courier("densky", "password", "lalka");

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
    public void loginCourierWithoutLogin() {
        given()
                .header("Content-type", "application/json")
                .body("{\"password\":\"password\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void loginCourierWithoutPassword() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"densky\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void loginNonexistentCourier() {
        String login = RandomStringUtils.randomAlphabetic(9);
        String password = RandomStringUtils.randomAlphabetic(9);
        Courier courier = new Courier(login, password);

        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
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
