package github

import scala.concurrent.{ExecutionContext, Future}

trait GithubClient {
  def fetchRepos(implicit ec: ExecutionContext): Future[Either[String, String]]
}
