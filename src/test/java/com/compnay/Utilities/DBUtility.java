package com.compnay.Utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtility {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void establishConnection(DBType dbtype) throws SQLException {
        switch (dbtype){
            case MYSQL:
                connection = DriverManager.getConnection(ConfigurationReader.get("mySQL.url"),
                        ConfigurationReader.get("mySQL.user"), ConfigurationReader.get("mySQL.pass"));
                break;
            default:
                connection=null;
        }
    }

    public static int getRowsCount(String sql) throws SQLException {
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultSet = statement.executeQuery(sql);
        resultSet.last();
        return resultSet.getRow();

    }

    public static List<Map<String, Object>> runSQLQuery(String sql) throws SQLException {
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultSet = statement.executeQuery(sql);

        List<Map<String, Object>> queryData = new ArrayList<>();
        ResultSetMetaData rsData = resultSet.getMetaData();
        int colCount = rsData.getColumnCount();

        while (resultSet.next()){
            Map<String, Object> rowMap = new HashMap<>();

            for(int col = 1; col<=colCount; col++){
                rowMap.put(rsData.getColumnName(col), resultSet.getObject(col));
            }
            queryData.add(rowMap);
        }
        return queryData;
    }

    public static void closeConnections(){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
