package com.cp.tools.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA. User: jblum Date: Dec 11, 2010 Time: 6:00:35 PM To change this template use File | Settings
 * | File Templates.
 */
public class TestConnection {

  public static void main(final String... args) throws Exception {
    final Properties props = new Properties();
    props.put("user", "stradmin");
    props.put("password", "stradmin");
    props.put("v$session.program", "TestConnection");

    Class.forName("oracle.jdbc.driver.OracleDriver");

    final Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.21:1521:source", props);

    final PreparedStatement statement = connection.prepareStatement("SELECT sysdate FROM dual;");

    final ResultSet rs = statement.executeQuery();

    rs.next();

    System.out.println(rs.getTimestamp(1));

    rs.close();
    statement.close();
    connection.close();
  }

}
