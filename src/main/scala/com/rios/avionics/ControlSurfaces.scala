package com.rios.avionics

import akka.actor.{Actor, ActorRef}
import com.rios.avionics.Altimeter.RateChange
import com.rios.avionics.ControlSurfaces.{StickBack, StickForward}

object ControlSurfaces {

  case class StickBack(amount: Float)
  case class StickForward(amount: Float)
}

class ControlSurfaces(altimeter: ActorRef) extends Actor {

  override def receive: Receive = {
    case StickBack(amount) =>
      altimeter ! RateChange(amount)

    case StickForward(amount) =>
      altimeter ! RateChange(-1 * amount)
  }
}
