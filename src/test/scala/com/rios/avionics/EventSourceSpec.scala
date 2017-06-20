package com.rios.avionics

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit, ImplicitSender}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class TestEventSource extends Actor with ProductionEventSource {
  override def receive: Receive = eventSourceReceive
}

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with BeforeAndAfterAll {

  import EventSource._

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "EventSource" should {

    "allows us to register a listener" in {
      val realActor = TestActorRef[TestEventSource].underlyingActor
      realActor.receive(RegisterListener(testActor))
      realActor.listeners must contain(testActor)
    }

    "allows us to unregister a listener" in {
      val realActor = TestActorRef[TestEventSource].underlyingActor
      realActor.receive(RegisterListener(testActor))
      realActor.receive(UnregisterListener(testActor))
      realActor.listeners.size must be(0)
    }

    "send event to our test actor" in {
      val testA = TestActorRef[TestEventSource]
      testA ! RegisterListener(testActor)
      testA.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }

  }

}
