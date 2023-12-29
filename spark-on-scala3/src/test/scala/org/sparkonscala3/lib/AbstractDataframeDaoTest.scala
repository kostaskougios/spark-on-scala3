package org.sparkonscala3.lib

import org.apache.spark.sql.SparkSession
import org.sparkonscala3.lib.testmodel.Person

import scala.util.Using

class AbstractDataframeDaoTest extends AbstractSparkSuite:
  val people = (for (i <- 1 to 10) yield Person(i.toString, s"text for row $i")).sortBy(_.id)

  def testFor(daoF: (SparkSession, String) => AbstractDataframeDao): Unit =
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val path = randomTmpFilename
      val dao  = daoF(spark, path)
      dao.save(people.toDF())
      dao.load().as[Person].collect().sortBy(_.id) should be(people)

  test("parquet dao"):
    testFor((spark, path) => new ParquetDataframeDao(spark, path))

  test("orc dao"):
    testFor((spark, path) => new OrcDataframeDao(spark, path))

  test("csv dao"):
    testFor((spark, path) => new CsvDataframeDao(spark, path))
