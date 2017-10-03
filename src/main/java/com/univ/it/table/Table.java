package com.univ.it.table;

import com.univ.it.types.Attribute;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Table {
    private String name;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;
    private boolean firstRow = true;

    public Table(String name) {
        this.name = name;
        rows = new ArrayList<>();
        columns = new ArrayList<>();
    }

    public void addNewRow(Row newRow) throws Exception {
        if (firstRow) {
            columns = deduceTypes(newRow);
            firstRow = false;
        } else {
            if (!checkTypes(newRow)) {
                throw new Exception("Types are incompatible");
            }
        }
        rows.add(newRow);
    }

    private ArrayList<Column> deduceTypes(Row row) {
        ArrayList<Column> deducedTypes = new ArrayList<>();
        for (int i = 0; i < row.size(); ++i) {
            deducedTypes.add(new Column(row.getAt(i).getClass()));
        }
        return deducedTypes;
    }

    private boolean checkTypes(Row row) {
        Row firstRow = rows.get(0);
        if (firstRow.size() != row.size()) {
            return false;
        }
        for (int i = 0; i < row.size(); ++i) {
            if (!row.getAt(i).getClass().equals(firstRow.getAt(i).getClass())) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        return rows.size();
    }

    public Row getRow(int ind) {
        if (ind >= rows.size()) {
            throw new IndexOutOfBoundsException("Table has no such row");
        } else {
            return rows.get(ind);
        }
    }

    public void saveToFile(String pathToFile) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(pathToFile + File.separator + name);

        StringJoiner columnNames = new StringJoiner("\t");
        for (Column column : columns) {
            columnNames.add(column.toString());
        }
        out.println(columnNames);
        for (Row row : rows) {
            String rowString = row.toString();
            out.println(rowString);
        }
    }

    public static Table readFromFile(String file) throws Exception {
        Table result = new Table(Paths.get(file).getFileName().toString());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String sCurrentLine;
        boolean firstLine = true;
        int columnNumber = 0;

        while ((sCurrentLine = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                String[] columnNames = sCurrentLine.split("\t");
                columnNumber = columnNames.length;
                for (String columnName : columnNames) {
                    result.columns.add(new Column(columnName));
                }
            } else {
                String[] row = sCurrentLine.split("\t");
                if (columnNumber != row.length) {
                    throw new Exception("Invalid file");
                }
                result.rows.add(new Row());
                int i = 0;
                for (String stringAttribute : row) {
                    Constructor attributeConstructor = result.columns.get(i).getAttributeConstructor();
                    Attribute attribute = (Attribute) attributeConstructor.newInstance(stringAttribute);
                    result.rows.get(result.rows.size() - 1).replaceAt(i, attribute);
                    ++i;
                }
            }
        }
        return result;
    }
}
