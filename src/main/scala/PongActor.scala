import akka.actor.Actor
import akka.actor.Actor.Receive

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Zielony on 2016-12-14.
  */
class PongActor extends Actor {

  override def receive: Receive = {
    case Pong => {
      println("Pong")
      context
        .actorSelection("akka.tcp://PingPongSystem@127.0.0.1:5575/user/PingActor")
        .resolveOne(1 minutes)
        .foreach { pingActor =>
          pingActor ! Ping
        }
    }
    case other => throw new IllegalArgumentException(s"Unexpected message $other in Pong Actor")
  }
}
