package controllers

import javax.inject._

import play.api._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok
  }

  def hello(name: String) = Action {
    Ok
  }

  def hi = Action { request: Request[AnyContent] =>
    def name(request: Request[AnyContent]): String = {
      val fallbackName = "World"
      request.queryString.get("name") match {
        case Some(Seq()) => fallbackName
        case Some(Seq("")) => fallbackName
        case Some(Seq(n)) => n.capitalize
        case Some(Seq(n, xs@_*)) => n.capitalize
        case None => fallbackName
      }
    }

    Ok
  }
}
