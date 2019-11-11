package eu.jrie.put.pod.geffe

import eu.jrie.put.pod.geffe.generator.Generator
import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}

object Main {
  def main(args: Array[String]): Unit = {

    val lfsr1 = new Xor(LFSR.randomRegistry(25), LFSR.randomRegistry(25))
    val lfsr2 = new Xor(LFSR.randomRegistry(25), LFSR.randomRegistry(25))
    val lfsr3 = new Fibonacci(LFSR.randomRegistry(25))

    val l = new Generator(100, lfsr1, lfsr2, lfsr3).get()
    val a = l.sliding(2)
  }
}
