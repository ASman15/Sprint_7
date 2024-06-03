import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    public ValidatableResponse createNewOrderWithColor(Object color) {
        return given()
                .contentType(ContentType.JSON)
                .body(color)
                .when()
                .post("/api/v1/orders")
                .then();
    }
    public ValidatableResponse getOrderList(int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders?courierId=" + courierId)
                .then();
    }
    public ValidatableResponse deleteOrder(int orderId) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"track\": \"" + orderId + "\"}")
                .when()
                .put("/api/v1/orders/cancel")
                .then();
    }
}
