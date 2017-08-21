package github

import javax.inject.Inject

import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait GithubGateway {
  def publicAS24Repos(implicit ec: ExecutionContext): Future[Either[String, Seq[Repository]]]
}

class HttpGithubGateway @Inject()(client: GithubClient) extends GithubGateway {
  def publicAS24Repos(implicit ec: ExecutionContext): Future[Either[String, Seq[Repository]]] = {
    client.fetchRepos.map {
      case Left(error) => Left("")
      case Right(jsonString) => parse(jsonString)
    }
  }

  private def parse(jsonString: String): Either[String, Seq[Repository]] = {
    implicit val repositoryRead: Reads[Repository] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "language").read[String]
      ) (Repository.apply _)

    val json = Try(Json.parse(jsonString))
    json match {
      case Success(jsValue) =>
        val items = (jsValue \ "items").validate[Seq[Repository]]
        items match {
          case JsError(_) => Left("Could not extract repositories from JSON")
          case JsSuccess(repositories@Seq(), _) =>
            Left("There are no public repositories")
          case JsSuccess(repositories, _) => Right(repositories)
        }

      case Failure(exception) => Left("JSON could not be parsed")
    }
  }
}