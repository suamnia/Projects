package com;
import java.sql.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1",produces=MediaType.APPLICATION_JSON_VALUE)
public class ServletContainer {
    Connection conn;
    String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=12345678";

    @PostMapping("/driver/create")
    public String createDriver(@RequestBody String body) throws SQLException {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String name = StringUtils.substringBetween(body,"\"name\"=\"","\"");
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO public.drivers(name)" +" VALUES ('"+name+"');");
        return "Водитель "+name+" создан в БД";
    }

    @GetMapping("/driver/get")
    public String getDriver(@RequestParam("id") String id) throws SQLException {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String result="";
        Statement statement = conn.createStatement();
        ResultSet resultSet=statement.executeQuery("SELECT * FROM public.drivers WHERE id = '"+id+"';");
        while (resultSet.next()){
            result+=resultSet.getString("id");
            result+=" "+resultSet.getString("name");
        }
        return result;
    }

    @PostMapping("/purchase/create")
    public String createPokupka(@RequestParam("name") String name, @RequestParam("number") String number) throws SQLException {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO public.purchases(name, kolichestvo) VALUES ('"+name+"', '"+number+"');");
        return "Покупка создана";
    }

    @GetMapping("/purchase/get/list")
    public String getList() throws SQLException {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String result="";
        Statement statement = conn.createStatement();
        ResultSet resultSet=statement.executeQuery("SELECT * FROM public.purchases;");
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();
        String[] row = new String[columnCount];
        while (resultSet.next()){
            for (int i = 1; i <= columnCount; ++i) {
                row[i - 1] = resultSet.getString(i); // Or even rs.getObject()
            }
            for(String x: row)result+=x+"\t";
            result+=";\n";
        }
        return result;
    }
}
