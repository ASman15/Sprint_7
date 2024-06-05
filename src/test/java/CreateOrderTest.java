import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.File;
import static org.hamcrest.Matchers.is;

    @RunWith(Parameterized.class)
    public class CreateOrderTest {
        String login;
        String password = "1234";
        String firstName = "altair";
        private final CourierSteps createCourier = new CourierSteps();
        private final OrderSteps createOrder = new OrderSteps();
        File color;
        static File blackAndGrey = new File ("src/test/java/resources/newOrderWithBlackAndGrey.json");
        static File black = new File ("src/test/java/resources/newOrderWithBlack.json");
        static File grey = new File ("src/test/java/resources/newOrderWithGrey.json");
        static File withoutColors = new File ("src/test/java/resources/newOrderWithoutColor.json");
        @Parameterized.Parameters
        public static Object[][] colors() {
            return new Object[][] {
                    {blackAndGrey},
                    {black},
                    {grey},
                    {withoutColors}
            };
        }
        public CreateOrderTest(File color) {
            this.color = color;
        }
        @Before
        public void setUp() {
            RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        }
        @Test
        public void createOdrerReturnTrack(){
            login = RandomStringUtils.randomAlphabetic(10);
            createCourier
                    .createNewCourier(login, password, firstName);
            Integer track = createOrder.createNewOrderWithColor(color)
                    .statusCode(201).extract().body().path("track");
            System.out.println(track);
        }
        @After
        public void tearDown() {
            Integer courierId = createCourier.loginNewCourier(login, password).extract().body().path("id");
            if (courierId != null) {
                createCourier.deleteCourier(courierId).statusCode(200).and().body("ok", is(true));
            }
        }
}
