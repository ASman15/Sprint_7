import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class CouirerCreateTest {
    private final CourierSteps createCourier = new CourierSteps();
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void createNewCourierSucesfell() {
        createCourier
                .createNewCourier().statusCode(201).and().body("ok", is(true));
    }
    @Test
    public void notAllowedToCreateTwoIdenticalCouriers() {
        createCourier
                .createNewCourier().statusCode(201);
        createCourier
                .createNewCourier().statusCode(409);
    }

    @Test
    public void createCourierWithoutPassword(){
        String courierData = "{\"login\": \"asassin\", \"firstName\": \"altair\"}";
        createCourier
                .createCourierWithData(courierData).statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @Test
    public void createCourierWithoutLogin() {
        String courierData = "{\"password\": \"1234\", \"firstName\": \"altair\"}";
        createCourier
                .createCourierWithData(courierData).statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @Test
    public void createCourierWithRepetitiveLogin() {
        String courierData = "{\"login\": \"asassin\",\"password\": \"1111\", \"firstName\": \"ecio\"}";
        createCourier
                .createNewCourier().statusCode(201);
        createCourier
                .createCourierWithData(courierData).statusCode(409)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }
    @After
    public void tearDown() {
        RestAssured.filters(new RequestLoggingFilter(), new RequestLoggingFilter());
        Integer courierId = createCourier.loginNewCourier().extract().body().path("id");
        if (courierId != null) {
            createCourier.deleteCourier(courierId).statusCode(200).and().body("ok", is(true));
        }
    }
}
