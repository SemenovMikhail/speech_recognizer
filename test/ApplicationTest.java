import java.util.Map;
import java.util.Collections;
import org.junit.*;
import static org.mockito.Mockito.*;
import play.mvc.*;
import play.test.*;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.api.mvc.RequestHeader;
import static play.test.Helpers.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import controllers.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/

public class ApplicationTest {

    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        //Ebean.save((List) Yaml.load("test-data.yml"));
    }
    @Test
    public void testLathe() {
        Result result = callAction(
        controllers.routes.ref.Application.sendRequest("lathe_converted.wav"),
        fakeRequest());
        assertEquals(200, status(result));
        assertThat(contentAsString(result)).contains("Alade is a big tool ground every dish of sugar");
    }

    @Test
    public void testEngm() {
        Result result = callAction(
        controllers.routes.ref.Application.sendRequest("ENG_M.wav"),
        fakeRequest());
        assertEquals(200, status(result));
        assertThat(contentAsString(result)).contains("races" );
    }


}
