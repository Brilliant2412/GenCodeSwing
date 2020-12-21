/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;
import controller.CodeGenerator;

import static controller.CodeGenerator.capitalize;
import static controller.CodeGenerator.uncapitalize;
import static controller.Service.genSELECT;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.ColumnProperty;
import model.TableInfo;
import model.TableSet;

/**
 *
 * @author hungy
 */
public class Tung {
    private static void genSubTableController(TableSet tableSet, FileWriter fileWriter) throws IOException {
        for(TableInfo subTableInfo : tableSet.subTables){
            fileWriter.append("\t\tList<CommonSubTableDTO> "+uncapitalize(subTableInfo.tableName)+"_lstSubTable = new ArrayList<>();\n" +
                    "       try {\n" +
                    "           if ("+uncapitalize(subTableInfo.tableName)+"DTO.get"+ subTableInfo.tableName+"_lstSubTable() != null && !"+uncapitalize(subTableInfo.tableName)+"DTO.get"+ subTableInfo.tableName+"_lstSubTable().isEmpty()) {\n" +
                    "               for (CommonSubTableDTO item : "+uncapitalize(subTableInfo.tableName)+"DTO.get"+ subTableInfo.tableName+"_lstSubTable()) {\n");

            for (int i = 0; i < subTableInfo.columns.size(); i++) {
                ColumnProperty columnProperty = subTableInfo.columns.get(i);
                if (columnProperty.getColType().equals("Date"))
                {
                    fileWriter.append("\t\t\t\t\t\tif (!StringUtil.isEmpty(item.get"+ subTableInfo.tableName+"_"+(columnProperty.getColName())+"())) {\n" +
                            "                                item.set"+ subTableInfo.tableName+"_"+(columnProperty.getColName())+"(DateUtil.formatDate(item.get"+ subTableInfo.tableName+"_"+(columnProperty.getColName())+"()));\n" +
                            "                            }\n");
                }
            }

            fileWriter.append("\t\tif (");
            int temp = 0;
            for (int i = 0; i < subTableInfo.columns.size(); i++) {
                ColumnProperty colProp = subTableInfo.columns.get(i);
                if (colProp.isValidate()) {
                    temp++;
                    if (colProp.getColType().equals("Long")) {
                        if (temp == 1) {
                            fileWriter.append("(item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+"() != null && item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+" > 0)\n");
                        } else {
                            fileWriter.append("\t\t\t||(item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+"() != null && item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+" > 0)\n");
                        }
                    }
                    if (colProp.getColType().equals("String") || colProp.getColType().equals("Date"))
                    {
                        if (temp == 1) {
                            fileWriter.append("!Strings.isNullOrEmpty(item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+"())");
                        } else {
                            fileWriter.append("\t\t\t||!Strings.isNullOrEmpty(item.get"+ subTableInfo.tableName+"_"+(colProp.getColName())+"())");
                        }
                    }
                    fileWriter.append("\n");
                }

            }

            fileWriter.append("\t\t\t) {\n" +
                    "                                "+uncapitalize(subTableInfo.tableName)+"_lstSubTable.add(item);\n" +
                    "                            }\n" +
                    "\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                    "+uncapitalize(subTableInfo.tableName)+"DTO.set"+ subTableInfo.tableName+"_lstSubTable("+uncapitalize(subTableInfo.tableName)+"_lstSubTable);\n" +
                    "                } catch (Exception ex) {\n" +
                    "                }\n");
        }
    }

