package eu.jrie.put.pod.geffe.registry

class Fibonacci (registry: List[Boolean]) extends LFSR(registry) {

  override protected def calculateNext(): Boolean = {
    val (listA, listB) = values.splitAt(values.size / 2)
    val (a, b): (Long, Long) = (listA, listB)
    if((a + b) % 2 == 1) true else false
  }

  private implicit def fold(list: List[Boolean]): Long = list.foldLeft(0)((a: Int, b: Boolean) => {
    (a << 1) ^ (if(b) 1 else 0)
  })
}
