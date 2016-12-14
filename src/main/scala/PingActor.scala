import akka.actor.Actor
import akka.actor.Actor.Receive

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Zielony on 2016-12-14.
  */
class PingActor extends Actor {

  override def receive: Receive = {
    case Ping => {
      println("Ping")
      context
        .actorSelection("akka.tcp://PingPongSystem@127.0.0.1:5576/user/PongActor")
        .resolveOne(1 minutes)
        .foreach { pongActor =>
          pongActor ! Pong
        }
    }
    case other => throw new IllegalArgumentException(s"Unexpected message $other in Ping Actor")
  }
}
