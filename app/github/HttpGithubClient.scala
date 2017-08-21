package github

import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class HttpGithubClient(wsClient: WSClient, url: String) extends GithubClient {
  def fetchRepos(implicit ec: ExecutionContext): Future[Either[String, String]] =
    wsClient.url(url).get().map { response =>
      Right(response.body)
    }
}
