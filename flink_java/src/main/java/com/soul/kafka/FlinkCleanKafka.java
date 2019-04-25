package com.soul.kafka;

import com.soul.utils.DateUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;

import java.util.Properties;

/**
 * @author 若泽数据-soulChun
 * @create 2018-12-19-17:23
 */
public class FlinkCleanKafka {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");//kafka的节点的IP或者hostName，多个使用逗号分隔
        properties.setProperty("zookeeper.connect", "localhost:2181");//zookeeper的节点的IP或者hostName，多个使用逗号进行分隔
        properties.setProperty("group.id", "test-consumer-group");//flink consumer flink的消费者的group.id

        FlinkKafkaConsumer09<String> myConsumer = new FlinkKafkaConsumer09<String>("imooc_topic", new SimpleStringSchema(), properties);

//        myConsumer.assignTimestampsAndWatermarks(new CustomWaterMark());

        DataStream<String> stream = env.addSource(myConsumer);
//        stream.print().setParallelism(2);

        DataStream CleanData = stream.map(new MapFunction<String, Tuple5<String, String, String, String, String>>() {
            @Override
            public Tuple5<String, String, String, String, String> map(String value) throws Exception {
                String[] data = value.split("\\\t");
//                for(String str:data){
//                    System.out.println(str);
//                }
                String CourseID = null;
                String url = data[2].split("\\ ")[2];
                if (url.startsWith("/class")) {
                    String CourseHTML = url.split("\\/")[2];
                    CourseID = CourseHTML.substring(0, CourseHTML.lastIndexOf("."));
//                    System.out.println(CourseID);
                }

                return Tuple5.of(data[0], DateUtils.parseMinute(data[1]), CourseID, data[3], data[4]);
            }
        }).filter(new FilterFunction<Tuple5<String, String, String, String, String>>() {
            @Override
            public boolean filter(Tuple5<String, String, String, String, String> value) throws Exception {
                return value.f2 != null;
            }
        });


        CleanData.addSink(new MySQLSink());

        env.execute("Flink kafka");
    }
}