    public static void genDAO1(TableInfo tableInfo, String folder) throws IOException {

        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DAO1.java");
        fileWriter.write(
        "package com.tav.service.dao;\n" +
            "\n" +
            "import com.tav.service.base.db.dao.BaseFWDAOImpl;\n" +
            "import com.tav.service.bo." + tableInfo.tableName + "BO;\n" +
            "import com.tav.service.dto." + tableInfo.tableName + "DTO;\n" +
            "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
            "import com.tav.service.dto.ServiceResult;\n" +
            "import com.tav.service.common.StringUtil;\n" +
            "improt java.text.ParseException;\n" +
            "import java.math.BigInteger;\n" +
            "import java.text.SimpleDateFormat;\n" +
            "import java.util.List;\n" +
            "import java.util.Date;\n" +
            "import org.hibernate.Criteria;\n" +
            "import org.hibernate.HibernateException;\n" +
            "import org.hibernate.Query;\n" +
            "import org.hibernate.Session;\n" +
            "import org.hibernate.exception.ConstraintViolationException;\n" +
            "import org.hibernate.exception.JDBCConnectionException;\n" +
            "import org.hibernate.transform.Transformers;\n" +
            "import org.hibernate.type.LongType;\n" +
            "import org.hibernate.type.StringType;\n" +
            "import org.springframework.stereotype.Repository;\n" +
            "import org.springframework.transaction.annotation.Transactional;\n" +
            "\n" +
            "@Repository(\"" + uncapitalize(tableInfo.tableName) + "DAO\")\n" +
            "public class " + tableInfo.tableName + "DAO extends BaseFWDAOImpl<" + tableInfo.tableName + "BO, Long>{\n" +
            "    \n");

        /***************************************************************************************
         *                                     getAll()
         ***************************************************************************************/
        fileWriter.append(
                "    public List<" + tableInfo.tableName + "DTO> getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {\n" +
                        "        SimpleDateFormat formatter = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n" +
                        "        StringBuilder sqlCommand = new StringBuilder();\n");
        genSELECT(fileWriter, tableInfo);
        fileWriter.append(
                "\n\t\tsqlCommand.append(\" WHERE 1=1 \");\n");
        fileWriter.append(
                "\t//String\n" +
                        " \tif (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {\n" +
                        "            sqlCommand.append(\" and (   \");\n");
        int count_str = 0;
        int count_cb = 0;
        int count_db = 0;
        int count_long = 0;
        int count_date = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("String")) {
                    count_str++;
                    if (count_str == 1) {
                        fileWriter.append("\t    sqlCommand.append(\"  LOWER(tbl." + colProp.getColName() + ") like LOWER(:stringKeyWord)   \");\n");
                    } else {
                        fileWriter.append("\t    sqlCommand.append(\"  OR LOWER(tbl." + colProp.getColName() + ") like LOWER(:stringKeyWord)   \");\n");
                    }
                }
                if (colProp.getColType().equals("Long")) {
                    if (colProp.getInputType().equals("Combobox")) {
                        count_cb++;
                    } else {
                        count_long++;
                    }
                }
                if (colProp.getColType().equals("Double")) {
                    count_db++;
                }
                if (colProp.getColType().equals("Date")) {
                    count_date++;
                }

            }
        }

        fileWriter.append("\t    sqlCommand.append(\" )   \");\n" +
                "        }\n" +
                "\n");


        f1(tableInfo, fileWriter);


        fileWriter.append(
                "\t\tsqlCommand.append(\" ORDER BY tbl." + tableInfo.columns.get(0).getColName() + " \");" +

                        "\n\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n");
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\t\t\t.addScalar(\"");
            if (colProp.getColType().equals("Date")) {
                fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
            } else {
                fileWriter.append(colProp.getColName() + "\", " + capitalize((colProp.getColType())) + "Type.INSTANCE)\n");
                if (!colProp.getFKTable().equals("")) {
                    fileWriter.append("\t\t\t.addScalar(\"");
                    fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
                }
            }
        }
        fileWriter.append(
                "\t\t\t.setResultTransformer(Transformers.aliasToBean(" + tableInfo.tableName + "DTO.class))\n" +
                        "\t\t\t.setFirstResult(offset);\n" +
                        "\t\tif (limit != null && limit != 0) {\n" +
                        "\t\t\tquery.setMaxResults(limit);\n" +
                        "\t\t}\n");

        f2(fileWriter, count_cb, count_db, count_long, count_date);

        fileWriter.append(
                "\t\treturn query.list();\n" +
                        "\t}\n\n");

        /***************************************************************************************
         *                                     GET COUNT
         ***************************************************************************************/
        fileWriter.append(
                "public Integer getCount(SearchCommonFinalDTO searchDTO) {\n" +
                        "           SimpleDateFormat formatter = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n" +
                        "           StringBuilder sqlCommand = new StringBuilder();\n" +
                        "        sqlCommand.append(\" SELECT \");\n" +
                        "        sqlCommand.append(\" COUNT(1)\");\n" +
                        "        sqlCommand.append(\" FROM  " + tableInfo.title + " tbl \");\n" +
                        "        sqlCommand.append(\" WHERE 1=1 \");\n" +
                        "\t//String\n" +
                        " \tif (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {\n" +
                        "            sqlCommand.append(\" and (   \");\n");

        int temp = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("String")) {
                    temp++;
                    if (temp == 1) {
                        fileWriter.append("\t    sqlCommand.append(\"  LOWER(tbl." + colProp.getColName() + ") like LOWER(:stringKeyWord)   \");\n");
                    } else {
                        fileWriter.append("\t    sqlCommand.append(\"  OR LOWER(tbl." + colProp.getColName() + ") like LOWER(:stringKeyWord)   \");\n");
                    }
                }
            }
        }


        f1(tableInfo, fileWriter);


        fileWriter.append(
                "        Query query = getSession().createSQLQuery(sqlCommand.toString());\n");


        f2(fileWriter, count_cb, count_db, count_long, count_date);


        fileWriter.append(
                "        return ((BigInteger) query.uniqueResult()).intValue();\n" +
                        "    }\n"
        );

        /***************************************************************************************
         *                                     GET ONE
         ***************************************************************************************/
        fileWriter.append(
                "\t//get one\n" +
                        "\tpublic " + tableInfo.tableName + "DTO getOneObjById(Long id) {\n" +
                        "\t\tStringBuilder sqlCommand = new StringBuilder();\n");
        genSELECT(fileWriter, tableInfo);
        fileWriter.append("\t\tsqlCommand.append(\" WHERE tbl." + tableInfo.columns.get(0).getColName() + " = :" + tableInfo.columns.get(0).getColName() + "\");\n");
        fileWriter.append("\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n");
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\t\t\t.addScalar(\"");
            if (colProp.getColType().equals("Date")) {
                fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
            } else {
                fileWriter.append(colProp.getColName() + "\", " + capitalize((colProp.getColType())) + "Type.INSTANCE)\n");
                if (!colProp.getFKTable().equals("")) {
                    fileWriter.append("\t\t\t.addScalar(\"");
                    fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
                }
            }
        }
        fileWriter.append(
                "\t\t\t.setResultTransformer(Transformers.aliasToBean(" + tableInfo.tableName + "DTO.class));\n" +
                        "\t\tquery.setParameter(\"" + tableInfo.columns.get(0).getColName() + "\", id);\n" +
                        "\t\t" + tableInfo.tableName + "DTO item = (" + tableInfo.tableName + "DTO) query.uniqueResult();\n" +
                        "\t\treturn item;\n" +
                        "\t}\n\n");

        /***************************************************************************************
         *                                     DELETE
         ***************************************************************************************/
        fileWriter.append(
                "\t//delete\n" +
                        "\t@Transactional\n" +
                        "\tpublic ServiceResult deleteList(List<Long> listIds) {\n" +
                        "\t\tServiceResult result = new ServiceResult();\n" +
                        "\t\tQuery q = getSession().createQuery(\"DELETE FROM " + tableInfo.tableName + "BO WHERE " + tableInfo.columns.get(0).getColName() + " IN (:listIds)\");\n" +
                        "\t\tq.setParameterList(\"listIds\", listIds);\n" +
                        "\t\ttry {\n" +
                        "\t\t\tq.executeUpdate();\n" +
                        "\t\t} catch (ConstraintViolationException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(ConstraintViolationException.class.getSimpleName());\n" +
                        "\t\t\tresult.setConstraintName(e.getConstraintName());\n" +
                        "\t\t} catch (JDBCConnectionException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(JDBCConnectionException.class.getSimpleName());\n" +
                        "\t\t\t}\n" +
                        "\t\treturn result;\n" +
                        "\t}\n\n"
        );

        /***************************************************************************************
         *                                     UPDATE
         ***************************************************************************************/
        fileWriter.append(
                "\t//update\n" +
                        "\t@Transactional\n" +
                        "\tpublic ServiceResult updateObj(" + tableInfo.tableName + "DTO dto) {\n" +
                        "\t\tServiceResult result = new ServiceResult();\n" +
                        "\t\t" + tableInfo.tableName + "BO bo = dto.toModel();\n" +
                        "\t\ttry {\n" +
                        "\t\t\tgetSession().merge(bo);\n" +
                        "\t\t} catch (ConstraintViolationException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(ConstraintViolationException.class.getSimpleName());\n" +
                        "\t\t\tresult.setConstraintName(e.getConstraintName());\n" +
                        "\t\t} catch (JDBCConnectionException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(JDBCConnectionException.class.getSimpleName());\n" +
                        "\t\t} catch (HibernateException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t}\n" +
                        "\t\treturn result;\n" +
                        "\t}\n\n"
        );

        /***************************************************************************************
         *                                     ADD
         ***************************************************************************************/
        fileWriter.append(
                "\t@Transactional\n" +
                        "\tpublic " + tableInfo.tableName + "BO addDTO(" + tableInfo.tableName + "DTO dto) {\n" +
                        "\t\tServiceResult result = new ServiceResult();\n" +
                        "\t\tSession session1 = getSession();\n" +
                        "\t\t" + tableInfo.tableName + "BO BO = new " + tableInfo.tableName + "BO();\n" +
                        "\t\ttry {\n" +
                        "\t\t\tBO = (" + tableInfo.tableName + "BO) session1.merge(dto.toModel());\n" +
                        "\t\t} catch (JDBCConnectionException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(JDBCConnectionException.class.getSimpleName());\n" +
                        "\t\t} catch (ConstraintViolationException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t\tresult.setErrorType(ConstraintViolationException.class.getSimpleName());\n" +
                        "\t\t\tresult.setConstraintName(e.getConstraintName());\n" +
                        "\t\t} catch (HibernateException e) {\n" +
                        "\t\t\tlog.error(e);\n" +
                        "\t\t\tresult.setError(e.getMessage());\n" +
                        "\t\t}\n" +
                        "\t\treturn BO;\n" +
                        "\t}\n" +
                        "}"
        );
        fileWriter.close();
    }

    public static void f1(TableInfo tableInfo, FileWriter fileWriter) throws IOException {
        int t1 = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("Long")) {
                    if (colProp.getInputType().equals("Combobox")) {
                        t1++;
                        fileWriter.append("\tif (searchDTO.getListLong" + t1 + "() != null && !searchDTO.getListLong" + t1 + "().isEmpty()) {\n" +
                                "            sqlCommand.append(\" and tbl." + colProp.getColName() + " in (:listLong" + t1 + ") \");\n" +
                                "        }\n\n");
                    }
                }

            }

        }

        int t2 = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("Long")) {
                    if (!colProp.getInputType().equals("Combobox")) {
                        t2++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(searchDTO.getLong" + t2 + "())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl." + colProp.getColName() + " >= (:long" + (t2++) + ") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(searchDTO.getLong" + t2 + "())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl." + colProp.getColName() + " <= (:long" + t2 + ") \");\n" +
                                "\t}\n");
                    }
                }
            }
        }

        int t3 = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("Double")) {
                    if (!colProp.getInputType().equals("Combobox")) {
                        t3++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(searchDTO.getDouble" + t3 + "())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl." + colProp.getColName() + " >= (:double" + (t3++) + ") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(searchDTO.getDouble" + t3 + "())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl." + colProp.getColName() + " <= (:double" + t3 + ") \");\n" +
                                "\t}\n");
                    }
                }

            }

        }

        int t4 = 0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch()) {
                if (colProp.getColType().equals("Date")) {
                    t4++;
                    fileWriter.append("\tif (  (searchDTO.getString" + t4 + "() != null && !searchDTO.getString" + t4 + "().isEmpty())   &&   (searchDTO.getString" + (t4 + 1) + "() != null && !searchDTO.getString" + (t4 + 1) + "().isEmpty())  ) {\n" +
                            "            sqlCommand.append(\"  and ( tbl." + colProp.getColName() + " between (:string" + t4 + ") and (:string" + (t4 + 1) + ")    ) \");\n" +
                            "        }\n");

                }

            }

        }
    }

    public static void f2(FileWriter fileWriter, int count_cb, int count_db, int count_long, int count_date) throws IOException {
        //String
        fileWriter.append("\tif (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {\n" +
                "\t\tquery.setParameter(\"stringKeyWord\", \"%\" + searchDTO.getStringKeyWord() + \"%\");\n" +
                "\t}\n");
        //Combobox
        for (int i = 1; i < count_cb + 1; i++) {

            fileWriter.append("\tif (searchDTO.getListLong" + i + "() != null && !searchDTO.getListLong" + i + "().isEmpty()) {\n" +
                    "            query.setParameterList(\"listLong" + i + "\", searchDTO.getListLong" + i + "());\n" +
                    "        }\n");

        }
        //double
        for (int i = 1; i < count_db * 2; i += 2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(searchDTO.getDouble" + i + "())) {\n" +
                    "\t\tquery.setParameter(\"double" + i + "\", searchDTO.getDouble" + i + "());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(searchDTO.getDouble" + (i + 1) + "())) {\n" +
                    "\t\tquery.setParameter(\"double" + (i + 1) + "\", searchDTO.getDouble" + (i + 1) + "());\n" +
                    "\t}\n");

        }
        //Long
        for (int i = 1; i < count_long * 2; i += 2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(searchDTO.getLong" + i + "())) {\n" +
                    "\t\tquery.setParameter(\"long" + i + "\", searchDTO.getLong" + i + "());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(searchDTO.getLong" + (i + 1) + "())) {\n" +
                    "\t\tquery.setParameter(\"long" + (i + 1) + "\", searchDTO.getLong" + (i + 1) + "());\n" +
                    "\t}\n");

        }

        //date
        for (int i = 1; i < count_date * 2; i += 2) {

            fileWriter.append("\tif (  (searchDTO.getString" + i + "() != null && !searchDTO.getString" + i + "().isEmpty())   &&   (searchDTO.getString" + (i + 1) + "() != null && !searchDTO.getString" + (i + 1) + "().isEmpty())  ) {\n" +
                    "            try {\n" +
                    "                query.setParameter(\"string" + i + "\", formatter.parse(searchDTO.getString" + i + "() + \" 00:00:00\"));\n" +
                    "                query.setParameter(\"string" + (i + 1) + "\", formatter.parse(searchDTO.getString" + (i + 1) + "() + \" 23:59:59\"));\n" +
                    "            } catch (ParseException ex) {\n" +
                    "            }\n" +
                    "        }\n");

        }
    }

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


        int k = tableInfo.countSearch;
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
            for (; i < tableInfo.columns.size(); i++) {
                if (tableInfo.columns.get(i).isSearch() == true) {
                    String res = resSearch(tableInfo.columns.get(i).getColName(),
                            tableInfo.columns.get(i).getColDescription(),
                            tableInfo.columns.get(i).getColType(),
                            tableInfo.columns.get(i).getInputType()
                    );
                    fileWriter.append(res);
                }
            }
            fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
