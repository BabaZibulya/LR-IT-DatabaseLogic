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
    public void readAndWriteTableTest() throws Exception {
        Table table = fillTable();
        table.saveToFile("/home/bondarenko/");
        Table table2 = Table.readFromFile("/home/bondarenko/test");
        assertEquals(sizeOfTable, table2.size());
        for (int i = 0; i < table2.size(); ++i) {
            Row row = table.getRow(i);
            assertEquals(charVal + "\t" + realVal + "\t" + charIntervalVal, row.toString());
        }
    }
}
