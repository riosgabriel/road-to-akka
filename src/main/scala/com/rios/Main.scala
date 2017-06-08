package com.rios

import akka.actor.{ActorSystem, Props}
import com.rios.sample.actors.SomeActorConcurrent
import com.rios.sample.model.DoSomething

object Main extends App {

  val system = ActorSystem("BadShakespearean")
  val actor = system.actorOf(Props[SomeActorConcurrent])

  actor ! DoSomething
  println("Hi world")

  system.terminate()
}
