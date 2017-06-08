package com.rios.avionics

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._

object Avionics {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("PlaneSimulationSystem")
  val planeActor = system.actorOf(Props[Plane])

  def main(args: Array[String]): Unit = {
    val control = Await.result(
      (planeActor ? Plane.GiveMeControl).mapTo[ActorRef],
      5 seconds
    )

    system.scheduler.scheduleOnce(200 millis) {
      control ! ControlSurfaces.StickBack(1f)
    }

    system.scheduler.scheduleOnce(1 second) {
      control ! ControlSurfaces.StickBack(0f)
    }

    system.scheduler.scheduleOnce(3 seconds) {
      control ! ControlSurfaces.StickBack(0.5f)
    }

    system.scheduler.scheduleOnce(4 seconds) {
      control ! ControlSurfaces.StickBack(0f)
    }

    system.scheduler.scheduleOnce(5 seconds) {
      system.terminate()
    }
  }

}
