package controllers

import java.util.concurrent.TimeUnit

import actors.{ComputeCommand, ParallelComputerActor, SingletonRemoteActor}
import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.util.Timeout
import business.ParallelComputer
import javax.inject._
import kamon.Kamon
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, duration}

@Singleton
class Controller @Inject()(cc: ControllerComponents,
                           ws: WSClient,
                           parallelComputer: ParallelComputer,
                           system: ActorSystem)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  implicit val timeout = Timeout(duration.FiniteDuration(10, TimeUnit.SECONDS))
  val props = Props(new ParallelComputerActor(parallelComputer))
  val actor = system.actorOf(props, "ComputerActor")

  val actorSingleton = system.actorOf(
    ClusterSingletonManager.props(
      singletonProps = Props(classOf[SingletonRemoteActor]),
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system)
    ),
    name = "singletonRemoteComputerActor"
  )

  val actorProxy = system.actorOf(
    ClusterSingletonProxy.props(
      singletonManagerPath = "/user/singletonRemoteComputerActor",
      settings = ClusterSingletonProxySettings(system)),
    name = "singletonProxy"
  )

  def endpoint1() = Action.async { implicit request =>
    // Compute with futures on a remote cluster node
    actorProxy ! ComputeCommand()

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

    // Compute with futures on a remote cluster node
    actorProxy ! ComputeCommand()

    ws.url("http://s3:9000/endpoint3")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint3 replied"))
  }

  def endpoint3() = Action.async { implicit request =>
    // Compute with futures on a remote cluster node
    actorProxy ! ComputeCommand()

    Thread.sleep(1000)
    Future(Ok("Synchrone process done!"))
  }

}
