package ru.praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetCourierOrdersNegativeTest {

    Courier courier = new Courier("densky", "password", "lalka");

    @Test
    public void getCourierOrdersListTest() {

        CourierClient.createCourier(courier);
        ValidatableResponse rs = CourierClient.loginCourier(courier);
        Integer id = rs.extract().body().path("id");
        CourierClient.deleteCourier(courier);

        given()
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?courierId={id}", id)
                .then().assertThat().body("message", equalTo("Курьер с идентификатором " + id + " не найден"))
                .and()
                .statusCode(404);
    }
}
