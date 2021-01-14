package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import model.ColumnProperty;
import model.TableInfo;
import model.TableSet;


public class CodeGenerator {
    static String url;
    static String pathString;
    static String pathOne;
    static String pathTwo;
    static String pathSubObjs;

    public CodeGenerator() {
        url="";
        pathString = "";
    }

    public CodeGenerator(String url,String path) {
        setUrl(url);
        setPathString(path);
    }

    public static String getPathString() {
        return pathString;
    }

    public static void setPathString(String pathString) {
        CodeGenerator.pathString = pathString;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        CodeGenerator.url = url;
    }

    public static String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String uncapitalize(String s){
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public void GEN(int sheet, Vector<Integer> numSubObjs, int excelType) throws Exception {
        TableSet tableSet = new TableSet(url, sheet, numSubObjs, excelType);
        pathOne = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"service";
        pathTwo = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"web";
        //pathSubObjs = pathString+"\\"+ tableSet.tableInfo.tableName+"\\"+"sub objects";
        Service.genService(tableSet ,pathOne);
        Web.genWeb(tableSet, pathTwo);
        for(int i = 0; i < tableSet.subTables.size(); i++){
            SubObj.genSubObj(tableSet.subTables.get(i), pathTwo,tableSet.tableInfo.tableName);
        }
    }
}