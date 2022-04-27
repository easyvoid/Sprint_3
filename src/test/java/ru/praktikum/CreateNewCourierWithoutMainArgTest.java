package ru.praktikum;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateNewCourierWithoutMainArgTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createNewCourierWithoutPassword() {
        //запрос на создание курьера без пароля
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"densky\"}")
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    public void createNewCourierWithoutLogin() {
        //запрос на создание курьера без логина
        given()
                .header("Content-type", "application/json")
                .body("{\"password\":\"password\"}")
                .when()
                .post("/api/v1/courier")
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
