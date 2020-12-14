/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import model.ColumnProperty;
import model.TableInfo;

import java.io.FileWriter;
import java.io.IOException;

import static controller.CodeGenerator.capitalize;
import static controller.CodeGenerator.uncapitalize;

/**
 *
 * @author hungy
 */
public class Hieu {
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

    public static void genDTO(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DTO.java");
        fileWriter.write(
                "package com.tav.service.dto;\n\n" +

                        "import com.tav.service.base.db.dto.BaseFWDTOImpl;\n" +
                        "import com.tav.service.bo." + tableInfo.tableName + "BO;\n" +
                        "import com.vividsolutions.jts.geom.Geometry;\n" +
                        "import com.vividsolutions.jts.geom.Point;\n" +
                        "import java.util.Date;\n" +
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
}
