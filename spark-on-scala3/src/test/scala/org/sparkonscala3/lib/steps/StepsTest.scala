package org.sparkonscala3.lib.steps

import org.sparkonscala3.lib.{AbstractSparkSuite, Sessions, SparkSessionExt}

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Using

class StepsTest extends AbstractSparkSuite:
  test("returns the correct dataframe"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val sp         = new SparkSessionExt(spark)
      val steps      = Steps(spark, "StepsTest-correct-df")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      def calculations() = sp.toDF(Seq(1, 2, 3))

      for _ <- 1 to 3 do budgetStep.calculateOnce(calculations()).as[Int].collect().sorted should be(Array(1, 2, 3))

  test("returns the correct dataset"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val sp         = new SparkSessionExt(spark)
      val steps      = Steps(spark, "StepsTest-correct-ds")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      def calculations() = sp.toDS(Seq(1, 2, 3))

      for _ <- 1 to 3 do budgetStep.calculateOnce(calculations()).as[Int].collect().sorted should be(Array(1, 2, 3))

  test("doesn't re-evaluate cached dataframes"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val sp         = new SparkSessionExt(spark)
      val steps      = Steps(spark, "StepsTest-cachedDF")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      val calculated = new AtomicInteger(0)

      def calculations() =
        calculated.incrementAndGet()
        sp.toDF(Seq(1, 2, 3))

      budgetStep.calculateOnce(calculations())
      budgetStep.calculateOnce(calculations())
      calculated.get() should be(1)

  test("doesn't re-evaluate cached datasets"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val sp         = new SparkSessionExt(spark)
      val steps      = Steps(spark, "StepsTest-cachedDS")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      val calculated = new AtomicInteger(0)

      def calculations() =
        calculated.incrementAndGet()
        sp.toDS(Seq(1, 2, 3))
      budgetStep.calculateOnce(calculations())
      budgetStep.calculateOnce(calculations())
      calculated.get() should be(1)
