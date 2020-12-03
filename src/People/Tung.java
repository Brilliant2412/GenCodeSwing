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
import model.ColumnProperty;
import model.TableInfo;
/**
 *
 * @author hungy
 */
public class Tung {
    static void genDAO1(TableInfo tableInfo, String folder) throws IOException {

        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DAO1.java");
        fileWriter.write(
                "package com.tav.service.dao;\n" +
                        "\n" +
                        "import com.tav.service.base.db.dao.BaseFWDAOImpl;\n" +
                        "import com.tav.service.bo." + tableInfo.tableName + "BO;\n" +
                        "import com.tav.service.dto." + tableInfo.tableName + "DTO;\n" +
                        "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
                        "import com.tav.service.dto.ServiceResult;\n" +
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
                        "        SimpleDateFormat formatter = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n" +
                        "        StringBuilder sqlCommand = new StringBuilder();\n" +
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
}