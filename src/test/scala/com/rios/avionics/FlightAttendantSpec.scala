package com.rios.avionics

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.rios.avionics.FlightAttendant.{Drink, GetDrink}
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpecLike}

object TestFlightAttendant {
  def apply() = new FlightAttendant with AttendantResponsiveness {
    val maxResponseTimeMS = 1
  }
}

class FlightAttendantSpec extends TestKit(ActorSystem("FlightAttendantSpec",
  ConfigFactory.parseString("akka.scheduler.tick-duration = 1ms")))
  with ImplicitSender
  with WordSpecLike
  with MustMatchers {

  "FlightAttendant" should {
    "get a drink when asked" in {
      val testAttendant = TestActorRef(Props(TestFlightAttendant.apply()))
      testAttendant ! GetDrink("Beer")
      expectMsg(Drink("Beer"))
    }
  }
}
