/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import controller.CodeGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.TableInfo;
import static controller.CodeGenerator.*;
import model.ColumnProperty;
import model.TableSet;

/**
 *
 * @author hungy
 */


public class Hung {
    public static String resSearch(String tenTruong, String moTa, String kieuDL, String kieuNhap) {
        String res = "";
        if (kieuDL.equals("Long") || kieuDL.equals("Double")) {
            if (kieuNhap.equals("Combobox")) {
                res = "\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\">" + moTa + "</label>\n" +
                        "\t\t\t\t\t\t\t\t\t<div class=\"col-lg-2 selectContainer\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div id=\"cb" + tenTruong + "Search\"></div>\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n";
            } else {
                res = "\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\">" + moTa + "</label>\n" +
                        "\t\t\t\t\t\t\t\t\t<div class=\"col-lg-1 selectContainer\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<input type=\"number\" class=\"form-control\" placeholder=\"Từ\" id=\"" + tenTruong + "SearchFrom\">\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t<div class=\"col-lg-1\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<input type=\"number\" class=\"form-control\" placeholder=\"Đến\" id=\"" + tenTruong + "SearchTo\">\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n";
            }
        } else if (kieuDL.equals("Date")) {
            res = "\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\">" + moTa + "</label>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"col-lg-1 selectContainer\">\n" +
                    "\t\t\t\t\t\t\t\t\t\t<input type=\"text\" class=\"dateCalendar\" placeholder=\"Từ\" id=\"" + tenTruong + "SearchFrom\">\n" +
                    "\t\t\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"col-lg-1\">\n" +
                    "\t\t\t\t\t\t\t\t\t\t<input type=\"text\" class=\"dateCalendar\" placeholder=\"Đến\" id=\"" + tenTruong + "SearchTo\"> \n" +
                    "\t\t\t\t\t\t\t\t\t</div>\n";
        }
        return res;

    }

