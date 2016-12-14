import akka.actor.{ActorRef, ActorSystem, Address, Props}
import akka.cluster.Cluster

import scala.collection.immutable.Seq
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Zielony on 2016-12-14.
  */
object AkkaClusterMain extends App {

  val port = args(0).toInt
  val role = Role.withName(args(1))

  val actorSystem = ActorSystem("PingPongSystem", AkkaConfig(port, Seq(SeedNode("localhost", 5575))))

  val cluster = Cluster(actorSystem)

  cluster.joinSeedNodes(Seq(Address("akka.tcp", "PingPongSystem", "127.0.0.1", 5575)))

  if(role == Role.Ping) {
    actorSystem.actorOf(Props[PingActor], "PingActor")
  }
  else if(role == Role.Pong) {
    actorSystem.actorOf(Props[PongActor], "PongActor")
  }
  else if(role == Role.Trigger) {
    val futurePingActor = actorSystem
      .actorSelection("akka.tcp://PingPongSystem@127.0.0.1:5575/user/PingActor")
      .resolveOne(1 minutes)

    val actorRef = Await.result(futurePingActor, 1 minutes)
    actorRef ! Ping
  }
  else throw new IllegalArgumentException("Incorrect role given")
}