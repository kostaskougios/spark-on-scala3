package org.sparkonscala3.lib

import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}

import scala.deriving.Mirror.ProductOf
import scala.reflect.ClassTag

class SparkSessionExt(spark: SparkSession):
  import scala3encoders.given
  import spark.implicits.*

  def schemaOf[P: ClassTag: ProductOf] = summon[Encoder[P]].schema

  def toDF[P: ClassTag: ProductOf](s: Seq[P], numSlices: Int = spark.sparkContext.defaultParallelism): DataFrame =
    spark.sparkContext.parallelize(s, numSlices).toDF()

  def toDS[P: ClassTag: ProductOf](s: Seq[P], numSlices: Int = spark.sparkContext.defaultParallelism): Dataset[P] =
    spark.sparkContext.parallelize(s, numSlices).toDS()
