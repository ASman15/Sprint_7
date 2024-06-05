import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierSteps {
    @Step ("Создаём курьера")
    public ValidatableResponse createNewCourier(String login, String password, String firstName) {
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}")
                        .when()
                        .post("/api/v1/courier").then();
    }
    @Step ("Авторизация курьера")
    public ValidatableResponse loginNewCourier(String login, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/api/v1/courier/login").then();
    }
    @Step ("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then();
    }
}
