import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание нового заказа")
    public ValidatableResponse createNewOrderWithColor(Object color) {
        return given()
                .contentType(ContentType.JSON)
                .body(color)
                .when()
                .post("/api/v1/orders")
                .then();
    }
    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList(int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders?courierId" + courierId)
                .then();

    }
}
