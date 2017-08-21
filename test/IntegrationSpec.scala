import org.scalatestplus.play._
import org.treppo.mocoscala.dsl.Moco._

/**
  * add your integration spec here.
  * An integration test will fire up a whole play application in a real (or headless) browser
  */
class IntegrationSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {

  "Application" should {

    "work from within a browser" in {
      go to ("http://localhost:" + port)

      pageSource must include("Hello Vatro")
    }

    "greet a user using a path" in {
      go to s"http://localhost:$port/hello/bhavya"

      pageSource must include("Hello Bhavya")
    }

    "greet a user using parameters" in {
      go to s"http://localhost:$port/hello?name=bhavya"

      pageSource must include("Hello Bhavya")
    }

    "show user's ip address" in {
      val ipAddress = "192.168.155.155"
      val ipServerMock = server(8080)
        .when {
          uri("/") and method("get")
        }
        .respond {
          text(ipAddress)
        }

      ipServerMock.running {
        go to s"http://localhost:$port/ip"

        pageSource must include(ipAddress)
      }
    }

    "show all Autoscout24 public Github repositories" in {
      val reposJson = getClass.getResource("/repositories.json").getPath
      val githubMock = server(8081)
        .when {
          uri("/") and method("get")
        }
        .respond {
          file(reposJson)
        }

      githubMock.running {
        go to s"http://localhost:$port/repos"

        pageSource must include("DWX-2014-Demo Objective-C")
        pageSource must include("featurebee-scala Scala")
      }
    }
  }
}
