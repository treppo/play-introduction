package controllers

import github.{GithubGateway, Repository}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class ReposControllerTest extends PlaySpec with MockitoSugar {

  "ReposController" should {
    "display public Autoscout24 Github repositories" in {
      val githubClient = mock[GithubGateway]
      when(githubClient.publicAS24Repos).thenReturn(Future.successful(Right(Seq(Repository("repo_name", "language")))))

      val response = new ReposController(githubClient).publicRepos().apply(FakeRequest())

      contentAsString(response) must include("repo_name language")
    }

    "display an error if the client couldn't fetch the repositories" in {
      val githubClient = mock[GithubGateway]
      val errorMessage = "an error happened"
      when(githubClient.publicAS24Repos)
        .thenReturn(Future.successful(Left(errorMessage)))

      val response = new ReposController(githubClient).publicRepos().apply(FakeRequest())

      contentAsString(response) must include(errorMessage)
    }
  }
}

