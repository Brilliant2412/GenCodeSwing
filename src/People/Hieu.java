/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import model.ColumnProperty;
import model.TableInfo;
import model.TableSet;

import java.io.FileWriter;
import java.io.IOException;

import static controller.CodeGenerator.capitalize;
import static controller.CodeGenerator.uncapitalize;

/**
 *
 * @author hungy
 */
public class Hieu {
    private static String checkDang(String tenTruong,String moTa,String kieuDL,String kieuNhap){
        String res = "";
        if (kieuDL.equals("String")){
            res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                    "\t\t\t\t<div class=\"col-lg-2\">\n"+
                    "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+tenTruong+"\" type=\"text\" class=\"form-control\"/>\n" +
                    "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                    "\t\t\t\t</div>\n";
        }else if (kieuDL.equals("Long") || kieuDL.equals("Double")){
            if (kieuNhap.equals("Combobox")){
                res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-2\">\n" +
                        "\t\t\t\t\t<div id=\"cb"+tenTruong+"\"></div> \n" +
                        "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+tenTruong+"\" class=\"text_hidden\"  />\n" +
                        "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                        "\t\t\t\t</div>\n";
            }else{
                res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-2\">\n" +
                        "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+tenTruong+"\" type=\"number\" class=\"form-control\"/>\n" +
                        "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>                           \n" +
                        "\t\t\t\t</div>\n";
            }
        }else if (kieuDL.equals("Date")){
            res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                    "\t\t\t\t<div class=\"col-md-2\" input-group>\n"+
                    "\t\t\t\t<input class=\"dateCalendar\" placeholder=\"Bắt đầu\" name=\""+tenTruong+"\" id=\""+tenTruong+"\" type=\"text\"/>\n" +
                    "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                    "\t\t\t\t</div>\n";
        }
        return res;
    }

