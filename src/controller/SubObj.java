package controller;

import People.Hung;
import People.Tung;
import model.TableInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static controller.CodeGenerator.uncapitalize;

public class SubObj {
    public static void genSubObj(TableInfo tableInfo, String folder,String fatherTableName) throws IOException {
        File dir = new File(folder);
        dir.mkdirs();
        File dir2 = new File(folder + "\\" + uncapitalize(tableInfo.tableName));
        dir2.mkdirs();
        Tung.gensubTableJSP(tableInfo, dir2.getAbsolutePath());
        Hung.genSubTableJs(tableInfo,fatherTableName,dir2.getAbsolutePath());
    }
}
