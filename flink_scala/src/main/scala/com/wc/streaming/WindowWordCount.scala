package com.wc.streaming

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * flink流式处理实现WindowWordCount
  */
object WindowWordCount {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.socketTextStream("localhost", 9999)

    val counts = text.flatMap(x=>x.split(" "))
      .map((_,1))
      .keyBy(0)
      .timeWindow(Time.seconds(4),Time.seconds(2))
      .sum(1)

    counts.print()

    env.execute("Window Stream WordCount")

  }
}
