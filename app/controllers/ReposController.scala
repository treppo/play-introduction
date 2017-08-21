package controllers

import javax.inject.Inject

import github.GithubGateway
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class ReposController @Inject()(github: GithubGateway) extends Controller {

  def publicRepos() = Action.async {
    github.publicAS24Repos.map {
      case Left(error) => Ok(error)
      case Right(repositories) => Ok {
        repositories
          .map { repository => s"${repository.name} ${repository.language}" }
          .mkString(" ")
      }
    }
  }
}