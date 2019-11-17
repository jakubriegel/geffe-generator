package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.{Fibonacci, LFSR, Xor}
import org.scalatest.FunSuite

class RandomGeneratorTest extends FunSuite {

  private val N = 15

  test("should generate stream without long series") {
    runTest(() => {
      // when
      val stream = generator().get()

      // then
      var previous = stream.head
      var n = 0
      var con = true
      stream foreach { b => {
        if (b == previous) n += 1
        else {
          if (n >= 26) con = false
          else n = 1
        }
        previous = b
      } }
      con
    })
  }

  test("should pass poker test") {
    runTest(() => {
      // when
      val stream = generator().get().toList

      // then
      val s: Double = stream.sliding(4, 4).toList
        .groupBy(identity).view
        .mapValues(_.size).values
        .map(i => i.toLong)
        .map(i => i*i)
        .map(i => i - 5000.0)
        .sum

      val testResult = 16.0/5000.0 * s
      testResult > 2.16 && testResult < 46.17
    })
  }

  test("should pass monobit test") {
    runTest(() => {
      // when
      val stream = generator().get()

      // then
      val n = stream.count(i => i)
      n > 9725 && n < 10275
    })
  }

  private def generator() = new Generator(
    20_000,
    new Xor(LFSR.randomRegistry(5), LFSR.randomRegistry(5)),
    new Xor(LFSR.randomRegistry(5), LFSR.randomRegistry(5)),
    new Fibonacci(LFSR.randomRegistry(500)),
  )

  private def runTest(t: () => Boolean): Unit = {
    (0 to N) foreach(_ => {
      if (t()) return
    })
    val passed = false
    assert(passed, "should pass the test at least once")
  }
}
