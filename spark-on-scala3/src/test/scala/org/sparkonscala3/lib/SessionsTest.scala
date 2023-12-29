package org.sparkonscala3.lib

import org.scalatest.funsuite.AnyFunSuiteLike
import org.sparkonscala3.lib.testmodel.Person

import scala.util.Using

class SessionsTest extends AbstractSparkSuite:
  val people = for (i <- 1 to 10) yield Person(i, s"text for row $i")

  test("creates/destroys session"):
    Using.resource(Sessions.newSparkSession()): spark =>
      ()

  test("Can convert to Dataset"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits._
      import scala3encoders.given
      val ds = spark.sparkContext.parallelize(people, 16).toDS()
      ds.collect() should be(people.toArray)

  test("Can write parquet"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits._
      import scala3encoders.given
      val ds  = spark.sparkContext.parallelize(people, 16).toDS()
      val f   = randomTmpFilename
      ds.write.parquet(f)
      val rds = spark.read.parquet(f).as[Person]
      rds.collect() should be(rds.collect())
