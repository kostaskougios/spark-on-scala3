A utility library to help us use spark with scala 3 until spark is fully compatible with scala 3.

Based on work by Chris Birchall ,  https://xebia.com/blog/using-scala-3-with-spark/ and Vincenzo Baz, https://github.com/vincenzobaz/spark-scala3

Notes:

- To run on newer jdks like jdk21, please pass `--add-opens=java.base/sun.nio.ch=ALL-UNNAMED` to java.