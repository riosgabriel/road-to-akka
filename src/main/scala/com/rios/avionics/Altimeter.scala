package com.rios.avionics

import akka.actor.{Actor, ActorLogging, Cancellable}
import com.rios.avionics.Altimeter.{AltitudeUpdate, RateChange}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Altimeter {

  case class RateChange(amount: Float)
  case class AltitudeUpdate(altitude: Double)

  def apply() = new Altimeter with ProductionEventSource
}

class Altimeter extends Actor with ActorLogging { this: EventSource =>

  // in feet
  val ceiling = 43000

  val maxRateOfClimb: Int = 5000

  var rateOfClimb: Float = 0

  var altitude: Double = 0

  var lastTick: Long = System.currentTimeMillis()

  val ticker: Cancellable = context.system.scheduler.schedule(100 millis, 100 millis, self, Tick)

  case object Tick

  def altimeterReceive: Receive = {
    case RateChange(amount) =>
      rateOfClimb = amount.min(1.0f).max(-1.0f) * maxRateOfClimb
      log.info(s"Altimeter changed rate of climb to $rateOfClimb.")

    case Tick =>
      val tick = System.currentTimeMillis
      altitude = altitude + ((tick - lastTick) / 60000.0) * rateOfClimb
      sendEvent(AltitudeUpdate(altitude))
      lastTick = tick
  }

  override def receive: Receive = eventSourceReceive orElse altimeterReceive

  override def postStop(): Unit = ticker.cancel()

}


