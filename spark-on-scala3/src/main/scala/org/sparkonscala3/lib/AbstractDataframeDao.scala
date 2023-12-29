package org.sparkonscala3.lib

import org.apache.spark.sql.{DataFrame, SparkSession}

trait AbstractDataframeDao:
  def save(ds: DataFrame): Unit
  def load(): DataFrame

class ParquetDataframeDao(spark: SparkSession, path: String) extends AbstractDataframeDao:
  override def save(ds: DataFrame): Unit = ds.write.parquet(path)
  override def load(): DataFrame         = spark.read.parquet(path)

class OrcDataframeDao(spark: SparkSession, path: String) extends AbstractDataframeDao:
  override def save(ds: DataFrame): Unit = ds.write.orc(path)
  override def load(): DataFrame         = spark.read.orc(path)

class CsvDataframeDao(spark: SparkSession, path: String, header: Boolean = true) extends AbstractDataframeDao:
  override def save(ds: DataFrame): Unit = ds.write.option("header", header).parquet(path)
  override def load(): DataFrame         = spark.read.option("header", header).parquet(path)
