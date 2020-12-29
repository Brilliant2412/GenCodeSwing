package controller;

import People.Hung;
import People.Tung;
import model.TableInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static controller.CodeGenerator.uncapitalize;

public class SubObj {
    public static void genSubObj(TableInfo tableInfo, String folder,String parentTableName) throws IOException {
        File dir = new File(folder);
        dir.mkdirs();
        File dir2 = new File(folder + "\\" + uncapitalize(parentTableName));
        dir2.mkdirs();
        Tung.gensubTableJSP(tableInfo, parentTableName, dir2.getAbsolutePath());
        Hung.genSubTableJs(tableInfo,parentTableName,dir.getAbsolutePath());
    }
}
