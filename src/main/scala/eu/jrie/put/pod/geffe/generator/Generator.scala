package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.LFSR

/**
 * The Geffe Generator
 * @param n number of bits to generate
 */
class Generator (
                  n: Int = 100,
                  private val lfsr1: LFSR,
                  private val lfsr2: LFSR,
                  private val lfsr3: LFSR,
                ) {
  private def a1: Boolean = lfsr1.next()
  private def a2: Boolean = lfsr2.next()
  private def a3: Boolean = lfsr3.next()

  /**
   * Generates next bit of the stream
   * @return next bit of the stream
   */
  private def next(): Boolean =  {
    val a = a1
    val b = a2
    val c = a3
    (c && a) || (!a && b)
  }

  def get(): LazyList[Boolean] = {
    LazyList.fill(n) { next() }
  }

}