    public static ArrayList<ColumnProperty> columns_search(TableInfo tableInfo){
        ArrayList<ColumnProperty> res = new ArrayList<>();
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).isSearch() == true){
                if (tableInfo.columns.get(i).getColType().equals("String")){
                    continue;
                }else{
                    res.add(tableInfo.columns.get(i));
                }

            }
        }
        return res;
    }

    public static ArrayList<ColumnProperty> columns_isshow_notfile(TableInfo tableInfo){
        ArrayList<ColumnProperty> res = new ArrayList<>();
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).isShow() == true){
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    continue;
                }else{
                    res.add(tableInfo.columns.get(i));
                }

            }
        }
        return res;
    }

    public static void genformSearch(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder+"\\formSearch.jsp");
        fileWriter.write("<%@page contentType=\"text/html\" pageEncoding=\"UTF-8\"%>\n" +
                "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>\n" +
                "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\" %>\n" +
                "\n" +
                "<section id=\"widget-grid\" class=\"\" style=\"\n" +
                "         border-left-width: 0px;\n" +
                "         border-top-width: 0px;\n" +
                "         border-bottom-width: 0px;\n" +
                "         border-right-width: 0px;\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-lg-12\">\n" +
                "            <div class=\"jarviswidget\" id=\"wid-id-0\" data-widget-colorbutton=\"false\" data-widget-editbutton=\"false\" data-widget-deletebutton=\"false\" data-widget-sortable=\"false\" role=\"widget\">\n" +
                "                <!--<button type=\"button\" class=\"btn btn-sm btn-success\" id=\"show_hide_go\">Tìm kiếm - In danh sách</button><br>-->\n" +
                "                <div class=\"formsearchBody\" style=\"margin-left:4px !important;\">\n" +
                "                    <form id=\"productForm\" class=\"form-horizontal bv-form\" novalidate=\"novalidate\" style=\"width: 100%;\">\n" +
                "                        <button type=\"submit\" class=\"bv-hidden-submit\" style=\"display: none; width: 0px; height: 0px;\"></button>\n" +
                "                        <fieldset>\n");

        ArrayList<ColumnProperty> res_search = columns_search(tableInfo);
        int k = res_search.size()-1;
        int r = k / 4;
        int q = k%4;
        int i = 0;
        int countSearch = 0;
        if (r == 0) {
            fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
            fileWriter.append("\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\" style=\"padding:5px 4px\">Từ khóa</label>\n" +
                    "                                \t<div class=\"col-lg-2 selectContainer\">\n" +
                    "                                    \t<input type=\"text\" class=\"form-control\" placeholder=\"Nhập từ khóa\" id=\"keySearch\">\n" +
                    "                                \t</div>\n");
            for (; i < res_search.size(); i++) {
                String res = resSearch(res_search.get(i).getColName(),
                        res_search.get(i).getColDescription(),
                        res_search.get(i).getColType(),
                        res_search.get(i).getInputType()
                );
                if (res.equals("")){
                    continue;
                }else{
                    fileWriter.append(res);
                }

            }
            fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
        }
        else {
            fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
            fileWriter.append("\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\" style=\"padding:5px 4px\">Từ khóa</label>\n" +
                    "                                \t<div class=\"col-lg-2 selectContainer\">\n" +
                    "                                    \t<input type=\"text\" class=\"form-control\" placeholder=\"Nhập từ khóa\" id=\"keySearch\">\n" +
                    "                                \t</div>\n");
            for (; i < res_search.size(); i++) {
                String res = resSearch(res_search.get(i).getColName(),
                        res_search.get(i).getColDescription(),
                        res_search.get(i).getColType(),
                        res_search.get(i).getInputType()
                );
                fileWriter.append(res);
                if (i == 3) {
                    fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
                    break;
                }
            }
            k -= 3;
            r = k / 4;
            q = k % 4;
            //i++;
            if (r == 0) {
                fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                for (; i < res_search.size(); i++) {
                    String res = resSearch(res_search.get(i).getColName(),
                            res_search.get(i).getColDescription(),
                            res_search.get(i).getColType(),
                            res_search.get(i).getInputType()
                    );
                    fileWriter.append(res);
                }
                fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
            } else {
                for (int dem = 0; dem < r; dem++) {
                    fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                    for (; i < res_search.size(); i++) {
                        String res = resSearch(res_search.get(i).getColName(),
                                res_search.get(i).getColDescription(),
                                res_search.get(i).getColType(),
                                res_search.get(i).getInputType()
                        );
                        fileWriter.append(res);
                        if (i % 4 == 3 && i <= k) {
                            fileWriter.append("\t\t\t\t\t\t\t\t/div>\n");
                        } else {
                            continue;
                        }
                    }
                }
                if (q != 0) {
                    fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                    for (; i < res_search.size(); i++) {
                        String res = resSearch(res_search.get(i).getColName(),
                                res_search.get(i).getColDescription(),
                                res_search.get(i).getColType(),
                                res_search.get(i).getInputType()
                        );
                        fileWriter.append(res);
                    }
                    fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
                }
            }
        }
        fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
                "                                <div class=\"col-lg-2 selectContainer\">\n" +
                "                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
                "                                </div>\n");

        fileWriter.append("                        </fieldset>\n" +
                "                        \n" +
                "                    </form>\n" +
                "                </div>\n" +
                "                <br>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "   </div>\n"+
                "</section>\n" +
                "\n" +
                "<style>\n" +
                "    .ui-select-match-text{\n" +
                "        width: 100%;\n" +
                "        overflow: hidden;\n" +
                "        text-overflow: ellipsis;\n" +
                "        padding-right: 40px;\n" +
                "    }\n" +
                "    .ui-select-toggle > .btn.btn-link {\n" +
                "        margin-right: 10px;\n" +
                "        top: 6px;\n" +
                "        position: absolute;\n" +
                "        right: 10px;\n" +
                "    }\n" +
                "    .multiselect-container .input-group{\n" +
                "        margin: 0px !important;\n" +
                "    }\n" +
                "    .checkbox-status .checkbox-inline:nth-child(2n+1){\n" +
                "        margin-left: 0px !important;\n" +
                "    }\n" +
                "    .formsearchBody{\n" +
                "        display: block\n" +
                "    }\n" +
                "</style>");
        fileWriter.close();
    }

    public static void genJs(TableSet tableSet, String folder) throws  IOException{
        TableInfo tableInfo = tableSet.tableInfo;
        FileWriter fileWriter = new FileWriter(folder+"\\" + uncapitalize(tableInfo.tableName) + ".js");
        fileWriter.write("//$(\"#TBL_DOCUMENT_TYPE\").addClass(\"active\");\n" +
                "//$(\"#naviParent\").replaceWith($(\"#ROOT_LAND_POINT  span\").html());\n" +
                "//$(\"#naviChild\").replaceWith($(\"#cbma  span\").html());\n" +
                "\n\n");
        /*********************************************************************************************
         *                                 var editCellRendererVT
         *********************************************************************************************/
        fileWriter.append(
                "var editCellRendererVT = function (gid) {\n" +
                        "    return '<div style=\"text-align: center\">'\n" +
                        "            + '    <a class=\"tooltipCus iconEdit\" href=\"javascript:objTblDocumentType.editTblDocumentType(\\'' + gid + '\\')\">'\n" +
                        "            + '        <span class=\"tooltipCustext\">' + $(\"#tooltipEdit\").val() + '</span><img src=\"share/core/images/edit.png\" class=\"grid-icon\"/>'\n" +
                        "            + '    </a><a class=\"tooltipCus iconDelete\" href=\"javascript:objTblDocumentType.deleteTblDocumentType(\\'' + gid + '\\')\">'\n" +
                        "            + '        <span class=\"tooltipCustext\">' + $(\"#tooltipDelete\").val() + '</span><img src=\"share/core/images/delete_1.png\" class=\"grid-icon\"/></a>'\n" +
                        "            + '</div>';" +
                        "};\n\n");

        /*********************************************************************************************
         *                                 var datafields
         *********************************************************************************************/
        fileWriter.append("var datafields = [\n");
        for (int i = 0; i <tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if (columnProperty.getColType().equals("String")) {
                fileWriter.append("    {name: '" + columnProperty.getColName() + "', type: 'String'},\n");
            }
            else if (columnProperty.getColType().equals("Date")) {
                fileWriter.append("    {name: '" + columnProperty.getColName() + "', type: 'String'},\n");
                fileWriter.append("    {name: '" + columnProperty.getColName() + "ST', type: 'String'},\n");
            }
            else fileWriter.append("    {name: '" + columnProperty.getColName() + "', type: 'Number'},\n");
        }
        fileWriter.append("];\n\n");

        /*********************************************************************************************
         *                                 var columns
         *********************************************************************************************/
        fileWriter.append("var columns = [\n" +
                "\t{text: \"STT\", sortable: false, datafield: '', styleClass: 'stt', clstitle: 'tlb_class_center', res: \"data-hide='phone'\"},\n" +
                "\t{text: 'gid', datafield: 'gid', hidden: true},\n");
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);

            if (columnProperty.isShow())
            {
                fileWriter.append("\t{text: \""+columnProperty.getColDescription()+"\", datafield: '"+columnProperty.getColName()+"', res: \"data-class='phone'\"},\n");

            }
        }
        fileWriter.append("\t{text: \"Chức năng\", datafield: 'gid', edit: 1, sortable: false, clstitle: 'tlb_class_center'}\n");
        fileWriter.append("];\n\n");

        fileWriter.append(
                "downloadFileDocument = function (id) {\n" +
                        "    location.href = \"/sys-web/download" + tableInfo.tableName + "file.html?id=\" + id;\n" +
                        "};\n" +
                        "\n"
        );

        /*********************************************************************************************
         *                                do search funtion 1
         *********************************************************************************************/
        fileWriter.append("doSearch = function () {\n" +
"    \n" +
"    var keySearch = $('#keySearch').val();\n" +
"    \n");
//        int count_cb = 0;
//        int count_db = 0;
//        int count_long = 0;
//        int count_date = 0;
//        for (int i = 0; i < tableInfo.columns.size(); i++) {
//            ColumnProperty colProp = tableInfo.columns.get(i);
//            if (colProp.isSearch()) {                
//                if (colProp.getColType().equals("Long")) {
//                    if (colProp.getInputType().equals("Combobox")) {
//                        count_cb++;
//                    } else {
//                        count_long++;
//                    }
//                }
//                if (colProp.getColType().equals("Double")) {
//                    count_db++;
//                }
//                if (colProp.getColType().equals("Date")) {
//                    count_date++;
//                }
//            }
//        }
        
        //Combobox
        int t_cb = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).getColType().equals("Long") && tableInfo.columns.get(i).isSearch()){
                t_cb++;
                fileWriter.append("    var listLong"+(t_cb)+" = $('#cb"+tableInfo.columns.get(i).getColName()+"SearchCombobox').val();\n");
            }
        }
        //date
        int t_date = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date") && tableInfo.columns.get(i).isSearch()){
                t_date++;
                fileWriter.append("    var string"+(t_date)+" = $('#"+tableInfo.columns.get(i).getColName()+"SearchFrom').val();\n");
                t_date++;
                fileWriter.append("    var string"+(t_date)+" = $('#"+tableInfo.columns.get(i).getColName()+"SearchTo').val();\n");
            }
        }
        
        //long not cb
        int t_long = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") && !tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).isSearch()){
                t_long++;
                fileWriter.append("    var long"+(t_long)+" = $('#"+tableInfo.columns.get(i).getColName()+"SearchCombobox').val();\n");
            }
        }
        
        //double
        int t_db = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Double") && tableInfo.columns.get(i).isSearch()){
                t_db++;
                fileWriter.append("    var double"+(t_db)+" = $('#"+tableInfo.columns.get(i).getColName()+"SearchCombobox').val();\n");
            }
        }
        
        
        fileWriter.append(
"    var url = \"getall"+tableInfo.tableName.toLowerCase()+".json\";\n" +
"    url += \"?key=\" + keySearch;\n");
        //Combobox
        int t_cb1 = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).getColType().equals("Long") && tableInfo.columns.get(i).isSearch()){
                t_cb1++;
                fileWriter.append("    url += \"&listLong"+t_cb1+"=\"+listLong"+t_cb1+";\n");
            }
        }
        //date
        int t_date1 = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date") && tableInfo.columns.get(i).isSearch()){
                t_date1++;
                fileWriter.append("    url += \"&string"+t_date1+"=\"+string"+t_date1+";\n");
                t_date1++;
                fileWriter.append("    url += \"&string"+t_date1+"=\"+string"+t_date1+";\n");
            }
        }
        
        //long not cb
        int t_long1 = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") && !tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).isSearch()){
                t_long1++;
                fileWriter.append("    url += \"&long"+t_long1+"=\"+long"+t_long1+";\n");
            }
        }
        
        //double
        int t_db1 = 0;
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Double") && tableInfo.columns.get(i).isSearch()){
                t_db1++;
                fileWriter.append("    url += \"&double"+t_db1+"=\"+double"+t_db1+";\n");
            }
        }
        
        
                fileWriter.append("\n"+
"    vt_datagrid.loadPageAgainRes(\"#dataTblDocumentType\", url);\n" +
"    vt_sys.showBody();\n" +
"    vt_loading.hideIconLoading();\n" +
"    return false;\n" +
"};");
        
        
        
        /*********************************************************************************************
         *                                 var gridSetting
         *********************************************************************************************/
        fileWriter.append("var gridSetting = {\n" +
                "    sortable: false,\n" +
                "    virtualmode: true,\n" +
                "    isSetting: false,\n" +
                "    enableSearch: false,\n" +
                "    onClickRow: true\n" +
                "};\n\n");
        /*********************************************************************************************
         *                                 do search
         *********************************************************************************************/
//        fileWriter.append(
//                "doSearch = function () {\n" +
//                        "    vt_datagrid.loadPageAgainRes(\"#dataTblDocumentType\", \"getall" + tableInfo.tableName.toLowerCase() + ".json\");\n" +
//                        "    vt_sys.showBody();\n" +
//                        "    vt_loading.hideIconLoading();\n" +
//                        "    return false;\n" +
//                        "};\n\n"
//        );
        
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getColType().equals("Date")){
                fileWriter.append(
                        "$(\"#" + colProp.getColName() + "\").datepicker({\n" +
                                "\tduration: \"fast\",\n" +
                                "\tchangeMonth: true,\n" +
                                "\tchangeYear: true,\n" +
                                "\tdateFormat: 'dd/mm/yy',\n" +
                                "\tconstrainInput: true,\n" +
                                "\tdisabled: false,\n" +
                                "\tyearRange: \"-20:+10\",\n" +
                                "\tonSelect: function (selected) {\n" +
                                "\t}\n" +
                                "});\n\n"
                );
            }
        }
        
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getColType().equals("Date")){
                fileWriter.append(
                        "$(\"#" + colProp.getColName() + "SearchFrom\").datepicker({\n" +
                                "\tduration: \"fast\",\n" +
                                "\tchangeMonth: true,\n" +
                                "\tchangeYear: true,\n" +
                                "\tdateFormat: 'dd/mm/yy',\n" +
                                "\tconstrainInput: true,\n" +
                                "\tdisabled: false,\n" +
                                "\tyearRange: \"-20:+10\",\n" +
                                "\tonSelect: function (selected) {\n" +
                                "\t}\n" +
                                "});\n\n"
                );
                fileWriter.append(
                        "$(\"#" + colProp.getColName() + "SearchTo\").datepicker({\n" +
                                "\tduration: \"fast\",\n" +
                                "\tchangeMonth: true,\n" +
                                "\tchangeYear: true,\n" +
                                "\tdateFormat: 'dd/mm/yy',\n" +
                                "\tconstrainInput: true,\n" +
                                "\tdisabled: false,\n" +
                                "\tyearRange: \"-20:+10\",\n" +
                                "\tonSelect: function (selected) {\n" +
                                "\t}\n" +
                                "});\n\n"
                );
            }
        }
        fileWriter.append(
                "$(function () {\n" +
                        "\tdoSearch();\n" +
                        "\t\n" );
        for(int i = 1; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getInputType().equals("Combobox")){
                fileWriter.append("\t\tvt_combobox.buildMultipleCombobox(\"cb" + colProp.getColName() + "Search\", \"" + colProp.getComboboxBuildPath() + "\", 0, \"" + colProp.getComboboxName() + "\", \"" + colProp.getComboboxValue() + "\", \"- Chọn " + colProp.getColDescription() + " -\", 0);\n");
            }
        }
        
        
        fileWriter.append("\t$(\"#btnSearch\").click(function () {\n" +
"        doSearch();\n" +
"    });\n");

        /**********************************************************************************************
         *                            edit
         **********************************************************************************************/
