package business

import akka.actor.ActorSystem
import javax.inject.Inject
import kamon.Kamon

import scala.concurrent.{ExecutionContext, Future}

class ParallelComputer @Inject()(system: ActorSystem)(
    implicit ec: ExecutionContext) {

  def compute() =
    Future
      .sequence {
        (1 to 5)
          .map { i =>
            {
              val span = Kamon.buildSpan("compute locally").start()
              val f = Future(i)
                .map(_ * 10)
                .map(_ + 1)
                .map { i => Thread.sleep(100); i }
              span.finish()
              f
            }
          }
      }
      .map(_.sum)
}
