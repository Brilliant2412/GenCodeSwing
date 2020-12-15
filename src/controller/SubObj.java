package controller;

import model.TableInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static controller.CodeGenerator.uncapitalize;

public class SubObj {
    private static void genNewDialog(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + uncapitalize(tableInfo.tableName) + "newDialog.jsp");
        fileWriter.append(
                "<legend class=\"fs-legend-head\">\n" +
                        "                <span class=\"iconFS\"></span>\n" +
                        "                <span class=\"titleFS\" style=\"color: #047fcd !important;\"><b>Bảng con</b></span>\n" +
                        "            </legend>\n" +
                        "            <div class=\"form-group-add row\">\n" +
                        "                <table id=\"tblMstDivision\" class=\"table table-striped table-bordered table-hover smart-form dataTable no-footer\">\n" +
                        "                    <thead>\n" +
                        "                        <tr>\n" +
                        "                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\">STT</th>\n");
        for(int i = 1; i < tableInfo.columns.size(); i++){
            fileWriter.append("                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\">" + tableInfo.columns.get(i).getColDescription() + "</th>\n");
        }
        fileWriter.append(
                "                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\"><a style=\"cursor: pointer; color:white !important;\" class=\"fa fa-plus fa-lg src\" onclick=\"onClickAddData();\"></a></th>\n" +
                        "                        </tr>\n" +
                        "                    </thead>\n" +
                        "                    <tbody id=\"dataDetailInfo\" >\n" +
                        "                    </tbody>\n" +
                        "                </table>\n" +
                        "            </div>\n");
        fileWriter.close();
    }

    public static void genSubObj(TableInfo tableInfo, String folder) throws IOException {
        File dir = new File(folder);
        dir.mkdirs();
        File dir2 = new File(folder + "\\" + uncapitalize(tableInfo.tableName));
        dir2.mkdirs();
        genNewDialog(tableInfo, dir2.getAbsolutePath());
    }
}