package model;

import java.io.IOException;
import java.util.Vector;

public class TableSet {
    public TableInfo tableInfo;
    public Vector<TableInfo> subTables;

    public TableSet(String url, int sheet, Vector<Integer> numSubObjs, int excelType) throws Exception {
        subTables = new Vector<>();
        tableInfo = new TableInfo(url, sheet, 1);
        for(int i = 0; i < numSubObjs.size(); i++){
            subTables.add(new TableInfo(url, numSubObjs.get(i), excelType));
        }
    }
}