//        for(int i = 1; i < tableInfo.columns.size(); i++){
//            ColumnProperty colProp = tableInfo.columns.get(i);
//            if(colProp.getInputType().equals("Combobox") && colProp.isSearch() ){
//                fileWriter.append("\t\tvt_combobox.buildCombobox(\"cb" + colProp.getColName() + "Search\", \"" + colProp.getComboboxBuildPath() + "\", 0, \"" + colProp.getComboboxName() + "\", \"" + colProp.getComboboxValue() + "\", \"- Chọn " + colProp.getColDescription() + " -\", 0);\n");
//            }
//        }

        fileWriter.append("\tonClickBtAdd = function () {\n" +
                "        vt_form.reset($('#" + uncapitalize(tableInfo.tableName) + "Form'));\n");
        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append("        $(\"#"+uncapitalize(subTableInfo.tableName)+"_dataDetailInfo\").html(\"\");\n");
        };
        fileWriter.append("        $(\"#gid\").val(\"\"); // reset form\n" +
                "        vt_form.clearError();\n" +
                "        $(\"#isedit\").val(\"0\");\n" +
                "        \n"
        );
        for(int i = 1; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getInputType().equals("Combobox")){
                fileWriter.append("\t\tvt_combobox.buildCombobox(\"cb" + colProp.getColName() + "\", \"" + colProp.getComboboxBuildPath() + "\", 0, \"" + colProp.getComboboxName() + "\", \"" + colProp.getComboboxValue() + "\", \"- Chọn " + colProp.getColDescription() + " -\", 0);\n");
            }
        }
        fileWriter.append("\n" +
                "        $('#dialog-formAddNew').dialog({\n" +
                "            title: \"Thêm mới " + tableInfo.description + "\"\n" +
                "        }).dialog('open');\n" +
                "        return false;\n" +
                "    };\n" +
                "\t\n"
        );

        /**********************************************************************************************
         *                            setValueToForm
         **********************************************************************************************/

        fileWriter.append(
                "\tsetValueToForm = function () {\n" +
                        "        var item;\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getInputType().equals("Combobox")){
                fileWriter.append(
                        "\t\titem = $('#cb" + colProp.getColName() + "Combobox').val();\n" +
                                "\t\t$('input[name=\""  + colProp.getColName() + "\"]').val(item);\n"
                );
            }
        }
        fileWriter.append("\t}");

        /**********************************************************************************************
         *                            editTblDocumentTypeMethod
         **********************************************************************************************/

        fileWriter.append("\n" +
                "\teditTblDocumentTypeMethod = function () {\n" +
                "        clearError();\n" +
                "        setValueToForm();\n" +
                "        $.ajax({\n" +
                "            traditional: true,\n" +
                "            url: \"token.json\",\n" +
                "            dataType: \"text\",\n" +
                "            type: \"GET\"\n" +
                "        }).success(function (result) {\n" +
                "            if (vt_form.validate1(\"#" + uncapitalize(tableInfo.tableName) + "Form\", null, objTblDocumentType.validateRule))\n" +
                "            {\n" +
                "                var formdataTmp = new FormData();\n" +
                "                var formData = new FormData(document.getElementById(\"" + uncapitalize(tableInfo.tableName) + "Form\"));\n" +
                "                for (var pair of formData.entries()) {\n" +
                "                    formdataTmp.append(pair[0], pair[1]);\n" +
                "                }\n" +
                "                $.ajax({\n" +
                "                    async: false,\n" +
                "                    url: \"update" + tableInfo.tableName.toLowerCase() + ".html\",\n" +
                "                    data: formdataTmp,\n" +
                "                    processData: false,\n" +
                "                    contentType: false,\n" +
                "                    enctype: 'multipart/form-data',\n" +
                "                    type: \"POST\",\n" +
                "                    headers: {\"X-XSRF-TOKEN\": result},\n" +
                "                    dataType: 'json',\n" +
                "                    beforeSend: function (xhr) {\n" +
                "                        vt_loading.showIconLoading();\n" +
                "                    },\n" +
                "                    success: function (result) {\n" +
                "                        if (result.error != null) {\n" +
                "                            vt_loading.hideIconLoading();\n" +
                "                        } else\n" +
                "                        if (result !== null && result.length > 0) {\n" +
                "                            for (var i = 0; i < result.length; i++) {\n" +
                "                                $(\"#\" + result[i].fieldName + \"_error\").text(result[i].error);\n" +
                "                            }\n" +
                "                            setTimeout('$(\"#\"' + result[0].fieldName + ').focus()', 100);\n" +
                "                            vt_loading.hideIconLoading();\n" +
                "                        } else {\n" +
                "                            $(\"#dialog-formAddNew\").dialog(\"close\");\n" +
                "                            doSearch();\n" +
                "                            vt_loading.hideIconLoading();\n" +
                "                            vt_loading.showAlertSuccess(\"Chỉnh sửa thông tin thành công\");\n" +
                "                        }\n" +
                "\n" +
                "                    }, error: function (xhr, ajaxOption, throwErr) {\n" +
                "                        console.log(xhr);\n" +
                "                        console.log(ajaxOption);\n" +
                "                        console.log(throwErr);\n" +
                "                    }\n" +
                "                });\n" +
                "            }\n" +
                "        });\n" +
                "    };\n"
        );

        /**********************************************************************************************
         *                            addTblDocumentTypeMethod
         **********************************************************************************************/

        fileWriter.append("\n" +
                "\taddTblDocumentTypeMethod = function () {\n" +
                "        vt_form.clearError();\n" +
                "        setValueToForm();\n" +
                "        $.ajax({\n" +
                "            traditional: true,\n" +
                "            url: \"token.json\",\n" +
                "            dataType: \"text\",\n" +
                "            type: \"GET\"\n" +
                "        }).success(function (result) {\n" +
                "            if (vt_form.validate1(\"#" + uncapitalize(tableInfo.tableName) + "Form\", null, objTblDocumentType.validateRule))\n" +
                "            {\n" +
                "                var formdataTmp = new FormData();\n" +
                "                var formData = new FormData(document.getElementById(\"" + uncapitalize(tableInfo.tableName) + "Form\"));\n" +
                "                for (var pair of formData.entries()) {\n" +
                "                    formdataTmp.append(pair[0], pair[1]);\n" +
                "                }\n" +
                "                $.ajax({\n" +
                "                    url: \"add" + tableInfo.tableName.toLowerCase() + ".html\",\n" +
                "                    data: formdataTmp,\n" +
                "                    processData: false,\n" +
                "                    contentType: false,\n" +
                "                    enctype: 'multipart/form-data',\n" +
                "                    type: \"POST\",\n" +
                "                    headers: {\"X-XSRF-TOKEN\": result},\n" +
                "                    dataType: 'json',\n" +
                "                    beforeSend: function (xhr) {\n" +
                "                        vt_loading.showIconLoading();\n" +
                "                    },\n" +
                "                    success: function (result) {\n" +
                "                        if (result.error != null) {\n" +
                "                        } else if (result !== null && result.length > 0) {\n" +
                "                            for (var i = 0; i < result.length; i++) {\n" +
                "                                $(\"#\" + result[i].fieldName + \"_error\").text(result[i].error);\n" +
                "                            }\n" +
                "                            setTimeout('$(\"#' + result[0].fieldName + '\").focus()', 100);\n" +
                "                            vt_loading.hideIconLoading();\n" +
                "                        } else {\n" +
                "                            $(\"#dialog-formAddNew\").dialog(\"close\");\n" +
                "                            doSearch();\n" +
                "                            vt_loading.hideIconLoading();\n" +
                "                            vt_loading.showAlertSuccess(\"Thêm mới thành công\");\n" +
                "                        }\n" +
                "                    }, error: function (xhr, ajaxOption, throwErr) {\n" +
                "                        console.log(xhr);\n" +
                "                        console.log(ajaxOption);\n" +
                "                        console.log(throwErr);\n" +
                "                    }\n" +
                "                });\n" +
                "            }\n" +
                "\n" +
                "        });\n" +
                "    };\n" +
                "});\n\n"
        );

        /*********************************************************************************************
         *                                 var objTblDocumentType
         *********************************************************************************************/
        fileWriter.append("var objTblDocumentType = {\n" +
                /*********************************************************************************************
                 *                                 validateRule
                 *********************************************************************************************/
                "\tvalidateRule: {\n" +
                "        rules: {\n");
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if (columnProperty.isValidate())
            {
                fileWriter.append("\t\t\t"+columnProperty.getColName()+": {\n" +
                        "                required: true\n" +
                        "            }");
                if(i != tableInfo.columns.size() -1){
                    fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

        }
        fileWriter.append("        },\n");

        fileWriter.append("        messages: {\n");

        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if (columnProperty.isValidate())
            {
                fileWriter.append("\t\t\t"+columnProperty.getColName()+": {\n" +
                        "                required: \""+columnProperty.getValidateMessage()+"\"\n" +
                        "            }");
                if(i != tableInfo.columns.size() -1){
                    fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

        }
        fileWriter.append("        }\n" +
                "    },\n\n");

        /*********************************************************************************************
         *                                 editTblDocumentType
         *********************************************************************************************/
        fileWriter.append("\teditTblDocumentType: function (id) {\n" +
                "        if (id !== null) {\n" +
                "            vt_form.reset($('#"+uncapitalize(tableInfo.tableName)+"Form'));\n");
        fileWriter.append("            $(\"#isedit\").val(\"1\");\n");
        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append("            $(\"#"+uncapitalize(subTableInfo.tableName)+"_dataDetailInfo\").html(\"\");\n");
        };
        fileWriter.append( "            vt_form.clearError();\n" +
                 "            $.ajax({\n" +
                 "                async: false,\n" +
                 "                data: {gid: id},\n" +
                 "                url: \"getone"+tableInfo.tableName.toLowerCase()+"bygid.json\",\n" +
                 "                success: function (data, status, xhr) {\n" +
                 "                    $(\"#gid\").val(data.gid);\n");
        for (int i = 1; i < tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if (columnProperty.getColType().equals("Date"))
            {
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+"ST);\n");
            }
            else if (columnProperty.getInputType().equals("Combobox"))
            {

                fileWriter.append("\t\t\t\t\tvt_combobox.buildCombobox(\"cb"+columnProperty.getColName()+"\", \""+columnProperty.getComboboxBuildPath()+"\", data."+columnProperty.getColName()+", \""+columnProperty.getComboboxName()+"\", \""+columnProperty.getComboboxValue()+"\", \"- Chọn "+columnProperty.getColDescription()+" -\", 0);\n");
            }
            else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+");\n");

        }
        String file = null;
        for(int i = 0; i < tableInfo.columns.size(); i++){
            if(tableInfo.columns.get(i).getColType().equals("File")){
                file = tableInfo.columns.get(i).getColName();
            }
        }
        if(file != null){
            fileWriter.append(
                    "                var html = '';\n" +
                            "                    if (data.file !== null && data.file !== \"\") {\n" +
                            "                        html = '<a href=\"javascript:void(0)\" onclick=\"downloadFileDocument(' + data.gid + ')\"  style=\"cursor:pointer;color: blue;\"><span >' + (data.file + '').substring(0, 20) + '...' + '</span></a>';\n" +
                            "                    } else {\n" +
                            "                        html += '';\n" +
                            "                    }\n"
            );
        }

        for(TableInfo subTableInfo : tableSet.subTables) {
            fileWriter.append(uncapitalize(subTableInfo.tableName)+"indexTopicMember = 3;\n");
        }

        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append(
                    "                    var lst_" + subTableInfo.tableName + " = data." + uncapitalize(subTableInfo.tableName) + "_lstSubTable;\n" +
                    "                    for(var i=0; i<lst_" + subTableInfo.tableName + ".length;i++){\n" +
                    "                        $(\"#" + uncapitalize(subTableInfo.tableName) + "_dataDetailInfo\").append(\n" +
                    "                                " + uncapitalize(subTableInfo.tableName) + "addNewDataToTable("+uncapitalize(subTableInfo.tableName)+"indexTopicMember, lst_" + subTableInfo.tableName + "[i].main_id, ");
            for(int i = 1; i < subTableInfo.columns.size(); i++){
                ColumnProperty colProp = subTableInfo.columns.get(i);
                if(colProp.getColType().equals("Long")){
                    fileWriter.append("lst_" + subTableInfo.tableName + "[i]." + colProp.getColName() + ", ");
                    fileWriter.append("lst_" + subTableInfo.tableName + "[i]." + colProp.getColName() + "ST, ");
                }
            }
            for(int i = 1; i < subTableInfo.columns.size(); i++){
                ColumnProperty colProp = subTableInfo.columns.get(i);
                if(colProp.getColType().equals("String")){
                    fileWriter.append("lst_" + subTableInfo.tableName + "[i]." + colProp.getColName() + ", ");
                }
            }
            for(int i = 1; i < subTableInfo.columns.size(); i++){
                ColumnProperty colProp = subTableInfo.columns.get(i);
                if(colProp.getColType().equals("Date")){
                    fileWriter.append("lst_" + subTableInfo.tableName + "[i]." + colProp.getColName() + "ST, ");
                }
            }
            fileWriter.append(
                    "1)\n" +
                    "                        );\n"
                             +"\t\t"+uncapitalize(subTableInfo.tableName)+"indexTopicMember++;\n"+
                    "                    }");
        }





        fileWriter.append("\n\t\t\t\t\t$('#dialog-formAddNew').dialog({\n" +
                "                        title: \"Cập nhật thông tin " + tableInfo.description + "\"\n" +
                "                    }).dialog('open');\n" +
                "                    // set css to form\n" +
                "                    $('#dialog-formAddNew').parent().addClass(\"dialogAddEdit\");\n" +
                "                    objCommon.setTimeout(\"code\");\n" +
                "                    return false;\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "    },\n" +
                "\tgid: null,\n\n");
        /*********************************************************************************************
         *                                 deleteTblDocumentType
         *********************************************************************************************/
        fileWriter.append(
                "    deleteTblDocumentType: function (gid) {\n" +
                        "        if (gid !== null) {\n" +
                        "            var tmp_mess = '\"' + $(\"#dataTblDocumentType #\" + gid).parent().parent().parent().find(\".expand\").text() + '\"';\n" +
                        "            vt_loading.showConfirmDeleteDialog(\"Bạn có chắc chắn muốn xóa danh mục\" + \" \" + tmp_mess, function (callback) {\n" +
                        "                if (callback) {\n" +
                        "                    objTblDocumentType.gid = gid;\n" +
                        "                    objTblDocumentType.deleteOneTblDocumentType();\n" +
                        "                }\n" +
                        "            });\n" +
                        "        }\n" +
                        "    },\n\n");

        /*********************************************************************************************
         *                                 deleteOneTblDocumentType
         *********************************************************************************************/
        fileWriter.append(
                "\tdeleteOneTblDocumentType: function () {\n" +
                        "        vt_loading.showIconLoading();\n" +
                        "        var ids = objTblDocumentType.gid;\n" +
                        "        var onDone = function (result) {\n" +
                        "            if (result !== null && result.hasError) {\n" +
                        "                $(\"#deleteDialogMessageError\").text(result.error);\n" +
                        "                vt_loading.hideIconLoading();\n" +
                        "            } else {\n" +
                        "                $(\"#dialog-confirmDelete\").dialog(\"close\");\n" +
                        "                if ($(\"#allValue\").val() === ids && pagenum > 1) {\n" +
                        "                    pagenum--;\n" +
                        "                }\n" +
                        "                doSearch();\n" +
                        "                vt_loading.hideIconLoading();\n" +
                        "                vt_loading.showAlertSuccess(\"Xóa thành công\");\n" +
                        "            }\n" +
                        "        };\n" +
                        "        vt_form.ajax(\"POST\", \"delete" + tableInfo.tableName.toLowerCase() + ".html\", {lstFirst: ids}, null, null, onDone);\n" +
                        "    },\n"
        );

        /*********************************************************************************************
         *                                 Viewjsp
         *********************************************************************************************/
        fileWriter.append("\n\tview: function(id) {\n" +
                "        if (id !== null) {\n" +
                "            vt_form.reset($('#"+uncapitalize(tableInfo.tableName)+"Form'));\n" +
                "            vt_form.clearError();\n" +
                "            $.ajax({\n" +
                "                async: false,\n" +
                "                data: {gid: id},\n" +
                "                url: \"getone"+tableInfo.tableName.toLowerCase()+"bygid.json\",\n" +
                "                success: function (data, status, xhr) {\n" +
                "                    $(\"#gid\").val(data.gid);\n");
        for (int i = 1; i < tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if (columnProperty.getColType().equals("Date"))
            {
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"View"+"\").val(data."+columnProperty.getColName()+"ST);\n");
            }
            else if (columnProperty.getInputType().equals("Combobox"))
            {
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"View"+"\").val(data."+columnProperty.getColName()+"ST"+");\n");
            }
            else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"View"+"\").val(data."+columnProperty.getColName()+");\n");

        }
        fileWriter.append("\n\t\t\t\t\t$('#dialog-formView').dialog({\n" +
                "                        title: \"Xem " + tableInfo.description + "\"\n" +
                "                    }).dialog('open');\n" +
                "                    // set css to form\n" +
                "                    $('#dialog-formView').parent().addClass(\"dialogAddEdit\");\n" +
                "                    objCommon.setTimeout(\"code\");\n" +
                "                    return false;\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "    }\n" +
                "}");
        fileWriter.close();
    }
    
    public static void genView(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\View.jsp");
        fileWriter.write("<%@ page contentType=\"text/html;charset=UTF-8\" %>\n" +
                    "<%@ taglib prefix=\"spring\" uri=\"http://www.springframework.org/tags\" %>\n" +
                    "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>\n" +
                    "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\" %>\n" +
                    "<%@ taglib uri=\"http://www.springframework.org/tags/form\" prefix=\"form\"%>  \n" +
                    "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>\n" +
                    "<div id=\"dialog-formView\">\n" +
                    "\t<form:form id=\"" + uncapitalize(tableInfo.tableName) + "Form\" modelAttribute=\"" + uncapitalize(tableInfo.tableName) + "Form\" class=\"form-horizontal\">\t\n" +
                    "\t\t<input type=\"hidden\" name=\"${_csrf.parameterName}\" value=\"${_csrf.token}\" />\n" +
                    "\t\t<input type=\"hidden\" id=\"gid\" name=\"gid\" value=\"\"/>\n" +
                    "\t\t<input type=\"hidden\" id=\"isedit\" name=\"isedit\" value=\"0\"/>\n" +
                    "\t\t<fieldset>\n");
            fileWriter.append("\t\t\t<legend class=\"fs-legend-head\">\n" +
                    "\t\t\t\t<span class=\"iconFS\"></span>\n" +
                    "\t\t\t\t<span class=\"titleFS\" style=\"color: #047fcd !important;\"><b>Thông tin chung</b></span>\n" +
                    "\t\t\t</legend>\n");
            int k = tableInfo.columns.size() - 1;
            int r = k / 4;
            int q = k % 4;
            for (int i = 0; i < r; i++) {
                fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
                for (int j = 0; j <= 3; j++) {
                    String res = "\t\t\t\t<label class=\"col-md-1 control-label \">" + tableInfo.columns.get(4*i+j+1).getColDescription() + "</label>\n" +
                            "\t\t\t\t<div class=\"col-md-2\">\n" +
                            "\t\t\t\t\t<input class=\"form-control\" placeholder=\"\" name=\"\" id=\""+tableInfo.columns.get(4*i+j+1).getColName()+"\" type=\"text\" readonly=\"true\"/>\n"+
                            "\t\t\t\t</div>\n";
                    fileWriter.append(res);
                }
                fileWriter.append("\t\t\t</div>\n\n");
            }
            if (q != 0) {
                fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
                for (int i = r * 4 + 1; i <= k; i++) {
                    String res = "\t\t\t\t<label class=\"col-md-1 control-label \">" + tableInfo.columns.get(i).getColDescription() + "</label>\n" +
                            "\t\t\t\t<div class=\"col-md-2\">\n" +
                            "\t\t\t\t\t<input class=\"form-control\" placeholder=\"\" name=\"\" id=\""+tableInfo.columns.get(i).getColName()+"\" type=\"text\" readonly=\"true\"/>\n"+
                            "\t\t\t\t</div>\n";
                    fileWriter.append(res);
                }
                fileWriter.append("\t\t\t</div>\n\n");
            }
            fileWriter.append("\t\t</fieldset>\n" +
                    "\t</form:form>\n" +
                    "</div>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "\t$(\"#dialog-formView\").dialog({\n" +
                    "\t\twidth: isMobile.any() ? $(window).width() : ($(window).width() / 5 * 4),\n" +
                    "\t\theight: $(window).height() / 5 * 4,\n" +
                    "\t\tautoOpen: false,\n" +
                    "\t\tmodal: true,\n" +
                    "\t\tposition: [($(window).width() / 10 * 1), 50],\n" +
                    "\t\topen: function () {\n" +
                    "\t\t\t$('.areaTable').addClass('custom-overlay-popup-add-edit');\n" +
                    "\t\t\t$('.dialogAddEdit').css('z-index', 1001);\n" +
                    "\n" +
                    "\t\t},\n" +
                    "\t\tclose: function () {\n" +
                    "\t\t\t$('.areaTable').removeClass('custom-overlay-popup-add-edit');\n" +
                    "\n" +
                    "\t\t},\n" +
                    "\t\tbuttons: [{\n" +
                    "\t\t\thtml: \"<fmt:message key='button.close' />\",\n" +
                    "\t\t\t\"class\": \"btn btn-default\",\n" +
                    "\t\t\tclick: function () {\n" +
                    "\t\t\t$(this).dialog('close');\n" +
                    "\t\t\t}\n" +
                    " \t\t\t}, {\n" +
                    "\t\t\t\thtml: \"<fmt:message key='button.update' />\",\n" +
                    "\t\t\t\t\"class\": \"btn btn-primary\",\n" +
                    "\t\t\t\t\"id\": \"btnAddTblInfoNotifyYes\",\n" +
                    "\t\t\t\tclick: function () {\n" +
                    "\t\t\t\t\tvar item = $('#isedit').val();\n" +
                    "\t\t\t\t\tif (item === '0') {\n" +
                    "\t\t\t\t\t\taddTblDocumentTypeMethod();\n" +
                    "\t\t\t\t\t} else {\n" +
                    "\t\t\t\t\t\teditTblDocumentTypeMethod();\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t]\n" +
                    "\t});\n" +
                    "</script>");
            fileWriter.close();
    
    }

    public static int dem(TableInfo tableInfo){
        int dem = 0;
        //string
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String") && tableInfo.columns.get(i).isShow() == true){
                dem++;
            }
        }

        // combobox
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).getColType().equals("Long") && tableInfo.columns.get(i).isShow() == true){
                dem++;
            }
        }
        //date

        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date") && tableInfo.columns.get(i).isShow() == true){
                dem++;
            }
        }
        return dem;
    }

    public static void genSubTableJs(TableInfo tableInfo,String fatherTableName,String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder +"\\"+uncapitalize(tableInfo.tableName) +"SubTable.js");
        fileWriter.write("var "+uncapitalize(tableInfo.tableName) +"indexTopicMember = 2;\n" +
                "var strFile = \"\";\n" +
                "var gid = 0;\n" );
        /*********************************************************************************************
         *                                 onClickAddData func
         *********************************************************************************************/
        fileWriter.append("function "+uncapitalize(tableInfo.tableName)+"onClickAddData() {\n" +
                "    vt_form.reset($('#subTableForm"+uncapitalize(tableInfo.tableName)+"'));\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"isedit1\").val(\"0\");\n" +
                "    //document.getElementById('div_btn_delete_file').style.display = \"none\";\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc\").val(\"0\");\n" +
                "    vt_form.clearError();\n");

        boolean checkfile = false;
        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox")){
                ColumnProperty colProp = tableInfo.columns.get(i);
                fileWriter.append("\tvt_combobox.buildCombobox(\"cb"+uncapitalize(tableInfo.tableName) + colProp.getColName() + "\", \"" + colProp.getComboboxBuildPath() + "\", 0, \"" + colProp.getComboboxName() + "\", \"" + colProp.getComboboxValue() + "\", \"- Chọn " + colProp.getColDescription() + " -\", 0);\n");
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("file")){
                checkfile = true;
                break;
            }
        }
        if (checkfile == true){
            fileWriter.append("\t$(\"#fileTopicFilesTmpSubTable"+uncapitalize(tableInfo.tableName)+"\").html(\"\");\n" +
                    "\tvar html = \"\";\n" +
                    "\thtml += '<input class=\"form-control\" placeholder=\"\" name=\"filestTmpSubTable\" id=\"filestTmpSubTable"+uncapitalize(tableInfo.tableName)+"\" type=\"file\"/>';\n" +
                    "\thtml += '<span id=\"filestTmpSubTable"+uncapitalize(tableInfo.tableName)+"_error\" class=\"note note-error\"></span>';\n" +
                    "\t$(\"#fileTopicFilesTmpSubTable"+uncapitalize(tableInfo.tableName)+"\").html(html);\n");
        }
        //STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("\t$(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val(\"\");\n");
            }
        }

        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("\t$(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val(\"\");\n");
            }
        }
        //DATE PICKER
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("\n\t$(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").datepicker({\n" +
                        "        duration: \"fast\",\n" +
                        "        changeMonth: true,\n" +
                        "        changeYear: true,\n" +
                        "        dateFormat: 'dd/mm/yy',\n" +
                        "        constrainInput: true,\n" +
                        "        disabled: false,\n" +
                        "        yearRange: \"-10:+50\",\n" +
                        "        onSelect: function (selected) {\n" +
                        "        }\n" +
                        "    });\n");
            }
        }




        fileWriter.append("    $('#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"').dialog('open');\n" +
                "    $('#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"').parent().addClass(\"dialogAddEditMemberTopic"+uncapitalize(tableInfo.tableName)+"\");\n" +
                "    $('.dialogAddEditMemberTopic"+uncapitalize(tableInfo.tableName)+"').find('.ui-dialog-title').empty().append(\"Thêm mới bảng con "+uncapitalize(tableInfo.tableName)+" \");\n");
        fileWriter.append("\n}\n");
        /*********************************************************************************************
         *                                 on click remove
         *********************************************************************************************/
        fileWriter.append("$(document).on(\"click\", \".remove-cha-bomb\", function () {\n" +
                "    var id = $(this).attr(\"data-id\");\n" +
                "    $(\"#dataChaBomb_\" + id).remove();\n" +
                "    "+uncapitalize(tableInfo.tableName)+"reloadSttMember();\n" +
                "    "+uncapitalize(tableInfo.tableName)+"reloadMemberIndex();\n" +
                "    "+uncapitalize(tableInfo.tableName)+"indexTopicMember--;\n" +
                "});\n");
        /*********************************************************************************************
         *                                 setValueToFormSubTable for combobox
         *********************************************************************************************/
        fileWriter.append(uncapitalize(tableInfo.tableName)+"setValueToFormSubTable = function () {\n");
        fileWriter.append("    var item;\n");
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).getColType().equals("Long")){
                ColumnProperty colProp = tableInfo.columns.get(i);
                fileWriter.append("\titem = $('#cb"+uncapitalize(tableInfo.tableName)+colProp.getColName()+"Combobox').val();\n"+
                        "\t$('input[name=\""+uncapitalize(tableInfo.tableName)+colProp.getColName()+"\"]').val(item);\n");
            }
        }
        fileWriter.append("};\n");
        /*********************************************************************************************
         *                                 saveTopicMember
         *********************************************************************************************/
        fileWriter.append("\nfunction "+uncapitalize(tableInfo.tableName)+"saveTopicMember() {\n" +
                "\n" +
                "    vt_form.clearError();\n" +
                "    "+uncapitalize(tableInfo.tableName)+"setValueToFormSubTable();\n" +
                "    if (vt_form.validate1(\"#subTableForm"+uncapitalize(tableInfo.tableName)+"\", null, "+uncapitalize(tableInfo.tableName)+"SubTable.validateRule)) {\n" +
                "        //lưu file\n" +
                "        $.ajax({\n" +
                "            traditional: true,\n" +
                "            url: \"token.json\",\n" +
                "            dataType: \"text\",\n" +
                "            type: \"GET\"\n" +
                "        }).success(function (result) {\n" +
                "            var formdataTmp = new FormData();\n" +
                "            var formData = new FormData(document.getElementById(\"subTableForm"+uncapitalize(tableInfo.tableName)+"\"));\n" +
                "            for (var pair of formData.entries()) {\n" +
                "                formdataTmp.append(pair[0], pair[1]);\n" +
                "            }\n" +
                "            $.ajax({\n" +
                "                async: false,\n" +
                "                url: \""+fatherTableName.toLowerCase()+"addfile.html\",\n" +
                "                data: formdataTmp,\n" +
                "                processData: false,\n" +
                "                contentType: false,\n" +
                "                enctype: 'multipart/form-data',\n" +
                "                type: \"POST\",\n" +
                "                headers: {\"X-XSRF-TOKEN\": result},\n" +
                "                dataType: 'json'\n" +
                "            }).success(function (result) {\n");
        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#cb"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Combobox\").val();\n" +
                        "                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Text = $(\"#cb"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Combobox>option:selected\").html();\n");
            }
        }
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        int demxuongdong = 0;
        fileWriter.append("\n                $(\"#"+uncapitalize(tableInfo.tableName)+"_dataDetailInfo\").append(\n" +
                "                        "+uncapitalize(tableInfo.tableName)+"addNewDataToTable("+uncapitalize(tableInfo.tableName)+"indexTopicMember, gid, ");
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                demxuongdong++;
                fileWriter.append(uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+", "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Text, ");
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                demxuongdong++;
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    fileWriter.append("result.name, ");
                }else{
                    fileWriter.append(uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+", ");
                }

                if (demxuongdong == 11){
                    fileWriter.append("\n");
                }
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append(uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+", ");
            }
        }
        fileWriter.append("1)\n"+
                "                        );"
        );
        fileWriter.append("\n                "+uncapitalize(tableInfo.tableName)+"reloadMemberIndex();\n" +
                "                "+uncapitalize(tableInfo.tableName)+"indexTopicMember++;\n" +
                "                "+uncapitalize(tableInfo.tableName)+"reloadSttMember();\n" +
                "                $(\"#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"\").dialog(\"close\");\n" +
                "            });\n" +
                "        });\n" +
                "   }\n" +
                "}\n");


        /*********************************************************************************************
         *                                 reloadSttMember
         *********************************************************************************************/
        fileWriter.append("function "+uncapitalize(tableInfo.tableName)+"reloadSttMember() {\n" +
                "    var count = 0;\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"_dataDetailInfo tr\").each(function () {\n" +
                "        count++;\n" +
                "        $(this).find(\"td:first-child\").html(count);\n" +
                "    });\n" +
                "}\n");

        /*********************************************************************************************
         *                                 reloadMemberIndex
         *********************************************************************************************/
        fileWriter.append("function "+uncapitalize(tableInfo.tableName)+"reloadMemberIndex() {\n" +
                "    var count = 2;\n" +
                "    $('.dataChaBomb-child').each(function (i, obj) {\n" +
                "        count++;\n");

        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"name\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n" +
                        "        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"id\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n");
            }
        }

        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"name\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n" +
                        "        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"id\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n");
            }
        }
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"name\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n" +
                        "        $(this).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").attr(\"id\", \""+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"\");\n");
            }

        }
        fileWriter.append("    });\n" +
                "}\n");
        /*********************************************************************************************
         *                                 addNewDataToTable
         *********************************************************************************************/
        demxuongdong = 0;
        fileWriter.append("\nfunction "+uncapitalize(tableInfo.tableName)+"addNewDataToTable(count, id, ");
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                demxuongdong++;
                fileWriter.append(tableInfo.columns.get(i).getColName()+", "+tableInfo.columns.get(i).getColName()+"Text, ");
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                demxuongdong++;
                fileWriter.append(tableInfo.columns.get(i).getColName()+", ");
                if (demxuongdong == 11){
                    fileWriter.append("\n");
                }
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append(tableInfo.columns.get(i).getColName()+", ");
            }
        }
        fileWriter.append("isEdit) {\n");
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("    "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = "+tableInfo.columns.get(i).getColName()+" !== null ? "+tableInfo.columns.get(i).getColName()+" : \"\";\n");
            }
        }
        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("    "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = "+tableInfo.columns.get(i).getColName()+" !== null ? "+tableInfo.columns.get(i).getColName()+" : 0;\n");
            }
        }
        fileWriter.append("\n\tgid = id;\n");
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("    "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = "+tableInfo.columns.get(i).getColName()+" !== null ? "+tableInfo.columns.get(i).getColName()+" : \"\";\n");
            }
        }
        // COMBOBOX TEXT
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("\tvar txt"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = "+tableInfo.columns.get(i).getColName()+" > 0 ? ("+tableInfo.columns.get(i).getColName()+"Text + '').trim() : \"\";\n");
            }
        }
        // html + nhung thang hien ra
        fileWriter.append("\n\tvar html = \"<tr class='dataChaBomb-child' id='dataChaBomb_\" + count + \"'>\";\n" +
                "    html += \"<td align='center' valign='middle'>\" + (count - 2) + \"</td>\";\n");
        //string
        int countS = dem(tableInfo);
        if (countS%2 == 1){

        }else{
            countS++;
        }

        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).isShow() == true){
                if (tableInfo.columns.get(i).getColType().equals("String")  && !tableInfo.columns.get(i).getInputType().equals("file")){
                    if (countS %2 == 1){
                        fileWriter.append("    html += \"<td align='left' valign='middle'>\" + vt_util.escapeHTML("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+") + \"</td>\";\n");
                    }else{
                        fileWriter.append("    html += \"<td align='center' valign='middle'>\" + vt_util.escapeHTML("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+") + \"</td>\";\n");
                    }
                    countS--;
                }else if (tableInfo.columns.get(i).getColType().equals("Date")){
                    if (countS%2 == 1){
                        fileWriter.append("    html += \"<td align='left' valign='middle'>\" + vt_util.escapeHTML("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+") + \"</td>\";\n");
                    }else{
                        fileWriter.append("    html += \"<td align='center' valign='middle'>\" + vt_util.escapeHTML("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+") + \"</td>\";\n");
                    }
                    countS--;
                }else if (tableInfo.columns.get(i).getInputType().equals("Combobox") && tableInfo.columns.get(i).getColType().equals("Long")){
                    fileWriter.append("    html += \"<td align='left' valign='middle'>\" + vt_util.escapeHTML(txt"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+") + \"</td>\";\n");
                    countS--;
                }
            }
        }

        fileWriter.append("    html += \"<td align='left' valign='middle'>\" + vt_util.escapeHTML('');");
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("file")){
                fileWriter.append("\n    if ("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" !== null && "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" !== \"\") {\n" +
                        "        html += '<a href=\"javascript:void(0)\" onclick=\"downloadFileDocument1(\\'' + "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" + '\\')\"  style=\"cursor:pointer;color: blue;\"><span >' + ("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" + '').substring(0, 30) + '...' + '</span></a></td>'; //\n" +
                        "    } else {\n" +
                        "        html += \"</td>\";\n" +
                        "    }\n");
            }
        }

        // INPUT
        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")) {
                fileWriter.append("    html += \"<input class='" +uncapitalize(tableInfo.tableName)+ tableInfo.columns.get(i).getColName() + "' type='hidden' value='\" + " +uncapitalize(tableInfo.tableName)+ tableInfo.columns.get(i).getColName() + " + \"' name='"+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName() + "' />\";\n");
            }
        }
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("    html += \"<input type='hidden' class='"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"' value='\" + "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" + \"' name='"+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"' />\";\n");
            }
        }
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("    html += \"<input type='hidden' class='"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"' value='\" + "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" + \"' name='"+uncapitalize(tableInfo.tableName)+"_lstSubTable[\" + (count - 1) + \"]." + tableInfo.columns.get(i).getColName()+"' />\";");
            }
        }
        fileWriter.append("\n    if (isEdit === 1) {\n" +
                "        html += '<td align=\"center\"><a data-id=\"' + count + '\" class=\"remove-cha-bomb\" style=\"cursor:pointer;\"><span title=\"Xóa\" class=\"fa fa-trash-o  fa-lg\"></span></a> |';\n" +
                "        html += '<a data-id=\"' + count + '\" class=\"edit-cha-bomb\" style=\"cursor:pointer;\"><span title=\"Chỉnh sửa\" class=\"fa fa-edit fa-lg\"></span></a>';\n" +
                "        html += \"</td>\";\n" +
                "    }\n" +
                "\n" +
                "    html += \"</tr>\";\n" +
                "    return html;\n" +
                "}\n");

        fileWriter.append(uncapitalize(tableInfo.tableName)+"downloadFileDocumentSubtable = function (filename) {\n" +
                "    location.href = \"/TopicFiles/\" + filename;\n" +
                "};\n");
        fileWriter.append("//edit dialog \n");
        /*********************************************************************************************
         *                                 Edit dialog
         *********************************************************************************************/
        fileWriter.append("$(document).on(\"click\", \".edit-cha-bomb\", function () {\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"isedit1\").val(\"\");\n"+
                "    vt_form.clearReadOnlyInput($('#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"'));\n" +
                "    $('#btnAddRole').removeAttr(\"btnAddRole1\");\n" +
                "    //document.getElementById('div_btn_delete_file').style.display = \"block\";\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc\").val(\"0\");\n" +
                "    var id = $(this).attr(\"data-id\");\n");
        //Combobox
        //tableInfo.columns.get(i).getColName()
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")) {
                fileWriter.append("    var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(this).closest(\"tr\").find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("    var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(this).closest(\"tr\").find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")) {
                fileWriter.append("    var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(this).closest(\"tr\").find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        //build combobox
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("Combobox")){
                ColumnProperty colProp = tableInfo.columns.get(i);
                fileWriter.append("\tvt_combobox.buildCombobox(\"cb"+uncapitalize(tableInfo.tableName) + colProp.getColName() + "\", \"" + colProp.getComboboxBuildPath() + "\", " + uncapitalize(tableInfo.tableName) +colProp.getColName()+", \"" + colProp.getComboboxName() + "\", \"" + colProp.getComboboxValue() + "\", \"- Chọn " + colProp.getColDescription() + " -\", 0);\n");
            }
        }
        fileWriter.append("\n" +
                "    $(\"#create_time111_subdoc\").val($('#create_time111').val());\n" +
                "    $(\"#user_create1_subdoc\").val($('#user_create1').val());");
        //String
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")) {
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    fileWriter.append("\n\tvar html = \"\";\n" +
                            "    if("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" !== null){\n" +
                            "        html += "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+";\n" +
                            "        html += '<input class=\"form-control\" placeholder=\"\" name=\"filestTmpSubTable\" id=\"filestTmpSubTable"+uncapitalize(tableInfo.tableName)+"\" type=\"file\">';\n" +
                            "    }\n" +
                            "    else{\n" +
                            "        html += '<input class=\"form-control\" placeholder=\"\" name=\"filestTmpSubTable\" id=\"filestTmpSubTable"+uncapitalize(tableInfo.tableName)+"\" type=\"file\">';\n" +
                            "    }\n" +
                            "    $(\"#fileTopicFilesTmpSubTable"+uncapitalize(tableInfo.tableName)+"\").html(html);\n");
                }else{
                    fileWriter.append("\n    $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+");");
                }

            }
        }

        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")) {
                fileWriter.append("\n    $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+");");
            }
        }
        //date picker
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")) {
                fileWriter.append("\n    $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").datepicker({\n" +
                        "        duration: \"fast\",\n" +
                        "        changeMonth: true,\n" +
                        "        changeYear: true,\n" +
                        "        dateFormat: 'dd/mm/yy',\n" +
                        "        constrainInput: true,\n" +
                        "        disabled: false,\n" +
                        "        yearRange: \"-10:+50\",\n" +
                        "        onSelect: function (selected) {\n" +
                        "        }\n" +
                        "    });");
            }
        }
        fileWriter.append("\n"+
                "    $('input[type=\"file\"]').change(function (e) {\n" +
                "        strFile = e.target.files[0].name;\n" +
                "    });\n" +
                "    $(\"#"+uncapitalize(tableInfo.tableName)+"isedit1\").val(id);\n" +
                "    $('#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"').dialog('open');\n" +
                "    $('#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"').parent().addClass(\"dialogAddEditMemberTopic"+uncapitalize(tableInfo.tableName)+"\");\n" +
                "    $('.dialogAddEditMemberTopic"+uncapitalize(tableInfo.tableName)+"').find('.ui-dialog-title').empty().append(\"Chỉnh sửa thông tin "+uncapitalize(tableInfo.tableName)+"\");\n" +
                "    return false;\n" +
                "});\n");
        /*********************************************************************************************
         *                                 editTopicMember
         *********************************************************************************************/
        fileWriter.append("function "+uncapitalize(tableInfo.tableName)+"editTopicMember() {\n" +
                "    vt_form.clearError();\n" +
                "    "+uncapitalize(tableInfo.tableName)+"setValueToFormSubTable();\n" +
                "    //lưu file\n" +
                "    $.ajax({\n" +
                "        traditional: true,\n" +
                "        url: \"token.json\",\n" +
                "        dataType: \"text\",\n" +
                "        type: \"GET\"\n" +
                "    }).success(function (result) {\n" +
                "\n" +
                "        var formdataTmp = new FormData();\n" +
                "        var formData = new FormData(document.getElementById(\"subTableForm"+uncapitalize(tableInfo.tableName)+"\"));\n" +
                "        for (var pair of formData.entries()) {\n" +
                "            formdataTmp.append(pair[0], pair[1]);\n" +
                "        }\n" +
                "        $.ajax({\n" +
                "            async: false,\n" +
                "            url: \""+fatherTableName.toLowerCase()+"addfile.html\",\n" +
                "            data: formdataTmp,\n" +
                "            processData: false,\n" +
                "            contentType: false,\n" +
                "            enctype: 'multipart/form-data',\n" +
                "            type: \"POST\",\n" +
                "            headers: {\"X-XSRF-TOKEN\": result},\n" +
                "            dataType: 'json'\n" +
                "        }).success(function (result) {\n"

        );
        fileWriter.append("            if (vt_form.validate1(\"#subTableForm"+uncapitalize(tableInfo.tableName)+"\", null, "+uncapitalize(tableInfo.tableName)+"SubTable.validateRule)) {\n" +
                "                var id = $(\"#"+uncapitalize(tableInfo.tableName)+"isedit1\").val();\n");
        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#cb"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Combobox\").val();\n" +
                        "                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Text = $(\"#cb"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Combobox>option:selected\").html();\n");
            }
        }
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("                var "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = $(\"#"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val();\n");
            }
        }

        // COMBOBOXTEXT
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("                var txt"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" = "+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+" > 0 ? ("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"Text + '').trim() : \"\";\n");
            }
        }


        // html + nhung thang hien ra ChaBombChild
        int nth_child = 2;
        ArrayList<ColumnProperty> table_isshow_notfile = columns_isshow_notfile(tableInfo);
        for (int i = 0;i<table_isshow_notfile.size();i++){
            if (table_isshow_notfile.get(i).getColType().equals("String")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"td:nth-child("+nth_child+")\").html("+uncapitalize(tableInfo.tableName)+table_isshow_notfile.get(i).getColName()+");\n");

            }else if (table_isshow_notfile.get(i).getColType().equals("Date")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"td:nth-child("+nth_child+")\").html("+uncapitalize(tableInfo.tableName)+table_isshow_notfile.get(i).getColName()+");\n");
            }
            else if (table_isshow_notfile.get(i).getInputType().equals("Combobox") && table_isshow_notfile.get(i).getColType().equals("Long")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"td:nth-child("+nth_child+")\").html(txt"+uncapitalize(tableInfo.tableName)+table_isshow_notfile.get(i).getColName()+");\n");
            }
            nth_child++;
        }

        // check file
        if (checkfile == true){
            for (int i = 0;i<tableInfo.columns.size();i++){
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    fileWriter.append("//                if($('#"+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc').val() === '0'){\n" +
                            "                    $(\"#dataChaBomb_\" + id).find(\"td:nth-child("+nth_child+")\").html('<a href=\"javascript:void(0)\" onclick=\"downloadFileDocument1(\\'' + id + '\\')\"  style=\"cursor:pointer; color: blue;\"><span >' + (result.name + '').substring(0, 30) + '...' + '</span></a>');\n" +
                            "//                }\n" +
                            "//                else{\n" +
                            "//                    $(\"#dataChaBomb_\" + id).find(\"td:nth-child("+nth_child+")\").html('');\n" +
                            "//                }\n");
                }
            }
        }

        // COMBOBOX
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Long") &&tableInfo.columns.get(i).getInputType().equals("Combobox")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+");\n");
            }
        }
        // STRING
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("String") && !tableInfo.columns.get(i).getInputType().equals("file")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+");\n");
            }
        }

        //file neu co
        if (checkfile == true){
            for (int i = 0;i<tableInfo.columns.size();i++){
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    fileWriter.append("//                if($('#"+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc').val() === '0'){\n" +
                            "                    $(\"#dataChaBomb_\" + id).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val(result.name);\n" +
                            "//                }\n" +
                            "//                else{\n" +
                            "//                    $(\"#dataChaBomb_\" + id).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val(\"\");\n" +
                            "//               }\n");
                }
            }
        }
        //+uncapitalize(tableInfo.tableName)+
        //DATE
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getColType().equals("Date")){
                fileWriter.append("                $(\"#dataChaBomb_\" + id).find(\"."+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\").val("+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+");\n");
            }
        }

        fileWriter.append("\n                "+uncapitalize(tableInfo.tableName)+"reloadMemberIndex();\n" +
                "                "+uncapitalize(tableInfo.tableName)+"indexTopicMember++;\n" +
                "                "+uncapitalize(tableInfo.tableName)+"reloadSttMember();\n" +
                "                $(\"#dialog-formAddTopicMember"+uncapitalize(tableInfo.tableName)+"\").dialog(\"close\");\n" +
                "            }\n" +
                "        });\n");
        fileWriter.append(
                "   });\n" +
                "}\n");

