import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello Vatro")
    }

    "render the hello page with a name using a path" in {
      val home = route(app, FakeRequest(GET, "/hello/bhavya")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello Bhavya")
    }

    "render the hello page with a name using a parameter" in {
      val home = route(app, FakeRequest(GET, "/hello?name=bhavya")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello Bhavya")
    }

    "render the hello page with a fallback when there is no parameter" in {
      val home = route(app, FakeRequest(GET, "/hello")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello World")
    }

    "render the hello page with a fallback when there is an empty parameter" in {
      val home = route(app, FakeRequest(GET, "/hello?name=")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello World")
    }

    "render the hello page with the first name when there is a list of values for the parameter" in {
      val home = route(app, FakeRequest(GET, "/hello?name=bhavya&name=anand")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Hello Bhavya")
    }
  }

  "CountController" should {
    "return an increasing count" in {
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "0"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "1"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "2"
    }
  }
}