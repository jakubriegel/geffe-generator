package eu.jrie.put.pod.geffe.cipher

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Stream cipher based on Geffe generator
 *
 * @see Generator
 */
object Cipher {

  type ResultBuilder =  mutable.Builder[Boolean, LazyList[Boolean]]

  def code(stream: LazyList[Boolean], data: LazyList[Boolean]): LazyList[Boolean] = code(
    stream, data, LazyList.newBuilder
  ).result()

  @tailrec
  private def code(stream: LazyList[Boolean], data: LazyList[Boolean], resultBuilder: ResultBuilder): ResultBuilder =
    if (data.isEmpty) resultBuilder
    else code(stream.tail, data.tail, resultBuilder.addOne(stream.head ^ data.head))

//  first tailrec solution - left here because looks fancy
//  def encode(stream: LazyList[Boolean], data: LazyList[Byte]): List [Boolean]= encode(
//    stream,
//    data.flatMap(byte => 0 to 7 map byteToBit(byte)).map { b => if (b == 1) true else false },
//    List()
//  )
//
//  @tailrec
//  private def encode(stream: LazyList[Boolean], data: LazyList[Boolean], code: List[Boolean]): List[Boolean] =
//    if (data.isEmpty) code
//    else encode(stream.tail, data.tail, code ::: ((stream.head ^ data.head) :: Nil))
//
}

