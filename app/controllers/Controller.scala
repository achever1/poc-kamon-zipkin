package controllers

import actors.{ComputeCommand, ParallelComputerActor}
import akka.actor.{ActorSystem, Props}
import business.ParallelComputer
import javax.inject._
import kamon.Kamon
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class Controller @Inject()(cc: ControllerComponents,
                           ws: WSClient,
                           parallelComputer: ParallelComputer,
                           system: ActorSystem)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  val props = Props(new ParallelComputerActor(parallelComputer))
  val actor = system.actorOf(props, "ComputerActor")

  def endpoint1() = Action.async { implicit request =>
    ws.url("http://s2:9000/endpoint2")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint2 replied"))
  }

  def endpoint2 = Action.async { implicit request =>
    // Compute with futures
    val span = Kamon.buildSpan("compute globally").start()
    parallelComputer.compute()
    span.finish()
    // Compute with futures in an actor
    actor ! ComputeCommand()

    ws.url("http://s3:9000/endpoint3")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint3 replied"))
  }

  def endpoint3() = Action.async { implicit request =>
    Thread.sleep(1000)
    Future(Ok("called by called !"))
  }

}
