package actors

import akka.actor.Actor
import play.api.Logger

class SingletonRemoteActor extends Actor {

  override def preStart(): Unit = {
    Logger.info(s"Starting cluster singleton actor ${context.self.path}")
    super.preStart()
  }

  def receive = {
    case cmd: ComputeCommand ⇒
      Logger.info("In Singleton - ComputeCommand")
      val localActor = context.actorSelection("/user/ComputerActor")
      localActor ! cmd
    case _ ⇒ Logger.info("In Singleton - default case")
  }
}
