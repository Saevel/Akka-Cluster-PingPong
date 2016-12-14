import com.typesafe.config.ConfigFactory

/**
  * Created by Zielony on 2016-12-14.
  */
object AkkaConfig {

  def apply(port: Int, seedNodes: Traversable[SeedNode]) = ConfigFactory.parseString (
    s"""akka {
          actor {
            provider = cluster
          }
          remote.netty.tcp {
            hostname = "127.0.0.1"
            port = $port
          }
          cluster {
            seed-nodes:[
              "akka.tcp://PingPongSystem@127.0.0.1:5575",
              "akka.tcp://PingPongSystem@127.0.0.1:5576"
            ]
          }

    }"""
  )
}

case class SeedNode(host: String, port: Int)
