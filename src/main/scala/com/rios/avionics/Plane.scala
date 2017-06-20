package com.rios.avionics

import akka.actor.{Actor, ActorLogging, Props}
import com.rios.avionics.Altimeter.AltitudeUpdate
import com.rios.avionics.EventSource.RegisterListener
import com.rios.avionics.Plane.GiveMeControl

object Plane {

  case object GiveMeControl
}

class Plane extends Actor with ActorLogging {

  val altimeter = context.actorOf(Props(Altimeter()))

  val controls = context.actorOf(Props(classOf[ControlSurfaces], altimeter))

  override def receive: Receive = {
    case GiveMeControl =>
      log.info(s"ActorRef = $sender")
      log.info("Plane giving control")
      sender ! controls
      sender forward controls

    case AltitudeUpdate(altitude) =>
      log.info(s"Altitude is now: $altitude")
  }

  override def preStart(): Unit = {
    altimeter ! RegisterListener(self)
  }
}
