package controllers

import javax.inject._
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class Controller @Inject()(
    cc: ControllerComponents,
    ws: WSClient)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def endpoint1() = Action.async { implicit request =>
    ws.url("http://s2:9000/endpoint2")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint2 replied"))
  }

  def endpoint2 = Action.async { implicit request =>
    ws.url( "http://s3:9000/endpoint3")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint3 replied"))
  }

  def endpoint3() = Action.async { implicit request =>
    Thread.sleep(1000)
    Future(Ok("called by called !"))
  }

}
