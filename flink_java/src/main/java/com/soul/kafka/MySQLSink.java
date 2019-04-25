package com.soul.kafka;

import com.soul.conf.sConf;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author 若泽数据-soulChun
 * @create 2018-12-20-15:09
 */
public class MySQLSink extends RichSinkFunction<Tuple5<String, String, String, String, String>> {
    private static final long serialVersionUID = 1L;
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void invoke(Tuple5<String, String, String, String, String> value) {

        try {
            if (connection == null) {
                Class.forName(sConf.DRIVERNAME);
                connection = DriverManager.getConnection(sConf.URL, sConf.USERNAME, sConf.PASSWORD);
            }
            String sql = "insert into log_info (ip,time,courseid,status_code,referer) values (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value.f0);
            preparedStatement.setString(2, value.f1);
            preparedStatement.setString(3, value.f2);
            preparedStatement.setString(4, value.f3);
            preparedStatement.setString(5, value.f4);
            System.out.println("Start insert");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open(Configuration parms) throws Exception {
        Class.forName(sConf.DRIVERNAME);
        connection = DriverManager.getConnection(sConf.URL, sConf.USERNAME, sConf.PASSWORD);
    }

    public void close() throws Exception {

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }

    }


}
