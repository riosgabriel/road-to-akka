package com.rios.sample.actors

import akka.actor.Actor
import com.rios.sample.model.DoSomething

/**
  * Created by gabriel on 6/2/17.
  */
class SomeActorConcurrent extends Actor {

  override def receive: Receive = {
    case x: DoSomething => x.doSomething()
  }
}
