package com.rios.sample.actors

import akka.actor.Actor

class BadShakespeareanActor extends Actor {

  def receive: Receive = {
    case "Good Morning" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")

    case "You're Terrible" =>
      println("Him: Yup")
  }
}
