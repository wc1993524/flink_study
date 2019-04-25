package com.study.streaming;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 基于Flink实现一个窗口（window）的workcount计算
 * 每隔2秒计算最近4秒的数据
 */

public class FlinkSocketApp {
    public static void main(String[] args) throws Exception{

        //环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //source
        DataStream<String> lines = env.socketTextStream("localhost",9999);

        //transformations
        DataStream<WC> result = lines.flatMap(new FlatMapFunction<String, WC>() {

            @Override
            public void flatMap(String s, Collector<WC> collector) throws Exception {
                String[] datas = s.split(" ");
                for(String data:datas){
                    collector.collect(new WC(data,1L));
                }
            }
        }).keyBy("word")
          .timeWindow(Time.seconds(4),Time.seconds(2))
          .sum("count");

        //sink
        result.print().setParallelism(1);

        //执行flink
        env.execute("FlinkSocketApp");

    }

    /**
     * recognized as a POJO
     * 1.public
     * 2.without arguments constructor
     * 3.getter/setter
     * 4.serialize
     */
    public static class WC{
        private String word;
        private long count;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public WC() {
        }

        public WC(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return "WC{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
