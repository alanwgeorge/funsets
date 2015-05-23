package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val range1to10 = rangeInclusiveSet(1, 10)
    val range5to15 = rangeInclusiveSet(5, 15)
    val even: Set = _ % 2 == 0
    val odd: Set = !even(_)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("rangeInclusiveSet()") {
    new TestSets {
      assert(contains(range1to10, 5), "range1to10 conatians 5")
      assert(contains(range1to10, 1), "range1to10 conatians 1")
      assert(contains(range1to10, 10), "range1to10 conatians 10")
      assert(!contains(rangeInclusiveSet(10, 1), 5), "impossible range 5")
      assert(!contains(rangeInclusiveSet(10, 1), 10), "impossible range 10")
      assert(!contains(rangeInclusiveSet(10, 1), 1), "impossible range 1")
    }
  }

  test("even and odd") {
    new TestSets {
      assert(!contains(even, 1), "Union 4")
      assert(contains(even, 2), "Union 5")
      assert(contains(odd, 1), "Union 6")
      assert(!contains(odd, 2), "Union 7")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val union1 = union(s1, s2)
      assert(contains(union1, 1), "Union 1")
      assert(contains(union1, 2), "Union 2")
      assert(!contains(union1, 3), "Union 3")

      val union2 = union(even, odd)
      assert(contains(union2, 1), "Union 4")
      assert(contains(union2, 2), "Union 5")
    }
  }

  test("intersect contains elements in both sets") {
    new TestSets {
      val intersection1 = intersect(range1to10, range5to15)
      assert(contains(intersection1, 8), "Intersect 8")
      assert(contains(intersection1, 10), "Intersect 10")
      assert(contains(intersection1, 5), "Intersect 5")
      assert(!contains(intersection1, 1), "Intersect 1")
      assert(!contains(intersection1, 15), "Intersect 15")
      assert(!contains(intersection1, 4), "Intersect 4")
      assert(!contains(intersection1, 14), "Intersect 14")

      val intersection2 = intersect(even, odd)
      assert(!contains(intersection2, 1), "Intersect Odd")
      assert(!contains(intersection2, 2), "Intersect Even")

    }
  }

  test("diff contains all elements in first set that are not in second") {
    new TestSets {
      val difference1 = diff(range1to10, range5to15)
      assert(contains(difference1, 1), "diff 1")
      assert(contains(difference1, 4), "diff 4")
      assert(!contains(difference1, 5), "diff 5")
      assert(!contains(difference1, 15), "diff 15")

      val difference2 = diff(range1to10, even)
      assert(contains(difference2, 1), "diff even 1")
      assert(!contains(difference2, 20), "diff even 20")
      assert(!contains(difference2, 4), "diff even 4")
    }
  }

  test("filter contains all elements in first set that are in second") {
    new TestSets {
      val filterSet1 = filter(range1to10, range5to15)
      assert(!contains(filterSet1, 1), "filter 1")
      assert(!contains(filterSet1, 4), "filter 4")
      assert(contains(filterSet1, 5), "filter 5")
      assert(!contains(filterSet1, 15), "filter 15")

      val filterSet2 = filter(range1to10, even)
      assert(!contains(filterSet2, 1), "filter even 1")
      assert(!contains(filterSet2, 20), "filter even 20")
      assert(contains(filterSet2, 4), "filter even 4")

      val filterSet3 = filter(filter(odd, _ > 0), _ < 5)
      assert(contains(filterSet3, 3))
      assert(!contains(filterSet3, 5))
    }
  }

  test("forAll applies Int => Boolean to whole set") {
    new TestSets {
      assert(forall(range1to10, _ > 0), "forall 1")
      assert(!forall(range1to10, _ != 5), "forall 2")
      assert(!forall(range1to10, even), "forall 3")
      assert(!forall(range1to10, odd), "forall 4")
      assert(forall(intersect(range1to10, even), even), "forall 5")
    }
  }

  test("exist: tests a set for one element that satisfies the predicate") {
    new TestSets {
      assert(exists(range1to10, _ == 5), "exists 1")
      assert(exists(range1to10, _ < 5), "exists 2")
      assert(!exists(even, _ == 5), "exists 3")
      assert(!exists(even, odd), "exists 4")
      assert(exists(range1to10, even), "exists 5")
    }
  }

  test("map: works!") {
    new TestSets {
      assert(forall(map(even, _ - 1), odd), "map 1")
      assert(exists(map(range1to10, _ * 10), _ % 10 == 0), "map 1")
    }
  }
}