//        fileWriter.append("//        $.ajax({\\n\" +\n" +
//                "                \"//        async: false,\\n\" +\n" +
//                "                \"//                url: \\\"updatefilessubtable.html\\\",\\n\" +\n" +
//                "                \"//                data: formdataTmp,\\n\" +\n" +
//                "                \"//                processData: false,\\n\" +\n" +
//                "                \"//                contentType: false,\\n\" +\n" +
//                "                \"//                enctype: 'multipart/form-data',\\n\" +\n" +
//                "                \"//                type: \\\"POST\\\",\\n\" +\n" +
//                "                \"//                headers: {\\\"X-XSRF-TOKEN\\\": result},\\n\" +\n" +
//                "                \"//                dataType: 'json',\\n\" +\n" +
//                "                \"//                beforeSend: function (xhr) {\\n\" +\n" +
//                "                \"//                vt_loading.showIconLoading();\\n\" +\n" +
//                "                \"//                },\\n\" +\n" +
//                "                \"//                success: function (data) {\\n\" +\n" +
//                "                \"//                strFile = data;\\n\" +\n" +
//                "                \"//                }, error: function (jqXHR, textStatus, errorThrown) {\\n\" +\n" +
//                "                \"////                vt_loading.showAlertFail(\\\"Thêm mới không thành công\\\");\\n\" +\n" +
//                "                \"//        }, complete: function (jqXHR, textStatus) {\\n\" +\n" +
//                "                \"//        vt_loading.hideIconLoading();\\n\" +\n" +
//                "                \"//        }\\n\" +\n" +
//                "                \"//        });\\n\"+");
        /*********************************************************************************************
         *                                 Validate
         *********************************************************************************************/
        fileWriter.append("var "+uncapitalize(tableInfo.tableName)+"SubTable = {\n" +
                "    validateRule: {\n" +
                "        rules: {\n");
        int validateLast = 0;
        for (int i = tableInfo.columns.size()-1;i>=0;i--){
            if (tableInfo.columns.get(i).isValidate() == true){
                validateLast = i;
                break;
            }
        }
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).isValidate() == true){
                if (i!= validateLast){
                    fileWriter.append("\t\t\t"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+": {\n" +
                            "                required: true\n" +
                            "            },\n");
                }else{
                    fileWriter.append("\t\t\t"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+": {\n" +
                            "                required: true\n" +
                            "            }\n" +
                            "        },\n");
                }

            }
        }
        fileWriter.append("        messages: {\n");
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).isValidate() == true){
                if (i!= validateLast){
                    fileWriter.append("\t\t\t"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+": {\n" +
                            "                 required: \""+tableInfo.columns.get(i).getValidateMessage()+"\"" +
                            "            \n\t\t\t},\n");
                }else{
                    fileWriter.append("\t\t\t"+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+": {\n" +
                            "                 required: \""+tableInfo.columns.get(i).getValidateMessage()+"\"" +
                            "            \n\t\t\t}" +
                            "           \n\t\t}\n"+
                            "\t}\n"+
                            "};\n"
                    );
                }
            }
        }
        //file neu co
        if (checkfile == true){
            for (int i = 0;i<tableInfo.columns.size();i++){
                if (tableInfo.columns.get(i).getInputType().equals("file")){
                    fileWriter.append(uncapitalize(tableInfo.tableName)+"onClickBtnDeleteFile = function(){\n" +
                            "    var html = '';\n" +
                            "    html += '<input class=\"form-control\" placeholder=\"\" name=\""+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\" id=\""+uncapitalize(tableInfo.tableName)+tableInfo.columns.get(i).getColName()+"\" type=\"file\">';\n" +
                            "    html += '<span id=\""+tableInfo.columns.get(i).getColName()+"_error\" class=\"note note-error\"></span>';\n" +
                            "    $(\"#cbfile_attach\").html(html);\n" +
                            "    $('#"+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc').val('1');\n" +
                            "};\n");
                }
            }
        }

        fileWriter.close();
    }
}
