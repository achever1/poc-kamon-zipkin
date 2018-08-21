package actors

import akka.actor.Actor
import business.ParallelComputer
import javax.inject.Inject
import kamon.Kamon

class ParallelComputerActor @Inject()(computer: ParallelComputer)
    extends Actor {

  def receive = {
    case cmd: ComputeCommand ⇒
      val span = Kamon.buildSpan("compute globally").start()
      computer.compute
      span.finish()
  }
}