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

  fips("should pass series test") {
    FIPS.seriesTest(stream)
  }

  fips("should pass long series test") {
    FIPS.longSeriesTest(stream)
  }

  fips("should pass poker test") {
    FIPS.pokerTest(stream)
  }

  fips("should pass monobit test") {
    FIPS.monobitTest(stream)
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

  def fips(name : String)(testAction: => Boolean): Unit = {
    test(name) {
      // when
      val result = testAction
      // then
      assert(result, name)
    }
  }
}
