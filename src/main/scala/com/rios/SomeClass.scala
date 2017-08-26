package com.rios

trait Mockable {

  def doSomething(): Int = 1
}

class SomeClass { this: Mockable =>

  def print(): Unit = println(doSomething())

}

object Test extends App {

  val someClass = new SomeClass with Mockable {
    override def doSomething(): Int = 2
  }

  val otherClass = new SomeClass with Mockable

  someClass.print() // 2
  otherClass.print() // 1
}
