package actors

import akka.actor.Actor
import business.ParallelComputer
import javax.inject.Inject

class ParallelComputerActor @Inject()(computer: ParallelComputer) extends Actor {

  def receive = {
    case cmd: ComputeCommand ⇒ computer.compute
  }
}