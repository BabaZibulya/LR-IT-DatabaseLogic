package com.univ.it.table;

import java.util.HashMap;
import java.io.*;
import java.nio.file.Paths;
import java.util.StringJoiner;

public class DataBase {
    private String name;
    private HashMap<String, Table> tables;

    public  DataBase(String name) {
        this.name = name;
        this.tables = new HashMap<>();
    }

    public boolean addTable(Table table) {
        if (tables.containsKey(table)) {
            return false;
        } else {
            tables.put(table.getName(), table);
            return true;
        }
    }

    public boolean dropTable(String tableName) {
        return (null != tables.remove(tableName));
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public void writeToFile(String pathToFile) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(pathToFile + File.separator + name + ".db");

        for (String tableName : tables.keySet()) {
            out.println(tableName);
        }
        out.close();
        for (Table table : tables.values()) {
            table.saveToFile(pathToFile);
        }
    }

    public static DataBase readFromFile(String dbFile) throws Exception {
        String dbName = Paths.get(dbFile).getFileName().toString();
        String dpPath = Paths.get(dbFile).getParent().toString();
        FileReader fr = new FileReader(dbFile);
        BufferedReader br = new BufferedReader(fr);

        String sCurrentLine;

        DataBase db = new DataBase(dbName);

        while ((sCurrentLine = br.readLine()) != null) {
            db.addTable(Table.readFromFile(dpPath + File.separator + sCurrentLine));
        }

        br.close();
        fr.close();

        return db;
    }
}
