package eu.jrie.put.pod.geffe.registry

class Xor (registry: List[Boolean], coefficients: List[Boolean]) extends LFSR(registry) {
  assert(values.size == coefficients.size, "coefficients should be provided for all registry elements")

  override protected def calculateNext(): Boolean = {
    values.zip(coefficients)
      .map { case (x, a) => a && x }
      .foldLeft(false) {(r, x) => r ^ x}
  }
}
