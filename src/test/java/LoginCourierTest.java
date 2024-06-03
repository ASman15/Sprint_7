import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class LoginCourierTest {
    private final CourierSteps createCourier = new CourierSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void isCourierCanAutorize() {
        createCourier.createNewCourier();
        createCourier.loginNewCourier().statusCode(200);
    }
    @Test
    public void courierCanNotAutorizedWithoutLogin() {
        createCourier.createNewCourier();
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body("{\"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login")
                        .then().statusCode(400)
                        .body("message", is("Недостаточно данных для входа"));
    }
    @Test
    public void courierCanNotAutorizedWithoutPassword() {
        createCourier.createNewCourier();
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body("{\"login\": \"asassin\", \"password\": \"\"}")
                        .when()
                        .post("/api/v1/courier/login").then()
                        .statusCode(400)
                        .body("message", is("Недостаточно данных для входа"));
    }
    @Test
    public void courierCanNotAutorizedIfLogAndPassIncorrect() {
        createCourier.createNewCourier();
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body("{\"login\": \"ninja\", \"password\": \"1111\"}")
                        .when()
                        .post("/api/v1/courier/login").then()
                        .statusCode(404)
                        .body("message", is("Учетная запись не найдена"));
    }
    @Test
    public void idIsReturning() {
        createCourier.createNewCourier();
        Integer id = createCourier.loginNewCourier().extract().body().path("id");
        System.out.println(id);
    }


    @After
    public void tearDown() {
        Integer id = createCourier.loginNewCourier().extract().body().path("id");
        if (id != null) {
            createCourier.deleteCourier(id).statusCode(200).and().body("ok", is(true));
        }
    }
}
