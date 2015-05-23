import funsets.FunSets._
object worksheet {
  val even:Set = _ % 2 == 0
  printSet(even)
  printSet(!even(_))
  val s1to10:Set = rangeInclusiveSet(1, 10)
  val five: Set = _ == 5
  printSet(five)
  printSet(!five(_))
  !forall(s1to10, !five(_))
  exists(s1to10, five)
  forall(s1to10, !five(_))
  forall(s1to10, five)
  printSet(_ % 10 == 0)
  printSet(map(s1to10, _ * 10))
  printSet(filter(!even(_), _ > 0))
  printSet(x => x % 2 != 0 && x > 0)
}
