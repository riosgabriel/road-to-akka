package com.rios.avionics

import java.util.concurrent.{CountDownLatch, TimeUnit}

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.rios.avionics.Altimeter.{AltitudeUpdate, RateChange}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpec, WordSpecLike}

object EventSourceSpy {

  val latch = new CountDownLatch(1)

}

trait EventSourceSpy extends EventSource {
  override def sendEvent[T](event: T): Unit = EventSourceSpy.latch.countDown()

  override def eventSourceReceive: Receive = {
    case "" =>
  }
}

class AltimeterSpec extends TestKit(ActorSystem("AltimeterSpec"))
  with ImplicitSender
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  override def afterAll() { system.terminate() }

  def slicedAltimeter = new Altimeter with EventSourceSpy

  def actor(): (TestActorRef[Altimeter], Altimeter) = {
    val a = TestActorRef[Altimeter](Props(slicedAltimeter))
    (a, a.underlyingActor)
  }

  "Altimeter" should {
    "record rate on climb changes" in {
      val (_, realActor) = actor()

      realActor.receive(RateChange(1))
      realActor.rateOfClimb must be (realActor.maxRateOfClimb)
    }

    "keep rate of climb changes within bounds" in {
      val (_, realActor) = actor()
      realActor.receive(RateChange(2.0f))
      realActor.rateOfClimb must be (realActor.maxRateOfClimb)
    }

    "calculate altitude changes" in {
      val refActor = system.actorOf(Props(Altimeter()))

      refActor ! EventSource.RegisterListener(testActor)
      refActor ! RateChange(1f)

      fishForMessage() {
        case AltitudeUpdate(altitude) if altitude == 0f => false
        case AltitudeUpdate(_) => true
      }
    }

    "send events" in {
      val (ref, _) = actor()

      EventSourceSpy.latch.await(1, TimeUnit.SECONDS) must be (true)
    }
  }


}
