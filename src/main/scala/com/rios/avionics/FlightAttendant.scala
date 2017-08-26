package com.rios.avionics

import akka.actor.Actor
import com.rios.avionics.FlightAttendant.{Drink, GetDrink}

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait AttendantResponsiveness {

  val maxResponseTimeMS: Int
  def responseDuration = Random.nextInt(maxResponseTimeMS).millis
}

object FlightAttendant {

  case class GetDrink(drinkName: String)
  case class Drink(drinkName: String)

  def apply: FlightAttendant = new FlightAttendant() with AttendantResponsiveness {
    override val maxResponseTimeMS: Int = 300000
  }
}

class FlightAttendant extends Actor { this: AttendantResponsiveness =>

  override def receive: Receive = {
    case GetDrink(drinkName) =>
      val senderRef = sender
      context.system.scheduler.scheduleOnce(responseDuration, senderRef, Drink(drinkName))
  }
}