package controllers

import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.IpService

import scala.concurrent.{ExecutionContext, Future}

class IpControllerTest extends PlaySpec with MockitoSugar {

  "IpController" should {
    "return the looked up IP address" in {
      val ipService = mock[IpService]
      val controller = new IpController(ipService)
      val ipAddress = "192.168.0.1"
      when(ipService.getIp(any(classOf[ExecutionContext]))).thenReturn(Future.successful(ipAddress))

      val result = controller.getIp.apply(FakeRequest())

      status(result) mustEqual OK
      contentAsString(result) mustEqual ipAddress
    }
  }
}
