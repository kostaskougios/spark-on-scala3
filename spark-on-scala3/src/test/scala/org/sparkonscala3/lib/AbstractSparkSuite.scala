package org.sparkonscala3.lib

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class AbstractSparkSuite extends AnyFunSuiteLike with Matchers:
  protected def randomString: String      = UUID.randomUUID().toString
  protected def randomTmpFilename: String = "/tmp/AbstractSparkSuite-" + UUID.randomUUID().toString
