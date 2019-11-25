package eu.jrie.put.pod.geffe.cipher

import eu.jrie.put.pod.geffe.cipher.Cipher.code
import org.scalatest.FunSuite

class CipherTest extends FunSuite {
  test("should code given data with given stream") {
    // given
    val stream = LazyList.continually { true }
    val data = LazyList.fill(1) { true }

    // when
    val result = code(stream, data)

    // then
    assertResult(List(false, true, true, true, true, true, true, true))(result)
  }
}
