package org.sparkonscala3.lib.steps

import org.sparkonscala3.lib.{AbstractSparkSuite, Sessions, SparkSessionExt}

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Using

class StepsTest extends AbstractSparkSuite:
  test("doesn't re-evaluate cached datasets"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val sp         = new SparkSessionExt(spark)
      val steps      = Steps(spark, "StepsTest1")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      val calculated = new AtomicInteger(0)

      def calculations() =
        println("calculations()")
        calculated.incrementAndGet()
        sp.toDS(Seq(1, 2, 3))
      budgetStep.calculateOnce(calculations())
      budgetStep.calculateOnce(calculations())
      calculated.get() should be(1)
