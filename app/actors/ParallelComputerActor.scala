package actors

import akka.actor.Actor
import business.ParallelComputer
import javax.inject.Inject
import kamon.Kamon
import play.api.Logger

class ParallelComputerActor @Inject()(computer: ParallelComputer)
    extends Actor {

  override def preStart(): Unit = {
    Logger.info(s"Starting local computer actor ${context.self.path}")
    super.preStart()
  }

  def receive = {
    case cmd: ComputeCommand ⇒
      val span = Kamon.buildSpan("compute globally").start()
      Logger.info("In computer actor - ComputeCommand")
      computer.compute()
      span.finish()
    case _ ⇒ Logger.info("In computer actor - default case")
  }
}
