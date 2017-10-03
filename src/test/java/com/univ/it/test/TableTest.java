package com.univ.it.test;

import com.univ.it.table.Row;
import com.univ.it.table.Table;
import com.univ.it.types.AttributeChar;
import com.univ.it.types.AttributeCharInterval;
import com.univ.it.types.AttributeReal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest {


    private String charVal = "h", realVal = "1.65", charIntervalVal = "[a:n]";
    private int sizeOfTable = 10;

    private Table fillTable() {
        Table table = new Table("test");
        for (int i = 0; i < sizeOfTable; ++i) {
            Row newRow = new Row(3);
            newRow.pushBack(new AttributeChar(charVal));
            newRow.pushBack(new AttributeReal(realVal));
            newRow.pushBack(new AttributeCharInterval(charIntervalVal));
            try {
                table.addNewRow(newRow);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return table;
    }

    @Test
    public void createTableTest() {
        Table table = fillTable();
        assertEquals(sizeOfTable, table.size());
        for (int i = 0; i < table.size(); ++i) {
            Row row = table.getRow(i);
            assertEquals(charVal + "\t" + realVal + "\t" + charIntervalVal, row.toString());
        }
    }

    @Test
    public void writeTableTest() {
        Table table = fillTable();
        table.saveToFile("/home/bondarenko/");
    }

    @Test
    public void readTableTest() {
        try {
            Table table = Table.readFromFile("/home/bondarenko/test");
            assertEquals(sizeOfTable, table.size());
            for (int i = 0; i < table.size(); ++i) {
                Row row = table.getRow(i);
                assertEquals(charVal + "\t" + realVal + "\t" + charIntervalVal, row.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
