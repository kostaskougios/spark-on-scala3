package org.sparkonscala3.lib

import org.sparkonscala3.lib.testmodel.Person

import scala.util.Using

class SparkSessionExtTest extends AbstractSparkSuite:
  val people = for (i <- 1 to 10) yield Person(i.toString, s"text for row $i")

  test("schemaOf"):
    Using.resource(Sessions.newSparkSession()): spark =>
      val sp     = new SparkSessionExt(spark)
      val schema = sp.schemaOf[Person]
      println(schema)
      schema.toList.size should be(2)

  test("toDF"):
    Using.resource(Sessions.newSparkSession()): spark =>
      val sp = new SparkSessionExt(spark)
      val df = sp.toDF(people)
      import scala3encoders.given
      df.as[Person].collect() should be(people.toArray)

  test("toDS"):
    Using.resource(Sessions.newSparkSession()): spark =>
      val sp = new SparkSessionExt(spark)
      val ds = sp.toDS(people)
      import spark.implicits.*
      ds.collect() should be(people.toArray)
