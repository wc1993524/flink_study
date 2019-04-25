package com.wc.batch

import com.wc.WordCountData
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.scala._

/**
  * flink批处理实现wordcount
  */
object BatchWordCount {
  def main(args: Array[String]): Unit = {
    val params: ParameterTool = ParameterTool.fromArgs(args)

    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    val text =
      if (params.has("input")) {
        env.readTextFile(params.get("input"))
      } else {
        println("Executing WordCount example with default input data set.")
        println("Use --input to specify file input.")
        env.fromCollection(WordCountData.WORDS)
      }

    val counts = text.flatMap(x=>x.split(" "))
      .map((_,1))
      .groupBy(0)
      .sum(1)
      .filter(_._2!=1)

    if (params.has("output")) {
      counts.writeAsCsv(params.get("output"), "\n", " ",FileSystem.WriteMode.OVERWRITE)
      env.execute("Scala WordCount Example")
    } else {
      println("Printing result to stdout. Use --output to specify output path.")
      counts.print()
    }

  }
}
