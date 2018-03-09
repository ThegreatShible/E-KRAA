package authTest;

import org.junit.Test;
import play.test.Helpers;
import play.test.WithApplication;

public class TestController extends WithApplication {
    @Test
    public void testAuthentication(){
        Helpers.running(Helpers.fakeApplication());
    }
}