    public static void genBO(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "BO.java");
        fileWriter.write(
                "package com.tav.service.bo;\n\n" +

                        "import com.tav.service.base.db.dto.BaseFWDTOImpl;\n" +
                        "import com.tav.service.base.db.model.BaseFWModelImpl;\n" +
                        "import com.tav.service.dto." + tableInfo.tableName + "DTO;\n" +
                        "import com.vividsolutions.jts.geom.Geometry;\n" +
                        "import com.vividsolutions.jts.geom.Point;\n" +
                        "import java.util.Date;\n" +
                        "import javax.persistence.Column;\n" +
                        "import javax.persistence.Entity;\n" +
                        "import javax.persistence.GeneratedValue;\n" +
                        "import javax.persistence.Id;\n" +
                        "import javax.persistence.Table;\n" +
                        "import org.hibernate.annotations.GenericGenerator;\n" +
                        "import org.hibernate.annotations.Parameter;\n" +
                        "import org.hibernate.annotations.Type;\n\n" +

                        "@Entity\n" +
                        "@Table(name = \"" + tableInfo.title + "\")\n" +
                        "public class " + tableInfo.tableName + "BO extends BaseFWModelImpl {\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            fileWriter.append("\tprivate ");
            if(tableInfo.columns.get(i).getColType().equals("File")){
                fileWriter.append("String");
            }
            else{
                fileWriter.append(tableInfo.columns.get(i).getColType());
            }
            fileWriter.append(" " + tableInfo.columns.get(i).getColName() + ";\t\t//" + tableInfo.columns.get(i).getColDescription() + "\n");
        }
        String id = tableInfo.columns.get(0).getColName();
        fileWriter.append(
                "\n\tpublic " + tableInfo.tableName + "BO(){\n" +
                        "\t\tsetColId(\"" + id + "\");\n" +
                        "\t\tsetColName(\"" + id + "\");\n" +
                        "\t\tsetUniqueColumn(new String[]{\"" + id + "\"});\n" +
                        "\t}\n\n" +

                        "\t@Id\n" +
                        "\t@GeneratedValue(generator = \"sequence\")\n" +
                        "\t@GenericGenerator(name = \"sequence\", strategy = \"sequence\",\n" +
                        "\t\tparameters = {\n" +
                        "\t\t\t@Parameter(name = \"sequence\", value = \"" + tableInfo.title + "_seq\")\n" +
                        "\t\t}\n" +
                        "\t)\n\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            String colType;
            if(colProp.getColType().equals("File")){
                colType = "String";
            }
            else{
                colType = colProp.getColType();
            }
            fileWriter.append(
                    "\t@Column(name = \"" + colProp.getColName() + "\", length = " + colProp.getLengthFormat() + ")\n" +
                            "\tpublic " + colType + " get" + capitalize(tableInfo.columns.get(i).getColName()) + "(){\n" +
                            "\t\treturn " + tableInfo.columns.get(i).getColName() + ";\n" +
                            "\t}\n\n" +
                            "\tpublic void set" + capitalize(tableInfo.columns.get(i).getColName()) + "(" + colType + " " + tableInfo.columns.get(i).getColName() + "){\n" +
                            "\t\tthis." + tableInfo.columns.get(i).getColName() + " = " + tableInfo.columns.get(i).getColName() + ";\n" +
                            "\t}\n\n"
            );
        }
        fileWriter.append(
                "\t@Override\n" +
                        "\tpublic BaseFWDTOImpl toDTO() {\n" +
                        "\t\t" + tableInfo.tableName + "DTO " + uncapitalize(tableInfo.tableName) + "DTO = new " + tableInfo.tableName + "DTO();\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            fileWriter.append("\t\t" + uncapitalize(tableInfo.tableName) + "DTO.set" + capitalize(tableInfo.columns.get(i).getColName()) + "(" + tableInfo.columns.get(i).getColName() + ");\n");
        }
        fileWriter.append(
                "\t\treturn " + uncapitalize(tableInfo.tableName) +"DTO;\n" +
                        "\t}\n" +
                        "}\n"
        );
        fileWriter.close();
    }

    public static void genDTO(TableSet tableSet, String folder) throws IOException{
        TableInfo tableInfo = tableSet.tableInfo;
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DTO.java");
        fileWriter.write(
                "package com.tav.service.dto;\n\n" +

                        "import com.tav.service.base.db.dto.BaseFWDTOImpl;\n" +
                        "import com.tav.service.bo." + tableInfo.tableName + "BO;\n" +
                        "import com.vividsolutions.jts.geom.Geometry;\n" +
                        "import com.vividsolutions.jts.geom.Point;\n" +
                        "import java.util.Date;\n" +
                        "import java.util.List;\n" +
                        "import javax.xml.bind.annotation.XmlRootElement;\n\n" +

                        "@XmlRootElement(name = \"" + tableInfo.tableName + "DTO\")\n" +
                        "public class " + tableInfo.tableName + "DTO extends BaseFWDTOImpl<" + tableInfo.tableName + "BO> {\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            String colType;
            if(colProp.getColType().equals("File")){
                colType = "String";
            }
            else{
                colType = colProp.getColType();
            }
            fileWriter.append("\tprivate " + colType + " " + colProp.getColName() + ";\t\t//" + colProp.getColDescription() + "\n");
            if(!colProp.getFKTable().equals("") || colType.equals("Date")){
                fileWriter.append("\tprivate String " + colProp.getColName() + "ST;\n");
            }
        }
        fileWriter.append("\n");
        for(int i = 0; i < tableSet.subTables.size(); i++){
            fileWriter.append("\tprivate List<CommonSubTableDTO> " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable;\n\n");
            fileWriter.append("\tpublic List<CommonSubTableDTO> get" + tableSet.subTables.get(i).tableName + "_lstSubTable(){\n" +
                    "\t\t return " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable;\n" +
                    "\t}\n\n");
            fileWriter.append("\tpublic void set" + tableSet.subTables.get(i).tableName + "_lstSubTable(List<CommonSubTableDTO> " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable){\n" +
                    "\t\tthis." + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable = " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable" + ";\n" +
                    "\t}\n\n");
        }
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            String colType;
            if(colProp.getColType().equals("File")){
                colType = "String";
            }
            else{
                colType = colProp.getColType();
            }
            fileWriter.append(
                    "\tpublic " + colType + " get" + capitalize(colProp.getColName()) + "(){\n" +
                            "\t\treturn " + colProp.getColName() + ";\n" +
                            "\t}\n\n" +

                            "\tpublic void set" + capitalize(colProp.getColName()) + "(" + colType + " " + colProp.getColName() + "){\n" +
                            "\t\tthis." + colProp.getColName() + " = " + colProp.getColName() + ";\n" +
                            "\t}\n\n"

            );
            if(!colProp.getFKTable().equals("") || colType.equals("Date")){
                fileWriter.append(
                        "\tpublic String get" + capitalize(colProp.getColName()) + "ST(){\n" +
                                "\t\treturn " + colProp.getColName() + "ST;\n" +
                                "\t}\n\n" +

                                "\tpublic void set" + capitalize(colProp.getColName()) + "ST(String " + colProp.getColName() + "ST){\n" +
                                "\t\tthis." + colProp.getColName() + "ST = " + colProp.getColName() + "ST;\n" +
                                "\t}\n\n"
                );
            }
        }
        fileWriter.append("\n");
        fileWriter.append(
                "\t@Override\n" +
                        "\tpublic " + tableInfo.tableName +"BO toModel() {\n" +
                        "\t\t" + tableInfo.tableName + "BO " + uncapitalize(tableInfo.tableName) + "BO = new " + tableInfo.tableName + "BO();\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            fileWriter.append("\t\t" + uncapitalize(tableInfo.tableName) + "BO.set" + capitalize(tableInfo.columns.get(i).getColName()) + "(" + tableInfo.columns.get(i).getColName() + ");\n");
        }
        fileWriter.append(
                "\t\treturn " + uncapitalize(tableInfo.tableName) +"BO;\n" +
                        "\t}\n\n"

        );
        String id = tableInfo.columns.get(0).getColName();
        String idType = tableInfo.columns.get(0).getColType();
        fileWriter.append(
                "\t@Override\n" +
                        "\tpublic " + idType + " getFWModelId() {\n" +
                        "\t\treturn get" + capitalize(id) +"();\n" +
                        "\t}\n\n" +

                        "\t@Override\n" +
                        "\tpublic String catchName() {\n" +
                        "\t\treturn " + id +".toString();\n" +
                        "\t}\n" +
                        "}\n"
        );
        fileWriter.close();
    }

    public static void genDTO_Web(TableSet tableSet, String folder) throws IOException {
        TableInfo tableInfo = tableSet.tableInfo;
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DTO.java");
        fileWriter.write(
                "package com.tav.web.dto;\n\n" +
                    "import java.util.List;\n" +

                    "public class " + tableInfo.tableName + "DTO{\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\tprivate ");
            if(colProp.getColType().equals("Date") || colProp.getColType().equals("File")){
                fileWriter.append("String " + colProp.getColName() + ";\t\t//" + colProp.getColDescription() + "\n");
            }
            else{
                fileWriter.append(colProp.getColType() + " " + colProp.getColName() + ";\t\t//" + colProp.getColDescription() + "\n");
            }
            if(!colProp.getFKTable().equals("") || colProp.getColType().equals("Date")){
                fileWriter.append("\tprivate String " + colProp.getColName() + "ST;\n");
            }
        }
        fileWriter.append("\n");
        for(int i = 0; i < tableSet.subTables.size(); i++){
            fileWriter.append("\tprivate List<CommonSubTableDTO> " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable;\n\n");
            fileWriter.append("\tpublic List<CommonSubTableDTO> get" + tableSet.subTables.get(i).tableName + "_lstSubTable(){\n" +
                    "\t\t return " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable;\n" +
                    "\t}\n\n");
            fileWriter.append("\tpublic void set" + tableSet.subTables.get(i).tableName + "_lstSubTable(List<CommonSubTableDTO> " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable){\n" +
                    "\t\tthis." + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable = " + uncapitalize(tableSet.subTables.get(i).tableName) + "_lstSubTable" + ";\n" +
                    "\t}\n\n");
        }
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append(
                    "\tpublic ");
            //colProp.getColType() +
            if(colProp.getColType().equals("Date") || colProp.getColType().equals("File")){
                fileWriter.append("String ");
            }
            else{
                fileWriter.append(colProp.getColType() + " ");
            }
            fileWriter.append("get"+capitalize(colProp.getColName()) + "(){\n" +
                    "\t\treturn " + colProp.getColName() + ";\n" +
                    "\t}\n\n");

            fileWriter.append(
                    "\tpublic void set" + capitalize(colProp.getColName()) + "(");
            //colProp.getColType()
            if(colProp.getColType().equals("Date") || colProp.getColType().equals("File")){
                fileWriter.append("String ");
            }
            else{
                fileWriter.append(colProp.getColType() + " ");
            }
            fileWriter.append(" " + colProp.getColName() + "){\n" +
                    "\t\tthis." + colProp.getColName() + " = " + colProp.getColName() + ";\n" +
                    "\t}\n\n"

            );
            if(!colProp.getFKTable().equals("") || colProp.getColType().equals("Date")){
                fileWriter.append(
                        "\tpublic String get" + capitalize(colProp.getColName()) + "ST(){\n" +
                                "\t\treturn " + colProp.getColName() + "ST;\n" +
                                "\t}\n\n" +

                                "\tpublic void set" + capitalize(colProp.getColName()) + "ST(String " + colProp.getColName() + "ST){\n" +
                                "\t\tthis." + colProp.getColName() + "ST = " + colProp.getColName() + "ST;\n" +
                                "\t}\n\n"
                );
            }
        }
        fileWriter.append("}");
        fileWriter.close();
    }

    public static void genDialogAdd(TableSet tableSet, String folder) throws IOException {
        TableInfo tableInfo = tableSet.tableInfo;
        FileWriter fileWriter = new FileWriter(folder + "\\dialogAdd.jsp");
        fileWriter.write("<%@ page contentType=\"text/html;charset=UTF-8\" %>\n" +
                "<%@ taglib prefix=\"spring\" uri=\"http://www.springframework.org/tags\" %>\n" +
                "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>\n" +
                "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\" %>\n" +
                "<%@ taglib uri=\"http://www.springframework.org/tags/form\" prefix=\"form\"%>  \n" +
                "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>\n"+
                "<div id=\"dialog-formAddNew\">\n" +
                "\t<form:form id=\""+uncapitalize(tableInfo.tableName)+"Form\" modelAttribute=\""+uncapitalize(tableInfo.tableName)+"Form\" class=\"form-horizontal\">\t\n" +
                "\t\t<input type=\"hidden\" name=\"${_csrf.parameterName}\" value=\"${_csrf.token}\" />\n" +
                "\t\t<input type=\"hidden\" id=\"gid\" name=\"gid\" value=\"\"/>\n" +
                "\t\t<input type=\"hidden\" id=\"isedit\" name=\"isedit\" value=\"0\"/>\n" +
                "\t\t<fieldset>\n");
        fileWriter.append("\t\t\t<legend class=\"fs-legend-head\">\n" +
                "\t\t\t\t<span class=\"iconFS\"></span>\n" +
                "\t\t\t\t<span class=\"titleFS\" style=\"color: #047fcd !important;\"><b>Thông tin chung</b></span>\n" +
                "\t\t\t</legend>\n");
        int k =tableInfo.columns.size()-1;
        int r = k/4;
        int q = k%4;
        for (int i = 0;i<r;i++){
            fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
            for (int j =0;j<=3;j++){
                String res = checkDang(tableInfo.columns.get(4*i+j+1).getColName(),
                        tableInfo.columns.get(4*i+j+1).getColDescription(),
                        tableInfo.columns.get(4*i+j+1).getColType(),
                        tableInfo.columns.get(4*i+j+1).getInputType()
                );
                fileWriter.append(res);
            }
            fileWriter.append("\t\t\t</div>\n\n");
        }
        if (q != 0){
            fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
            for (int i = r*4+1;i<=k;i++){
                String res = checkDang(tableInfo.columns.get(i).getColName(),
                        tableInfo.columns.get(i).getColDescription(),
                        tableInfo.columns.get(i).getColType(),
                        tableInfo.columns.get(i).getInputType()
                );
                fileWriter.append(res);
            }
            fileWriter.append("\t\t\t</div>\n\n");
        }
        fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n" +
                "                <label class=\"col-lg-1 control-label  lb_input\">File đính kèm</label>\n" +
                "                <div class=\"col-md-11\" id=\"fileTopicFilesTmp\">\n" +
                "                    <input class=\"form-control\" placeholder=\"\" name=\"filestTmp\" id=\"filestTmp\" type=\"file\"/>\n" +
                "                    <span id=\"filestTmp_error\" class=\"note note-error\"></span>\n" +
                "                </div>\n" +
                "            </div>\n\n");
        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append(
                    "\t\t\t<legend class=\"fs-legend-head\">\n" +
                            "                <span class=\"iconFS\"></span>\n" +
                            "                <span class=\"titleFS\" style=\"color: #047fcd !important;\"><b>Bảng con</b></span>\n" +
                            "            </legend>\n" +
                            "            <div class=\"form-group-add row\">\n" +
                            "                <table id=\"" + uncapitalize(subTableInfo.tableName) + "_tblMstDivision\" class=\"table table-striped table-bordered table-hover smart-form dataTable no-footer\">\n" +
                            "                    <thead>\n" +
                            "                        <tr>\n" +
                            "                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\">STT</th>\n");
            for(int i = 1; i < subTableInfo.columns.size(); i++){
                if(subTableInfo.columns.get(i).isShow()){
                    fileWriter.append("                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\">" + subTableInfo.columns.get(i).getColDescription() + "</th>\n");
                }

            }
            fileWriter.append(
                    "                            <th class=\"thtableresponsive tlb_class_center sorting_disabled\"><a style=\"cursor: pointer; color:white !important;\" class=\"fa fa-plus fa-lg src\" onclick=\"" + uncapitalize(subTableInfo.tableName) + "onClickAddData();\"></a></th>\n" +
                            "                        </tr>\n" +
                            "                    </thead>\n" +
                            "                    <tbody id=\"" + uncapitalize(subTableInfo.tableName) + "_dataDetailInfo\" >\n" +
                            "                    </tbody>\n" +
                            "                </table>\n" +
                            "            </div>\n");
        }
        fileWriter.append("\t\t</fieldset>\n" +
                "\t</form:form>\n" +
                "\t<jsp:include page=\"subTable.jsp\" />\n" +
                "</div>\n" +
                "<script type=\"text/javascript\">\n" +
                "\t$(\"#dialog-formAddNew\").dialog({\n" +
                "\t\twidth: isMobile.any() ? $(window).width() : ($(window).width() / 20 * 19),\n" +
                "\t\theight: $(window).height() / 5 * 5 - 80,\n" +
                "\t\tautoOpen: false,\n" +
                "\t\tmodal: true,\n" +
                "\t\tposition: [($(window).width() / 80 * 2.5), 20],\n" +
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

    public static void genController(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "Controller.java");
        fileWriter.write("package com.tav.web.controller;\n" +
                "\n" +
                "import com.google.common.base.Strings;\n" +
                "import com.tav.web.common.DateUtil;" +
                "import com.google.gson.Gson;\n" +
                "import com.google.gson.JsonObject;\n" +
                "import com.tav.common.web.form.JsonDataGrid;\n" +
                "import com.tav.web.bo.ServiceResult;\n" +
                "import com.tav.web.bo.UserSession;\n" +
                "import com.tav.web.bo.ValidationResult;\n" +
                "import com.tav.web.common.CommonConstant;\n" +
                "import com.tav.web.common.CommonFunction;\n" +
                "import com.tav.web.common.ConvertData;\n" +
                "import com.tav.web.common.ErpConstants;\n" +
                "import com.tav.web.common.StringUtil;\n" +
                "import com.tav.web.data."+ tableInfo.tableName+"Data;\n" +
                "import com.tav.web.dto."+ tableInfo.tableName+"DTO;\n" +
                "import com.tav.web.dto.ImportErrorMessage;\n" +
                "import java.util.Date;\n" +
                "import com.tav.web.dto.SearchCommonFinalDTO;\n" +
                "import com.tav.web.dto.ObjectCommonSearchDTO;\n" +
                "import java.io.BufferedOutputStream;\n" +
                "import java.io.File;\n" +
                "import java.io.FileInputStream;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.io.FileOutputStream;\n" +
                "import java.io.IOException;\n" +
                "import java.nio.file.Files;\n" +
                "import java.nio.file.Path;\n" +
                "import java.nio.file.Paths;\n" +
                "import java.text.ParseException;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Iterator;\n" +
                "import java.util.List;\n" +
                "import java.util.regex.Pattern;\n" +
                "import javax.servlet.http.HttpServletRequest;\n" +
                "import javax.servlet.http.HttpServletResponse;\n" +
                "import javax.servlet.http.HttpSession;\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "import org.apache.poi.hssf.usermodel.HSSFWorkbook;\n" +
                "import org.apache.poi.ss.usermodel.Cell;\n" +
                "import org.apache.poi.ss.usermodel.DataFormatter;\n" +
                "import org.apache.poi.ss.usermodel.Row;\n" +
                "import org.apache.poi.ss.usermodel.Sheet;\n" +
                "import org.apache.poi.ss.usermodel.Workbook;\n" +
                "import org.apache.poi.xssf.usermodel.XSSFWorkbook;\n" +
                "import org.json.JSONObject;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Controller;\n" +
                "import org.springframework.ui.Model;\n" +
                "import org.springframework.web.bind.annotation.ModelAttribute;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                "import org.springframework.web.bind.annotation.ResponseBody;\n" +
                "import org.springframework.web.multipart.MultipartFile;\n" +
                "import org.springframework.web.multipart.MultipartHttpServletRequest;\n" +
                "import org.springframework.web.servlet.ModelAndView;\n");

        fileWriter.append("\n" +
                "@Controller\n" +
                "public class "+ tableInfo.tableName+"Controller extends SubBaseController {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private "+ tableInfo.tableName+"Data "+uncapitalize(tableInfo.tableName)+"Data;\n" +
                "\n" +
                "    @RequestMapping(\"/\" + ErpConstants.RequestMapping."+ tableInfo.title.toUpperCase()+")\n" +
                "    public ModelAndView agent(Model model, HttpServletRequest request) {\n" +
                "        return new ModelAndView(\""+uncapitalize(tableInfo.tableName)+"\");\n" +
                "    }\n" +
                "\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.GET_ALL_"+ tableInfo.title.toUpperCase()+"}, method = RequestMethod.GET)\n" +
                "    @ResponseBody\n" +
                "    public JsonDataGrid getAll(HttpServletRequest request) {\n" +
                "        try {\n" +
                "            // get info paging\n" +
                "            Integer currentPage = getCurrentPage();\n" +
                "            Integer limit = getTotalRecordPerPage();\n" +
                "            Integer offset = --currentPage * limit;\n" +
                "            JsonDataGrid dataGrid = new JsonDataGrid();\n" +
                "            SearchCommonFinalDTO searchDTO = new SearchCommonFinalDTO();\n");




        int count_cb =0;
        int count_db =0;
        int count_long =0;
        int count_date =0;

        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Long") )
                {
                    if (colProp.getInputType().equals("Combobox"))
                    {
                        count_cb++;
                    }
                    else
                    {
                        count_long++;
                    }
                }
                if (colProp.getColType().equals("Double"))
                {
                    count_db++;
                }
                if (colProp.getColType().equals("Date"))
                {
                    count_date++;
                }
            }

        }
        fileWriter.append("            searchDTO.setStringKeyWord(request.getParameter(\"key\"));\n");

        for (int i = 0; i < count_cb; i++) {

            fileWriter.append("\tif (request.getParameter(\"listLong"+(i+1)+"\") != null) {\n" +
                    "                searchDTO.setListLong"+(i+1)+"(ConvertData.convertStringToListLong(request.getParameter(\"listLong"+(i+1)+"\")));\n" +
                    "            }\n");

        }
        for (int i = 0; i < 2*count_db; i+=2) {
            fileWriter.append("\ttry{\n" +
                    "                searchDTO.setDouble"+(i+1)+"(Double.parseDouble(request.getParameter(\"double"+(i+1)+"\")));\n" +
                    "                searchDTO.setDouble"+(i+2)+"(Double.parseDouble(request.getParameter(\"double"+(i+2)+"\")));\n" +
                    "            }catch(Exception ex){}\n");

        }

        for (int i = 0; i < 2*count_long; i+=2) {
            fileWriter.append("\ttry{\n" +
                    "                searchDTO.setLong"+(i+1)+"(Long.parseLong(request.getParameter(\"long"+(i+1)+"\")));\n" +
                    "                searchDTO.setLong"+(i+2)+"(Long.parseLong(request.getParameter(\"long"+(i+2)+"\")));\n" +
                    "            }catch(Exception ex){}\n");

        }

        for (int i = 0; i < 2*count_date; i+=2) {
            fileWriter.append("            searchDTO.setString"+(i+1)+"(request.getParameter(\"string"+(i+1)+"\"));\n" +
                    "            searchDTO.setString"+(i+2)+"(request.getParameter(\"string"+(i+2)+"\"));");

        }




        fileWriter.append("            List<"+ tableInfo.tableName+"DTO> lst = new ArrayList<>();\n" +
                "            Integer totalRecords = 0;\n" +
                "            totalRecords = "+uncapitalize(tableInfo.tableName)+"Data.getCount(searchDTO);\n" +
                "            if (totalRecords > 0) {\n" +
                "                lst = "+uncapitalize(tableInfo.tableName)+"Data.getAll(searchDTO, offset, limit);\n" +
                "            }\n" +
                "            dataGrid.setCurPage(getCurrentPage());\n" +
                "            dataGrid.setTotalRecords(totalRecords);\n" +
                "            dataGrid.setData(lst);\n" +
                "            return dataGrid;\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e.getMessage(), e);\n" +
                "            return null;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @RequestMapping(value = \"/\" + ErpConstants.RequestMapping.GET_"+ tableInfo.title.toUpperCase()+"_BY_ID, method = RequestMethod.GET)\n" +
                "    public @ResponseBody\n" +
                "    "+ tableInfo.tableName+"DTO getOneById(HttpServletRequest request) {\n" +
                "        Long id = Long.parseLong(request.getParameter(\"gid\"));\n" +
                "        return "+uncapitalize(tableInfo.tableName)+"Data.getOneById(id);\n" +
                "    }\n" +
                "\n" +
                "    //add\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.ADD_"+ tableInfo.title.toUpperCase()+"}, method = RequestMethod.POST, produces = ErpConstants.LANGUAGE)\n" +
                "    @ResponseBody\n" +
                "    public String addOBJ(@ModelAttribute(\"" + uncapitalize(tableInfo.tableName) + "Form\") "+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO, MultipartHttpServletRequest multipartRequest,\n" +
                "            HttpServletRequest request) throws ParseException {\n" +
                "\n" +
                "        JSONObject result;\n" +
                "        String error = validateForm("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                "        ServiceResult serviceResult;\n" +
                "        if (error != null) {\n" +
                "            return error;\n" +
                "        } else {\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getColType().equals("Date")){
                fileWriter.append(
                        "            if (!StringUtil.isEmpty(" + uncapitalize(tableInfo.tableName) + "DTO.get" + capitalize(colProp.getColName()) + "())) {\n" +
                                "                        " + uncapitalize(tableInfo.tableName) + "DTO.set" + capitalize(colProp.getColName()) + "(DateUtil.formatDate(" + uncapitalize(tableInfo.tableName) + "DTO.get" + capitalize(colProp.getColName()) + "()));\n" +
                                "            }\n"
                );
            }
        }
        String file = null;
        for(int i = 0; i < tableInfo.columns.size(); i++){
            if(tableInfo.columns.get(i).getColType().equals("File")){
                file = tableInfo.columns.get(i).getColName();
                break;
            }
        }
        if(file != null) {
            fileWriter.append(
                    "                String file = CommonFunction.uploadFileOnAdd(multipartRequest, \"filestTmp\");\n" +
                            "                " + uncapitalize(tableInfo.tableName) + "DTO.set" + capitalize(file) + "(file);\n");
        }
        fileWriter.append(
                "            serviceResult = "+uncapitalize(tableInfo.tableName)+"Data.addObj("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                        "            processServiceResult(serviceResult);\n" +
                        "            result = new JSONObject(serviceResult);\n" +
                        "        }\n" +
                        "        return result.toString();\n" +
                        "    }\n" +
                        "\n" +
                        "    //update\n" +
                        "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.UPDATE_"+ tableInfo.title.toUpperCase()+"}, method = RequestMethod.POST, produces = ErpConstants.LANGUAGE)\n" +
                        "    @ResponseBody\n" +
                        "    public String updateOBJ(@ModelAttribute(\"" + uncapitalize(tableInfo.tableName) + "Form\") "+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO, MultipartHttpServletRequest multipartRequest,\n" +
                        "            HttpServletRequest request) throws ParseException {\n" +
                        "\n" +
                        "        JSONObject result;\n" +
                        "        String error = validateForm("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                        "        ServiceResult serviceResult;\n" +
                        "        if (error != null) {\n" +
                        "            return error;\n" +
                        "        } else {\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(colProp.getColType().equals("Date")){
                fileWriter.append(
                        "            if (!StringUtil.isEmpty(" + uncapitalize(tableInfo.tableName) + "DTO.get" + capitalize(colProp.getColName()) + "())) {\n" +
                                "                        " + uncapitalize(tableInfo.tableName) + "DTO.set" + capitalize(colProp.getColName()) + "(DateUtil.formatDate(" + uncapitalize(tableInfo.tableName) + "DTO.get" + capitalize(colProp.getColName()) + "()));\n" +
                                "            }\n"
                );
            }
        }
        file = null;
        for(int i = 0; i < tableInfo.columns.size(); i++){
            if(tableInfo.columns.get(i).getColType().equals("File")){
                file = tableInfo.columns.get(i).getColName();
                break;
            }
        }
        if(file != null) {
            fileWriter.append(
                    "                String file = CommonFunction.uploadFileOnUpdate(multipartRequest, \"filestTmp\");\n" +
                            "                " + uncapitalize(tableInfo.tableName) + "DTO.set" + capitalize(file) + "(file);\n"
            );
        }
        fileWriter.append(
                "            serviceResult = "+uncapitalize(tableInfo.tableName)+"Data.updateBO("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                        "            processServiceResult(serviceResult);\n" +
                        "            result = new JSONObject(serviceResult);\n" +
                        "        }\n" +
                        "        return result.toString();\n" +
                        "    }\n" +
                        "\n" +
                        "    //validate\n" +
                        "    private String validateForm("+ tableInfo.tableName+"DTO cbChaDTO) {\n" +
                        "        List<ValidationResult> lsError = new ArrayList<>();\n" +
                        "        if (lsError.size() > 0) {\n" +
                        "            Gson gson = new Gson();\n" +
                        "            return gson.toJson(lsError);\n" +
                        "        }\n" +
                        "        return null;\n" +
                        "    }\n" +
                        "\n" +
                        "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.DELETE_"+ tableInfo.title.toUpperCase()+"}, method = RequestMethod.POST,\n" +
                        "            produces = \"text/html;charset=utf-8\")\n" +
                        "    public @ResponseBody\n" +
                        "    String deleteObj(@ModelAttribute(\"objectCommonSearchDTO\") ObjectCommonSearchDTO objectCommonSearchDTO,\n" +
                        "            HttpServletRequest request) {\n" +
                        "        HttpSession session = request.getSession();\n" +
                        "        ServiceResult serviceResult = "+uncapitalize(tableInfo.tableName)+"Data.deleteObj(objectCommonSearchDTO);\n" +
                        "        processServiceResult(serviceResult);\n" +
                        "        JSONObject result = new JSONObject(serviceResult);\n" +
                        "        return result.toString();\n" +
                        "    }\n" +
                        "\n");
        file = null;
        for(int i = 0; i < tableInfo.columns.size(); i++){
            if(tableInfo.columns.get(i).getColType().equals("File")){
                file = tableInfo.columns.get(i).getColName();
                break;
            }
        }
        if(file != null){
            fileWriter.append(
                    "\t@RequestMapping(value = \"/\" + ErpConstants.RequestMapping.DOWNLOAD_" + tableInfo.tableName.toUpperCase() + "_FILE, method = RequestMethod.GET)\n" +
                    "    public void downloadFiles(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {\n" +
                    "        String dataDirectory = CommonConstant.PATH_FOLDER_TOPIC_FILE;\n" +
                    "        Long id = null;\n" +
                    "        if (!Strings.isNullOrEmpty(request.getParameter(\"id\"))) {\n" +
                    "            id = Long.parseLong(request.getParameter(\"id\"));\n" +
                    "            " + tableInfo.tableName + "DTO objDTOTmp = " + uncapitalize(tableInfo.tableName) + "Data.getOneById(id);\n" +
                    "            if (objDTOTmp != null) {\n" +
                    "                String fileName = objDTOTmp .get" + capitalize(file) + "();\n" +
                    "                response.setContentType(MediaType.APPLICATION_OCTET_STREAM);\n" +
                    "                response.setHeader(\"Content-Transfer-Encoding\", \"binary\");\n" +
                    "                response.setHeader(\"Content-Disposition\", \"attachment; filename=\" + CommonFunction.convertFileNameVietNam(fileName));\n" +
                    "                Path file = Paths.get(dataDirectory, new String(fileName.getBytes(), \"UTF-8\"));\n" +
                    "                if (Files.exists(file)) {\n" +
                    "                    response.setContentType(\"application/vnd.ms-excel\");\n" +
                    "                    response.addHeader(\"Content-Disposition\", \"attachment; filename=\" + CommonFunction.convertFileNameVietNam(fileName));\n" +
                    "                    try {\n" +
                    "                        Files.copy(file, response.getOutputStream());\n" +
                    "                        response.getOutputStream().flush();\n" +
                    "                    } catch (IOException ex) {\n" +
                    "                        logger.error(ex);\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n"
            );
        }
        fileWriter.append("}\n");
        fileWriter.close();
    }

    public static void genSubTableController(TableSet tableSet, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\subTableController.txt");
        TableInfo tableInfo = tableSet.tableInfo;
        fileWriter.write(
                "\t@RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.GET_ALL_" + tableInfo.tableName.toUpperCase() + "_SUB_TABLE}, method = RequestMethod.GET)\n" +
                    "    @ResponseBody\n" +
                    "    public List<CommonSubTableDTO> getAllSubTable(HttpServletRequest request) {\n" +
                    "        try {\n" +
                    "            // get info paging\n" +
                    "            SearchCommonFinalDTO searchDTO = new SearchCommonFinalDTO();\n" +
                    "            searchDTO.setString1(\"" + tableInfo.tableName.toUpperCase() + "\");\n");
        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append("            searchDTO.setString2(\"" + tableInfo.tableName.toUpperCase() + "_" + subTableInfo.tableName + "\");\n");
        }
        fileWriter.append(
                "            if (request.getParameter(\"gid\") != null) {\n" +
                "                try {\n" +
                "                    searchDTO.setLong1(Long.parseLong(request.getParameter(\"gid\")));\n" +
                "                } catch (Exception e) {\n" +
                "                }\n" +
                "            }\n" +
                "            List<CommonSubTableDTO> lst = commonSubTableData.getAll(searchDTO, 0, 0);\n" +
                "\n" +
                "            for (CommonSubTableDTO item : lst) {\n"
        );
        int count_long = 0;
        for(TableInfo subTableInfo : tableSet.subTables){
            for(int i = 1; i < subTableInfo.columns.size(); i++){
                ColumnProperty colProp = subTableInfo.columns.get(i);
                if(colProp.getColType().equals("Long")){
                    count_long++;
                    fileWriter.append(
                            "                List<MstDivisionDTO> lstMst" + count_long + " = mstDivisionData.getAllMstDepartmentType(\"707\");\n" +
                            "                for (MstDivisionDTO item1 : lstMst" + count_long + ") {\n" +
                            "                    if (Objects.equals(item.get" + capitalize(colProp.getColName()) + "(), item1.getDvsValue())) {\n" +
                            "                        item.set" + capitalize(colProp.getColName()) + "ST(item1.getDvsName());\n" +
                            "                    }\n" +
                            "                }\n"
                    );
                }
            }
        }
        fileWriter.append(
                "            }\n" +
                "            return lst;\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e.getMessage(), e);\n" +
                "            return null;\n" +
                "        }\n" +
                "    }\n"
        );
        fileWriter.close();
    }
}
