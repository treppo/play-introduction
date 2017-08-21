package services

import com.google.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

trait IpService {
  def getIp(implicit ec: ExecutionContext): Future[String]
}

class ICanHazIpService @Inject()(wsClient: WSClient, config: play.api.Configuration) extends IpService {
  def getIp(implicit ec: ExecutionContext): Future[String] =
    wsClient
      .url(config.getString("ipServiceAddress").getOrElse("http://icanhazip.com"))
      .get()
      .map { _.body }
}