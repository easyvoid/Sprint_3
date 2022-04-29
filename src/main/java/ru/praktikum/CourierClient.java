package ru.praktikum;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final RequestSpecification baseSpec = new RequestSpecBuilder()
            .setBaseUri("http://qa-scooter.praktikum-services.ru/")
            .setContentType("application/json")
            .log(LogDetail.ALL)
            .build();

    public static RequestSpecification getBaseSpec() {
        return baseSpec;
    }


    @Step("Запрос на создание курьера: {courier}")
    public static ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().log().all();
    }

    @Step("Запрос на логин курьера: {courier}, который возвращает его id")
    public static Integer login(Courier courier) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all();

        return response.and().extract().body().path("id");
    }

    @Step("Запрос на удаление курьера с id: {id}")
    public static void delete(Integer id) {
        given()
                .spec(getBaseSpec())
                .when()
                .delete("/api/v1/courier/{id}", id)
                .then().log().all();
    }
}
