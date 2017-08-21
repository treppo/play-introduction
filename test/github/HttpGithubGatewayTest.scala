package github

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source

class HttpGithubGatewayTest extends PlaySpec with MockitoSugar {

  "GithubGateway" should {
    "fetch all public AS24 repositories and parse the JSON" in {
      // setup
      val resource = getClass.getResource("/repositories.json")
      val jsonString = Source.fromURL(resource).mkString
      val client = mock[GithubClient]
      when(client.fetchRepos).thenReturn(Future.successful(Right(jsonString)))
      val gateway = new HttpGithubGateway(client)

      // exercise
      val repositories = Await.result(gateway.publicAS24Repos, 1.second)

      // verify
      repositories mustEqual
        Right(Seq(
          Repository("DWX-2014-Demo", "Objective-C"),
          Repository("featurebee-scala", "Scala")))
    }

    "return an error when JSON can't be parsed" in {
      val jsonString = "invalid json"
      val client = mock[GithubClient]
      when(client.fetchRepos).thenReturn(Future.successful(Right(jsonString)))
      val gateway = new HttpGithubGateway(client)

      val repositories = Await.result(gateway.publicAS24Repos, 1.second)

      repositories mustEqual Left("JSON could not be parsed")
    }

    "return an error when the items can not be extracted" in {
      val jsonString =
        """{ "items": [
          |  {
          |    "name": "repo name",
          |    "no-language": ""
          |  }
          |]
          |}""".stripMargin
      val client = mock[GithubClient]
      when(client.fetchRepos).thenReturn(Future.successful(Right(jsonString)))
      val gateway = new HttpGithubGateway(client)

      val repositories = Await.result(gateway.publicAS24Repos, 1.second)

      repositories mustEqual Left("Could not extract repositories from JSON")
    }

    "return an error when there are no items" in {
      val jsonString = """{ "items": [] }"""
      val client = mock[GithubClient]
      when(client.fetchRepos).thenReturn(Future.successful(Right(jsonString)))
      val gateway = new HttpGithubGateway(client)

      val repositories = Await.result(gateway.publicAS24Repos, 1.second)

      repositories mustEqual Left("There are no public repositories")
    }
  }
}
