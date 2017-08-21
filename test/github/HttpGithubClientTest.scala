package github

import org.scalatestplus.play.PlaySpec
import org.treppo.mocoscala.dsl.Moco._
import play.api.test.WsTestClient

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class HttpGithubClientTest extends PlaySpec {

  "GithubClient" should {
    "fetch public AS24 repositories from Github" in {
      // setup
      val jsonString = "{}"
      val port = 8080
      val githubMock = server(port)
        .when {
          method("get")
        }
        .respond {
          text(jsonString)
        }

      githubMock.running {
        WsTestClient.withClient { client =>
          // execution
          val json = Await.result(new HttpGithubClient(client, s"http://localhost:$port").fetchRepos, 1.second)

          // verify
          json mustEqual Right(jsonString)
        }
      }
    }
  }
}
