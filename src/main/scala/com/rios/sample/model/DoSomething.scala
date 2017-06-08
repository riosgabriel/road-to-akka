package com.rios.sample.model

case class DoSomething(withThis: String) {

  def doSomething(): Unit = {
    println(withThis)
  }
}
