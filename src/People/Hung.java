/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import controller.CodeGenerator;
import java.io.FileWriter;
import java.io.IOException;
import model.TableInfo;
import static controller.CodeGenerator.*;
import model.ColumnProperty;

/**
 *
 * @author hungy
 */


public class Hung {
    /*********************************************************************************************
     *                                 Viewjsp
     *********************************************************************************************/
//        fileWriter.append("\tview: function (gid) {\n" +
//                "        if (id !== null) {\n" +
//                "            vt_form.reset($('#"+uncapitalize(tableInfo.tableName)+"Form'));\n" +
//            "            vt_form.clearError();\n" +
//            "            $.ajax({\n" +
//            "                async: false,\n" +
//            "                data: {gid: id},\n" +
//            "                url: \"getone"+tableInfo.tableName.toLowerCase()+"bygid.json\",\n" +
//            "                success: function (data, status, xhr) {\n" +
//            "                    $(\"#gid\").val(data.gid);\n");
//        for (int i = 1; i < tableInfo.columns.size(); i++) {
//        ColumnProperty columnProperty = tableInfo.columns.get(i);
//        if (columnProperty.getColType().equals("Date"))
//        {
//            fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+"ST);\n");
//        }
//        else if (columnProperty.getInputType().equals("Combobox"))
//        {
//            fileWriter.append("\t\t\t\t\t$(\"#cb"+columnProperty.getColName()+"combobox"+"\").val(data."+columnProperty.getColName()+"ST"+");\n");
//        }
//        else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+");\n");
//
//    }
//        fileWriter.append("\n\t\t\t\t\t$('#dialog-formView').dialog({\n" +
//                "                        title: \"Xem " + tableInfo.description + "\"\n" +
//            "                    }).dialog('open');\n" +
//            "                    // set css to form\n" +
//            "                    $('#dialog-formView').parent().addClass(\"dialogAddEdit\");\n" +
//            "                    objCommon.setTimeout(\"code\");\n" +
//            "                    return false;\n" +
//            "                }\n" +
//            "            });\n" +
//            "        }\n" +
//            "    },\n" +
//            "\tgid: null,\n\n");
//        fileWriter.close();
    
    static void genView(TableInfo tableInfo, String folder) throws IOException {
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
}