"                                <div class=\"col-lg-2 selectContainer\">\n" +
"                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
"                                </div>\n");
            fileWriter.append("\t\t\t\t\t\t\t\t</div>\n\n");
        }else {
            fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
            fileWriter.append("\t\t\t\t\t\t\t\t\t<label class=\"col-lg-1 control-label\" style=\"padding:5px 4px\">Từ khóa</label>\n" +
                    "                                \t<div class=\"col-lg-2 selectContainer\">\n" +
                    "                                    \t<input type=\"text\" class=\"form-control\" placeholder=\"Nhập từ khóa\" id=\"keySearch\">\n" +
                    "                                \t</div>\n");
            for (; i < tableInfo.columns.size(); i++) {
                if (tableInfo.columns.get(i).isSearch() == true) {
                    countSearch++;
                    String res = resSearch(tableInfo.columns.get(i).getColName(),
                            tableInfo.columns.get(i).getColDescription(),
                            tableInfo.columns.get(i).getColType(),
                            tableInfo.columns.get(i).getInputType()
                    );
                    fileWriter.append(res);
                    if (countSearch == 3) {
                                    fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
"                                <div class=\"col-lg-2 selectContainer\">\n" +
"                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
"                                </div>\n");
                        fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
                        break;
                    }
                }
            }
            
            k -= 3;
            countSearch -= 3;
            r = k / 4;
            q = k % 4;
            i++;
            if (r == 0) {
                fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                for (; i < tableInfo.columns.size(); i++) {
                    if (tableInfo.columns.get(i).isSearch() == true) {
                        String res = resSearch(tableInfo.columns.get(i).getColName(),
                                tableInfo.columns.get(i).getColDescription(),
                                tableInfo.columns.get(i).getColType(),
                                tableInfo.columns.get(i).getInputType()
                        );
                        fileWriter.append(res);
                    }
                }
                            fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
"                                <div class=\"col-lg-2 selectContainer\">\n" +
"                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
"                                </div>\n");
                fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
            } else {
                for (int dem = 0; dem < r; dem++) {
                    fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                    for (; i < tableInfo.columns.size(); i++) {
                        if (tableInfo.columns.get(i).isSearch() == true) {
                            countSearch++;
                            String res = resSearch(tableInfo.columns.get(i).getColName(),
                                    tableInfo.columns.get(i).getColDescription(),
                                    tableInfo.columns.get(i).getColType(),
                                    tableInfo.columns.get(i).getInputType()
                            );
                            fileWriter.append(res);
                            if (countSearch % 4 == 3 && countSearch <= k) {
                                            fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
"                                <div class=\"col-lg-2 selectContainer\">\n" +
"                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
"                                </div>\n");
                                fileWriter.append("\t\t\t\t\t\t\t\t/div>\n");
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (q != 0) {
                    fileWriter.append("\t\t\t\t\t\t\t\t<div class=\"form-group has-feedback\">\n");
                    for (; i < tableInfo.columns.size(); i++) {
                        if (tableInfo.columns.get(i).isSearch() == true) {
                            String res = resSearch(tableInfo.columns.get(i).getColName(),
                                    tableInfo.columns.get(i).getColDescription(),
                                    tableInfo.columns.get(i).getColType(),
                                    tableInfo.columns.get(i).getInputType()
                            );
                            fileWriter.append(res);
                        }
                    }
                                fileWriter.append("\t\t\t\t\t\t\t\t<label class=\"col-md-1 control-label\" ></label>\n" +
"                                <div class=\"col-lg-2 selectContainer\">\n" +
"                                    <button id=\"btnSearch\" class=\"btn btn-success\" type=\"button\" style=\"width: 100%;\">Tìm kiếm</button>\n" +
"                                </div>\n");
                    fileWriter.append("\t\t\t\t\t\t\t\t</div>\n");
                }
            }
        }
        fileWriter.append("                        </fieldset>\n" +
                "                        \n" +
                "                    </form>\n" +
                "                </div>\n" +
                "                <br>\n" +
                "            </div>\n" +
                "        </div>\n" +
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

    public static void genJsSearch(TableInfo tableInfo, String folder) throws  IOException{
        FileWriter fileWriter = new FileWriter(folder+"\\jsSearch.js");
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
        fileWriter.append(
                "doSearch = function () {\n" +
                        "    vt_datagrid.loadPageAgainRes(\"#dataTblDocumentType\", \"getall" + tableInfo.tableName.toLowerCase() + ".json\");\n" +
                        "    vt_sys.showBody();\n" +
                        "    vt_loading.hideIconLoading();\n" +
                        "    return false;\n" +
                        "};\n\n"
        );
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
                "        vt_form.reset($('#" + uncapitalize(tableInfo.tableName) + "Form'));\n" +
                "        $(\"#gid\").val(\"\"); // reset form\n" +
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
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+"ST);\n");
            }
            else if (columnProperty.getInputType().equals("Combobox"))
            {

                fileWriter.append("\t\t\t\t\tvt_combobox.buildCombobox(\"cb"+columnProperty.getColName()+"\", \""+columnProperty.getComboboxBuildPath()+"\", data."+columnProperty.getColName()+", \""+columnProperty.getComboboxName()+"\", \""+columnProperty.getComboboxValue()+"\", \"- Chọn "+columnProperty.getColDescription()+" -\", 0);\n");
            }
            else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+");\n");

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
        fileWriter.append("\n\teditView: function(id) {\n" +
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
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+"ST);\n");
            }
            else if (columnProperty.getInputType().equals("Combobox"))
            {
                //ok gen lai cho anh file js cai file excel cua a la file nao a
                fileWriter.append("\t\t\t\t\tvt_combobox.buildCombobox(\"cb"+columnProperty.getColName()+"\", \""+columnProperty.getComboboxBuildPath()+"\", data."+columnProperty.getColName()+", \""+columnProperty.getComboboxName()+"\", \""+columnProperty.getComboboxValue()+"\", \"- Chọn "+columnProperty.getColDescription()+" -\", 0);\n");
            }
            else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+");\n");

        }
        fileWriter.append("\t\t\t\t}\n\t\t\t});\n\t\t}\n\t},\n");
        /*********************************************************************************************
         *                                 Viewjsp
         *********************************************************************************************/
        fileWriter.append("\n\tview: function(gid) {\n" +
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
                fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+"ST);\n");
            }
            else if (columnProperty.getInputType().equals("Combobox"))
            {
                fileWriter.append("\t\t\t\t\t$(\"#cb"+columnProperty.getColName()+"combobox"+"\").val(data."+columnProperty.getColName()+"ST"+");\n");
            }
            else fileWriter.append("\t\t\t\t\t$(\"#"+columnProperty.getColName()+"\").val(data."+columnProperty.getColName()+");\n");

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
    public static String checkDang_sub(String tenTruong, String moTa, String kieuDL, String kieuNhap,String tenbang){
        String res = "";
        if (kieuDL.equals("String")){
            res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                    "\t\t\t\t<div class=\"col-lg-2\">\n"+
                    "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+uncapitalize(tenbang)+tenTruong+"\" type=\"text\" class=\"form-control\"/>\n" +
                    "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                    "\t\t\t\t</div>\n";
        }else if (kieuDL.equals("Long") || kieuDL.equals("Double")){
            if (kieuNhap.equals("Combobox")){
                res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-2\">\n" +
                        "\t\t\t\t\t<div id=\"cb"+tenTruong+"\"></div> \n" +
                        "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+uncapitalize(tenbang)+tenTruong+"\" class=\"text_hidden\"  />\n" +
                        "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                        "\t\t\t\t</div>\n";
            }else{
                res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-2\">\n" +
                        "\t\t\t\t\t<input name=\""+tenTruong+"\" id=\""+uncapitalize(tenbang)+tenTruong+"\" type=\"number\" class=\"form-control\"/>\n" +
                        "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>                           \n" +
                        "\t\t\t\t</div>\n";
            }
        }else if (kieuDL.equals("Date")){
            res = "\t\t\t\t<label class=\"col-lg-1 control-label  lb_input\">"+moTa+"</label>\n" +
                    "\t\t\t\t<div class=\"col-md-2\" input-group>\n"+
                    "\t\t\t\t<input class=\"dateCalendar\" placeholder=\"Bắt đầu\" name=\""+tenTruong+"\" id=\""+uncapitalize(tenbang)+tenTruong+"\" type=\"text\"/>\n" +
                    "\t\t\t\t\t<span id=\""+tenTruong+"_error\" class=\"note note-error\"></span>\n" +
                    "\t\t\t\t</div>\n";
        }
        return res;
    }

    public static void gensubTableJSP(TableInfo tableInfo, String folder) throws  IOException
    {
        FileWriter fileWriter = new FileWriter(folder + "\\subTable.jsp");
        fileWriter.write("<%@page contentType=\"text/html\" pageEncoding=\"UTF-8\"%>\n" +
                "<%@ taglib prefix=\"spring\" uri=\"http://www.springframework.org/tags\" %>\n" +
                "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>\n" +
                "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\" %>\n" +
                "<%@ taglib uri=\"http://www.springframework.org/tags/form\" prefix=\"form\"%>  \n" +
                "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>\n" +
                "<script src=\"${pageContext.request.contextPath}/share/core/js/"+uncapitalize(tableInfo.tableName)+"SubTable.js\"/>\n" +
                "<div class=\"ui-widget-overlay1 ui-front custom-overlay\" style=\"z-index: 1 !important\"></div>\n" +
                "<div id=\"dialog-formAddTopicMember\" style=\"z-index: 9998 !important\"> \n" +
                "    <form:form id=\"subTableForm\" modelAttribute=\"subTableForm\" class=\"form-horizontal\">\n" +
                "        <input type=\"hidden\" id=\""+uncapitalize(tableInfo.tableName)+"isedit1\" name=\"isedit1\" value=\"0\"/>\n" +
                "        <input type=\"hidden\" id=\""+uncapitalize(tableInfo.tableName)+"isDeleteFile_subdoc\" name=\"\" value=\"0\"/>\n" +
                "        <fieldset>\n" +
                "            <br/>\n" +
                "            <legend class=\"fs-legend-head\" style=\"margin-bottom: 0px;\">\n" +
                "                <span class=\"iconFS\"></span>\n" +
                "                <span class=\"titleFS\" style=\"color: #047fcd !important;\"><b>Bảng con</b></span>\n" +
                "                <div class=\"col-md-5\" style=\"float:right;font-size: 12px;\">\n" +
                "                    <label class=\"col-md-2 control-label\">Người tạo</label>\n" +
                "                    <div class=\"col-md-4\">\n" +
                "                        <input class=\"form-control\" placeholder=\"\"  id=\"user_create1_subdoc\" type=\"text\" readonly=\"true\" />\n" +
                "                        <span id=\"documentary_number_error\" class=\"note note-error\"></span>\n" +
                "                    </div>\n" +
                "                    <div class=\"col-md-4\">\n" +
                "                        <input class=\"form-control\" placeholder=\"\"  id=\"create_time111_subdoc\" type=\"text\" readonly=\"true\" />\n" +
                "                        <span id=\"documentary_number_error\" class=\"note note-error\"></span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </legend>\n");
        ArrayList<ColumnProperty> columns_except_file = new ArrayList<>();
        for (int i = 0;i<tableInfo.columns.size();i++){
            if (tableInfo.columns.get(i).getInputType().equals("file")){
                continue;
            }else{
                columns_except_file.add(tableInfo.columns.get(i));
            }
        }
        int k = columns_except_file.size()-1;
        int r = k/4;
        int q = k%4;
        for (int i = 0;i<r;i++){
            fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
            for (int j =0;j<=3;j++){
                String res = checkDang_sub(columns_except_file.get(4*i+j+1).getColName(),
                        columns_except_file.get(4*i+j+1).getColDescription(),
                        columns_except_file.get(4*i+j+1).getColType(),
                        columns_except_file.get(4*i+j+1).getInputType(),
                        tableInfo.tableName
                );
                fileWriter.append(res);
            }
            fileWriter.append("\t\t\t</div>\n\n");
        }
        if (q != 0){
            fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n");
            for (int i = r*4+1;i<=k;i++){
                String res = checkDang_sub(columns_except_file.get(i).getColName(),
                        columns_except_file.get(i).getColDescription(),
                        columns_except_file.get(i).getColType(),
                        columns_except_file.get(i).getInputType().toLowerCase(),
                        tableInfo.tableName
                );
                fileWriter.append(res);
            }
            fileWriter.append("\t\t\t</div>\n\n");
        }
        int c_file = 0;
        for (int i = 0; i <tableInfo.columns.size(); i++) {
            ColumnProperty columnProperty = tableInfo.columns.get(i);
            if(columnProperty.getInputType().toLowerCase().equals("file"))
            {
                if (c_file==0) {
                    fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n" +
                            "                <label class=\"col-lg-1 control-label  lb_input\">" + columnProperty.getColDescription() + "</label>\n" +
                            "                <div class=\"col-md-11\" id=\"fileTopicFilesTmpSubTable\">\n" +
                            "                    <input class=\"form-control\" placeholder=\"\" name=\"filestTmpSubTable\" id=\"filestTmpSubTable\" type=\"file\"/>\n" +
                            "                    <span id=\"filestTmpSubTable_error\" class=\"note note-error\"></span>\n" +
                            "                </div>\n" +
                            "            </div>\n");

                }
                else
                {
                    fileWriter.append("\t\t\t<div class=\"form-group-add row\">\n" +
                            "                <label class=\"col-lg-1 control-label  lb_input\">" + columnProperty.getColDescription() + "</label>\n" +
                            "                <div class=\"col-md-11\" id=\"fileTopicFilesTmpSubTable"+c_file+"\">\n" +
                            "                    <input class=\"form-control\" placeholder=\"\" name=\"filestTmpSubTable"+c_file+"\" id=\"filestTmpSubTable"+c_file+"\" type=\"file\"/>\n" +
                            "                    <span id=\"filestTmpSubTable"+c_file+"_error\" class=\"note note-error\"></span>\n" +
                            "                </div>\n" +
                            "            </div>\n");
                }
                c_file++;
            }

        }
        fileWriter.append("\t\t</fieldset>\n" +
                "    </form:form>\n" +
                "</div>\n" +
                "<script type=\"text/javascript\">\n" +
                "    $(\"#dialog-formAddTopicMember\").dialog({\n" +
                "        width: isMobile.any() ? $(window).width() : ($(window).width() / 20 * 19),\n" +
                "        height: $(window).height() / 5 * 5 - 80,\n" +
                "        autoOpen: false,\n" +
                "        modal: true,\n" +
                "        position: [($(window).width() / 80 * 2.5), 20],\n" +
                "        close: function () {\n" +
                "            $('.ui-widget-overlay1').removeClass('ui-widget-overlay');\n" +
                "            $('.ui-widget-overlay1').removeClass('custom-overlay');\n" +
                "            $('.ui-widget-overlay1').css('z-index', \"1 !important\");\n" +
                "        },\n" +
                "        open: function () {\n" +
                "            $('.ui-widget-overlay1').addClass('ui-widget-overlay');\n" +
                "            $('.ui-widget-overlay1').css('z-index', \"99999 !important\");\n" +
                "            $(\"#tabs\").tabs({\n" +
                "                active: 0\n" +
                "            });\n" +
                "        },\n" +
                "        buttons: [{\n" +
                "                html: \"<fmt:message key='button.close' />\",\n" +
                "                \"class\": \"btn btn-default\",\n" +
                "                click: function () {\n" +
                "                    $(this).dialog('close');\n" +
                "                }\n" +
                "            }, {\n" +
                "                html: \"<fmt:message key='button.update' />\",\n" +
                "                \"class\": \"btn btn-primary\",\n" +
                "                \"id\": \"btnAddRole1\",\n" +
                "                click: function () {\n" +
                "                    var item = $('#"+uncapitalize(tableInfo.tableName)+"isedit1').val();\n" +
                "                    if (item === '0') {\n" +
                "                        "+uncapitalize(tableInfo.tableName)+"saveTopicMember();\n" +
                "                    } else {\n" +
                "                        "+uncapitalize(tableInfo.tableName)+"editTopicMember();\n" +
                "                    }\n" +
                "                }\n" +
                "            }]\n" +
                "    });\n" +
                "</script>\n");
        fileWriter.close();
    }
    public static void genController(TableSet tableSet, String folder) throws IOException {
        TableInfo tableInfo = tableSet.tableInfo;
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
                "import com.tav.web.dto.CommonSubTableDTO;\n" +
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        genSubTableController(tableSet, fileWriter);




        ////////////////////////////////////////////////



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



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        genSubTableController(tableSet, fileWriter);
        ////////////////////////////////////////////////




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
                        "\n" +
                        "}\n");

        fileWriter.close();
    }

    public static void genBusinessImpl_SUB(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "BusinessImplSUB.java");
        fileWriter.write("package com.tav.service.business;\n" +
                "\n" +
                "import com.tav.service.base.db.business.BaseFWBusinessImpl;\n" +
                "import com.tav.service.bo."+ tableInfo.tableName +"BO;\n" +
                "import com.tav.service.common.Constants;\n" +
                "import com.tav.service.dao."+ tableInfo.tableName +"DAO;\n" +
                "import com.tav.service.dao.ObjectReationDAO;\n" +
                "import com.tav.service.dto."+ tableInfo.tableName + "DTO;\n" +
                "import com.tav.service.dto.ObjectCommonSearchDTO;\n" +
                "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
                "import com.tav.service.dto.ObjectSearchDTO;\n" +
                "import com.tav.service.dto.ServiceResult;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Date;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.context.annotation.Scope;\n" +
                "import org.springframework.context.annotation.ScopedProxyMode;\n" +
                "import org.springframework.stereotype.Service;\n");
        fileWriter.append("\n@Service(\""+uncapitalize(tableInfo.tableName)+"BusinessImpl\")"+
                "\n@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)\n");
        fileWriter.append("public class "+tableInfo.tableName+"BusinessImpl extends\n"+
                "\t\tBaseFWBusinessImpl<"+tableInfo.tableName+"DAO, "+ tableInfo.tableName+"DTO, "+tableInfo.tableName+"BO> implements "+tableInfo.tableName+"Business {\n"
        );
        fileWriter.append("\n\t@Autowired\n"+
                "\tprivate " +tableInfo.tableName+"DAO " + uncapitalize(tableInfo.tableName) +"DAO;\n"
        );

        fileWriter.append("\t@Autowired\n" +
                "    private CommonSubTableDAO commonSubTableDAO;\n");

        fileWriter.append("\n\t@Override\n"+
                "\tpublic "+ tableInfo.tableName+"DAO" + " gettDAO() { return "+ uncapitalize(tableInfo.tableName) +"DAO; }\n"
        );
        fileWriter.append("\n\tpublic List<"+ tableInfo.tableName+"DTO>" + " getAll(SearchCommonFinalDTO searchDTOTmp, Integer offset, Integer limit) {\n"+
                "\t\tList<"+ tableInfo.tableName+"DTO>" + " lstDTO = "+ uncapitalize(tableInfo.tableName) +"DAO"+".getAll(searchDTOTmp, offset, limit);\n" +
                "\t\treturn lstDTO;\n\t}\n"
        );
        fileWriter.append("\n\tpublic Integer getCount(SearchCommonFinalDTO searchDTO) { return "+
                uncapitalize(tableInfo.tableName)+"DAO.getCount(searchDTO); }\n"
        );
        String gid = tableInfo.columns.get(0).getColName();
        String gidType = tableInfo.columns.get(0).getColType();
        fileWriter.append("\n\t//GET ONE\n\tpublic "+ tableInfo.tableName+"DTO getOneObjById("+gidType +" " +gid+") {\n"+
                "\t\t"+ tableInfo.tableName+"DTO dto = "+ uncapitalize(tableInfo.tableName) +"DAO"+".getOneObjById("+gid+");\n"+
                "\t\treturn dto;\n\t}\n"
        );
        fileWriter.append("\n\t//add\n"+
                "\tpublic ServiceResult addDTO("+ tableInfo.tableName+"DTO "+ uncapitalize(tableInfo.tableName) +"DTO) {\n"+
                "\t\t"+ tableInfo.tableName+"BO bo = "+ uncapitalize(tableInfo.tableName)+"DAO"+".addDTO("+ uncapitalize(tableInfo.tableName)+"DTO);\n"+
                "\t\tServiceResult serviceResult = new ServiceResult();\n"+
                "\t\tserviceResult.setId(bo.get"+capitalize(gid).trim()+"());\n");

        fileWriter.append("\t\tList<CommonSubTableDTO> "+uncapitalize(tableInfo.tableName)+"_lstSubTable = "+uncapitalize(tableInfo.tableName)+"DTO.get"+tableInfo.tableName+"lstSubTable();\n" +
                "        if ("+uncapitalize(tableInfo.tableName)+"_lstSubTable != null && !"+uncapitalize(tableInfo.tableName)+"_lstSubTable .isEmpty()) {\n" +
                "            "+uncapitalize(tableInfo.tableName)+"_lstSubTable .stream().forEach((item) -> {\n" +
                "                item.setMain_id("+uncapitalize(tableInfo.tableName)+"BO.getGid());\n" +
                "                item.setTable_name(\""+tableInfo.tableName+"\");\n" +
                "                item.setField_name(\""+tableInfo.tableName+"SubTable\");\n" +
                "                commonSubTableDAO.addDTO(item);\n"  +
                "            });\n" +
                "        }\n"
        );

        fileWriter.append(
                "\t\treturn serviceResult;\n"+
                "\t}\n"
        );
        fileWriter.append("\n\t//update\n"+
                "\tpublic ServiceResult updateObj("+ tableInfo.tableName+"DTO "+ uncapitalize(tableInfo.tableName)+"DTO) {\n"+
                "\t\tServiceResult result;\n"+
                "\t\t"+ tableInfo.tableName+"BO bo = "+ uncapitalize(tableInfo.tableName)+"DAO"+".addDTO("+ uncapitalize(tableInfo.tableName)+"DTO);\n"+
                "\t\tresult = new ServiceResult();\n" );
        fileWriter.append("\t\tcommonSubTableDAO.deleteListObjByTableName(searchDTO);\n");

        fileWriter.append("\t\tList<CommonSubTableDTO> "+uncapitalize(tableInfo.tableName)+"_lstSubTable = "+uncapitalize(tableInfo.tableName)+"DTO.get"+tableInfo.tableName+"lstSubTable();\n" +
                "        if ("+uncapitalize(tableInfo.tableName)+"_lstSubTable != null && !"+uncapitalize(tableInfo.tableName)+"_lstSubTable .isEmpty()) {\n" +
                "            "+uncapitalize(tableInfo.tableName)+"_lstSubTable .stream().forEach((item) -> {\n" +
                "                item.setMain_id("+uncapitalize(tableInfo.tableName)+"BO.getGid());\n" +
                "                item.setTable_name(\""+tableInfo.tableName+"\");\n" +
                "                item.setField_name(\""+tableInfo.tableName+"SubTable\");\n" +
                "                commonSubTableDAO.addDTO(item);\n"  +
                "            });\n" +
                "        }\n"
        );

        fileWriter.append(
                "\t\treturn result;\n"+
                "\t}\n"
        );
        fileWriter.append("\n\t//delete\n"+
                "\tpublic ServiceResult deleteList(ObjectCommonSearchDTO searchDTO) {\n"+
                "\t\tServiceResult result = "+ uncapitalize(tableInfo.tableName) +"DAO"+".deleteList(searchDTO.getLstFirst());\n"+
                "\t\treturn result;\n"+
                "\t}\n"
        );
        fileWriter.append("\n}");
        fileWriter.close();
    }

}