package com.example.questApplication.Util;

import android.database.SQLException;
import android.util.Log;

import java.sql.DriverManager;


public class JDBCUtils {
    private static final String TAG = "mysql11111";
    java.sql.Connection conn = null;

    public static void mysql() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(100);  // 每隔0.1秒尝试连接
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                    }

// 1.加载JDBC驱动
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Log.v(TAG, "加载JDBC驱动成功");
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "加载JDBC驱动失败");
                        return;
                    }

                    // 2.设置好IP/端口/数据库名/用户名/密码等必要的连接信息
                    String ip = "140.210.204.183";
                    int port = 3306;
                    String dbName = "quest";
                    String url = "jdbc:mysql://" + ip + ":" + port
                            + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
                    // 构建连接mysql的字符串
                    String user = "root";
                    String password = "Lidong123!";

                    // 3.连接JDBC
                    try {
                        java.sql.Connection conn = DriverManager.getConnection(url, user, password);
                        Log.d(TAG, "数据库连接成功");
                        conn.close();
                        return;
                    } catch (SQLException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        thread.start();
    }

}