import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.File;
import static org.hamcrest.Matchers.is;

    @RunWith(Parameterized.class)
    public class CreateOrderTest {
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
            createCourier
                    .createNewCourier();
            Integer track = createOrder.createNewOrderWithColor(color)
                    .statusCode(201).extract().body().path("track");
            System.out.println(track);
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
