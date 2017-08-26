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

  // AWS-OpsWorks-Custom-Server,AWS-OpsWorks-Default-Server,databases,awseb-e-pbvg43kgcz-stack-AWSEBSecurityGroup-ZD2Z9I4548U0

  // postgres://devs:99taxis2117@promotions-staging.cqela5ipg9g5.us-east-1.rds.amazonaws.com:5432/promotions
  // postgres://devs:99taxis2117@promotions-staging.cqela5ipg9g5.us-east-1.rds.amazonaws.com:5432/promotions
}
