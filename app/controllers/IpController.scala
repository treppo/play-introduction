package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}
import services.IpService

class IpController @Inject()(ipService: IpService) extends Controller {
  def getIp = Action.async {
    ipService.getIp.map { ipAddress =>
      Ok(ipAddress)
    }
  }
}