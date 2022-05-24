package ru.praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


public class CreateNewCourierPositiveAndSameLoginTest {

    Courier courier = new Courier("densky", "password", "lalka");

    @Test
    public void createNewCourierPositive() {
        ValidatableResponse response = CourierClient.create(courier);
        response.assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    public void createNewCourierWithSameLogin() {
        //запрос на создание курьера
        CourierClient.create(courier);

        //запрос на создание курьера с тем же логином
        ValidatableResponse responseWithSameLogin = CourierClient.create(courier);
        responseWithSameLogin.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @After
    public void tearDown() {
        CourierClient.delete(CourierClient.login(courier));
    }
}