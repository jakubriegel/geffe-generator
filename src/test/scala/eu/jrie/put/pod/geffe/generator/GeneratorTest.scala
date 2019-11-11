package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import org.scalatest.FunSuite

class GeneratorTest extends FunSuite {
  test("should generate stream without long series") {
    // execute test multiple times
    (0 to 5) foreach(_ => {
      // given
      val g = new Generator(
        20_000,
        new Xor(LFSR.randomRegistry(30), LFSR.randomRegistry(30)),
        new Xor(LFSR.randomRegistry(30), LFSR.randomRegistry(30)),
        new Fibonacci(LFSR.randomRegistry(50)),
      )

      // when
      val stream = g.get()

      // then
      var previous = stream.head
      var n = 0
      var con = true
      stream foreach { b => {
        if (b == previous) n += 1
        else {
          println(n)
          if (n >= 26) con = false
          else n = 1
        }
        previous = b
      } }
      assert(con, "the stream should not contain long series")
    })
  }

  test("should pass poker test") {
    // execute test multiple times
    (0 to 5) foreach(_ => {
      // given
      val g = new Generator(
        20_000,
        new Xor(LFSR.randomRegistry(5), LFSR.randomRegistry(5)),
        new Xor(LFSR.randomRegistry(5), LFSR.randomRegistry(5)),
        new Fibonacci(LFSR.randomRegistry(500)),
      )

      // when
      val stream = g.get().toList

      // then
      val equation = { s: Float => 16.0/5000.0 * s - 5000.0 }
      val s: Int = stream.sliding(4, 4).toList
        .groupBy(identity).view
        .mapValues(_.size).values
        .map(i => i*i)
        .sum

      val testResult = equation(s)
      assert(testResult > 2.16)
      assert(testResult < 46.17)
    })
  }
}
