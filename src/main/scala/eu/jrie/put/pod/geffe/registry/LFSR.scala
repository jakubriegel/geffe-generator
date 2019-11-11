package eu.jrie.put.pod.geffe.registry

import scala.util.Random

abstract class LFSR (var register: List[Boolean]) {
  def this(size: Int) {
    this({
      val r = new Random()
      List.fill(size)(r.nextBoolean())
    })
  }

  protected def values: List[Boolean] = register

  /**
   * Calculates next value of the registry
   * @return next value of the registry
   */
  protected def calculateNext(): Boolean

  /**
   * Calculates and shifts next value of the registry
   * @return next value of the registry
   */
  def next(): Boolean = {
    register = register.tail ::: (calculateNext() :: Nil)
    register.last
  }
}

object LFSR {
  private val random = new Random()
  protected def randomBoolean(): Boolean = random.nextBoolean()
  def randomRegistry(size: Int): List[Boolean] = List.fill(size)(randomBoolean())
}
