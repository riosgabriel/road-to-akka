package com.rios.avionics

import akka.actor.{Actor, ActorRef}
import com.rios.avionics.EventSource.{RegisterListener, UnregisterListener}

object EventSource {

  case class RegisterListener(listener: ActorRef)
  case class UnregisterListener(listener: ActorRef)

}

trait EventSource { this: Actor =>

  var listeners = Vector.empty[ActorRef]

  def sendEvent[T](event: T): Unit = listeners foreach(_ ! event)

  def eventSourceReceive: Receive = {
    case RegisterListener(listener) =>
      listeners = listeners :+ listener

    case UnregisterListener(listener) =>
      listeners = listeners.filter(_ != listener)
  }
}

