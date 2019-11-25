package eu.jrie.put.pod.geffe.generator

import eu.jrie.put.pod.geffe.registry.{Fibonacci, Xor}
import eu.jrie.put.pod.geffe.test.FIPS
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class GeneratorTest extends FunSuite with BeforeAndAfterEach{

  private val N = 20_000
  private var stream: LazyList[Boolean] = _

  override def beforeEach() {
    stream = generator().get()
  }

  test("should pass series test") {
    // when
    val result = FIPS.seriesTest(stream)

    // then
    assert(result)
  }

  test("should pass long series test") {
    // when
    val result = FIPS.longSeriesTest(stream)

    // then
    assert(result)
  }

  test("should pass poker test") {
    // when
    val result = FIPS.pokerTest(stream)

    // then
    assert(result)
  }

  test("should pass monobit test") {
    // when
    val result = FIPS.monobitTest(stream)

    // then
    assert(result)
  }

  private def generator() = new Generator(
    N,
    new Fibonacci(
      List(true, false, true, true, true, false, true, true, true, true, true, false, true, true, true, false),
    ),
    new Xor(
      List(false, true, true, true, false, true, false, true, false, true, false, true, false, true, false, true),
      List(true, true, false, true, false, true, true, true, true, true, true, false, true, false, true, false),
    ),
    new Xor(
      List(true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false),
      List(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true),
    )
  )
}
