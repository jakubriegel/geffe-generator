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
  private def next(): Boolean = (a3 && a1) || (!a1 && a2)

  def get(): LazyList[Boolean] = {
//    (1 to n) foreach (_ => {
//      print(if (next()) 1 else 0)
//    })

    LazyList.fill(n) { next() }
  }

}
