package controller;

import model.ColumnProperty;
import model.TableInfo;
import static controller.CodeGenerator.capitalize;
import static controller.CodeGenerator.uncapitalize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Service {
    static void genSELECT(FileWriter fileWriter, TableInfo tableInfo) throws IOException {
        fileWriter.append("\t\tsqlCommand.append(\" SELECT \");\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\t\tsqlCommand.append(\"");
            if(colProp.getColType().equals("Date")){
                fileWriter.append("to_char(tbl." + colProp.getColName() + ", 'DD/MM/YYYY') as " + colProp.getColName() + "ST");
                if(i != tableInfo.columns.size() - 1){
                    fileWriter.append(",");
                }
                fileWriter.append(" \");\n");
            }
            else {
                fileWriter.append("tbl." + colProp.getColName() + " as " + colProp.getColName());
                if(i != tableInfo.columns.size() - 1){
                    fileWriter.append(",");
                }
                fileWriter.append(" \");\n");
                if(!colProp.getFKTable().equals("")){
                    fileWriter.append("\t\tsqlCommand.append(\"");
                    fileWriter.append(colProp.getJoinTableName() + "." + colProp.getSelectField() + " as " + colProp.getColName() + "ST");
                    if(i != tableInfo.columns.size() - 1){
                        fileWriter.append(",");
                    }
                    fileWriter.append(" \");\n");
                }
            }
        }
        fileWriter.append("\n\t\tsqlCommand.append(\" FROM " + tableInfo.title  + " tbl \");\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            if(!colProp.getFKTable().equals("")){
                fileWriter.append("\t\tsqlCommand.append(\" left join " +
                        colProp.getFKTable() + " " + colProp.getJoinTableName() + " on " + colProp.getJoinTableName() + "." +
                        colProp.getJoinField() + " = tbl." + colProp.getColName());
                if(!colProp.getJoinConstraint().equals("")){
                    fileWriter.append(" AND " + colProp.getJoinTableName() + "." + colProp.getJoinConstraint());
                }
                fileWriter.append("\");\n");
            }
        }
    }

    static void genBO(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "BO.java");
        fileWriter.write(
                "package com.tav.service.bo;\n\n" +

                        "import com.tav.service.base.db.dto.BaseFWDTOImpl;\n" +
                        "import com.tav.service.base.db.model.BaseFWModelImpl;\n" +
                        "import com.tav.service.dto." + tableInfo.tableName + "DTO;\n" +
                        "import com.vividsolutions.jts.geom.Geometry;\n" +
                        "import com.vividsolutions.jts.geom.Point;\n" +
                        "import java.util.Date;" +
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
            fileWriter.append("\tprivate " + tableInfo.columns.get(i).getColType() + " " + tableInfo.columns.get(i).getColName() + ";\t\t//" + tableInfo.columns.get(i).getColDescription() + "\n");
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
            fileWriter.append(
                    "\t@Column(name = \"" + tableInfo.columns.get(i).getColName() + "\", length = " + tableInfo.columns.get(i).getLengthFormat() + ")\n" +
                            "\tpublic " + tableInfo.columns.get(i).getColType() + " get" + capitalize(tableInfo.columns.get(i).getColName()) + "(){\n" +
                            "\t\treturn " + tableInfo.columns.get(i).getColName() + ";\n" +
                            "\t}\n\n" +
                            "\tpublic void set" + capitalize(tableInfo.columns.get(i).getColName()) + "(" + tableInfo.columns.get(i).getColType() + " " + tableInfo.columns.get(i).getColName() + "){\n" +
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

    static void genDTO(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DTO.java");
        fileWriter.write(
                "package com.tav.service.dto;\n\n" +

                        "import com.tav.service.base.db.dto.BaseFWDTOImpl;\n" +
                        "import com.tav.service.bo." + tableInfo.tableName + "BO;\n" +
                        "import com.vividsolutions.jts.geom.Geometry;\n" +
                        "import com.vividsolutions.jts.geom.Point;\n" +
                        "import java.util.Date;" +
                        "import javax.xml.bind.annotation.XmlRootElement;\n\n" +

                        "@XmlRootElement(name = \"" + tableInfo.tableName + "DTO\")\n" +
                        "public class " + tableInfo.tableName + "DTO extends BaseFWDTOImpl<" + tableInfo.tableName + "BO> {\n"
        );
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\tprivate " + colProp.getColType() + " " + colProp.getColName() + ";\t\t//" + colProp.getColDescription() + "\n");
            if(!colProp.getFKTable().equals("") || colProp.getColType().equals("Date")){
                fileWriter.append("\tprivate String " + colProp.getColName() + "ST;\n");
            }
        }
        fileWriter.append("\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append(
                    "\tpublic " + colProp.getColType() + " get" + capitalize(colProp.getColName()) + "(){\n" +
                            "\t\treturn " + colProp.getColName() + ";\n" +
                            "\t}\n\n" +

                            "\tpublic void set" + capitalize(colProp.getColName()) + "(" + colProp.getColType() + " " + colProp.getColName() + "){\n" +
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

    static void genDAO(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "DAO.java");
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
                        "import java.util.Date;" +
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
                "\n\t\tsqlCommand.append(\" WHERE 1=1 \");\n" +
                        "\t\tsqlCommand.append(\" ORDER BY tbl." + tableInfo.columns.get(0).getColName() + " \");" +

                        "\n\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\t\t\t.addScalar(\"");
            if(colProp.getColType().equals("Date")){
                fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
            }
            else{
                fileWriter.append(colProp.getColName() + "\", " + capitalize((colProp.getColType())) + "Type.INSTANCE)\n");
                if(!colProp.getFKTable().equals("")){
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
                        "\t\t}\n" +
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
                        "        Query query = getSession().createSQLQuery(sqlCommand.toString());\n" +
                        "        return ((BigInteger) query.uniqueResult()).intValue();\n" +
                        "    }"
        );

        /***************************************************************************************
         *                                     GET ONE
         ***************************************************************************************/
        fileWriter.append(
                "\t//get one\n" +
                        "\tpublic " + tableInfo.tableName + "DTO getOneObjById(Long id) {\n" +
                        "\t\tStringBuilder sqlCommand = new StringBuilder();\n");
        genSELECT(fileWriter, tableInfo);
        fileWriter.append("\t\tsqlCommand.append(\" WHERE tbl." + tableInfo.columns.get(0).getColName() +" = :" + tableInfo.columns.get(0).getColName() +"\");\n");
        fileWriter.append("\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n");
        for(int i = 0; i < tableInfo.columns.size(); i++){
            ColumnProperty colProp = tableInfo.columns.get(i);
            fileWriter.append("\t\t\t.addScalar(\"");
            if(colProp.getColType().equals("Date")){
                fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
            }
            else{
                fileWriter.append(colProp.getColName() + "\", " + capitalize((colProp.getColType())) + "Type.INSTANCE)\n");
                if(!colProp.getFKTable().equals("")){
                    fileWriter.append("\t\t\t.addScalar(\"");
                    fileWriter.append(colProp.getColName() + "ST\", StringType.INSTANCE)\n");
                }
            }
        }
        fileWriter.append(
                "\t\t\t.setResultTransformer(Transformers.aliasToBean(" + tableInfo.tableName + "DTO.class));\n" +
                        "\t\tquery.setParameter(\"" + tableInfo.columns.get(0).getColName()  + "\", id);\n" +
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

    static void genBusiness(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "Business.java");
        fileWriter.write("package com.tav.service.business;\n" +
                "\n" +
                "public interface "+capitalize(tableInfo.tableName)+"Business {\n" +
                "    \n" +
                "}");
        fileWriter.close();
    }

    static void genBusinessImpl(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName + "BusinessImpl.java");
        fileWriter.write("package com.tav.service.business;\n" +
                "\n" +
                "import com.tav.service.base.db.business.BaseFWBusinessImpl;\n" +
                "import com.tav.service.bo."+ tableInfo.tableName +"BO;\n" +
                "import com.tav.service.common.Constants;\n" +
                "import com.tav.service.dao."+ tableInfo.tableName +"DAO;\n" +
                "import com.tav.service.dao.ObjectReationDAO;\n" +
                "import com.tav.service.dto."+ tableInfo.tableName + "DTO;\n" +
                "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
                "import com.tav.service.dto.ObjectSearchDTO;\n" +
                "import com.tav.service.dto.ServiceResult;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Date;" +
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
                "\t\tserviceResult.setId(bo.get"+capitalize(gid).trim()+"());\n"+
                "\t\treturn serviceResult;\n"+
                "\t}\n"
        );
        fileWriter.append("\n\t//update\n"+
                "\tpublic ServiceResult updateObj("+ tableInfo.tableName+"DTO "+ uncapitalize(tableInfo.tableName)+"DTO) {\n"+
                "\t\tServiceResult result;\n"+
                "\t\t"+ tableInfo.tableName+"BO bo = "+ uncapitalize(tableInfo.tableName)+"DAO"+".addDTO("+ uncapitalize(tableInfo.tableName)+"DTO);\n"+
                "\t\tresult = new ServiceResult();\n" +
                "\t\treturn result;\n"+
                "\t}\n"
        );
        fileWriter.append("\n\t//delete\n"+
                "\tpublic ServiceResult deleteList(SearchCommonFinalDTO searchDTO) {\n"+
                "\t\tServiceResult result = "+ uncapitalize(tableInfo.tableName) +"DAO"+".deleteList(searchDTO.getLstFirst());\n"+
                "\t\treturn result;\n"+
                "\t}\n"
        );
        fileWriter.append("\n}");
        fileWriter.close();
    }

    static void genRsService(TableInfo tableInfo, String folder) throws IOException{
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName+"RsService.java");
        fileWriter.write("package com.tav.service.rest;\n" +
                "\n" +
                "import com.tav.service.dto."+ tableInfo.tableName+"DTO;\n" +
                "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
                "import javax.ws.rs.Consumes;\n" +
                "import javax.ws.rs.GET;\n" +
                "import javax.ws.rs.POST;\n" +
                "import javax.ws.rs.Path;\n" +
                "import javax.ws.rs.PathParam;\n" +
                "import javax.ws.rs.Produces;\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "import javax.ws.rs.core.Response;\n");
        fileWriter.append("\npublic interface "+ tableInfo.tableName+"RsService {\n");
        fileWriter.append("\t@POST\n" +
                "\t@Path(\"/getAll/{offset}/{limit}\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response getAll(SearchCommonFinalDTO searchDTO, @PathParam(\"offset\") Integer offset, @PathParam(\"limit\") Integer limit);\n");

        fileWriter.append("\n\t@POST\n" +
                "\t@Path(\"/getCount\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response getCount(SearchCommonFinalDTO searchDTO);\n");
        fileWriter.append("\n\t@GET\n" +
                "\t@Path(\"/getOneById/{id}\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response getOneById(@PathParam(\"id\") Long id);\n");
        fileWriter.append("\n\t@POST\n" +
                "\t@Path(\"/deleteList/\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response deleteList(SearchCommonFinalDTO searchDTO);\n");
        fileWriter.append("\n\t@POST\n" +
                "\t@Path(\"/updateBO/\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response updateObj("+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO);\n");
        fileWriter.append("\n\t@POST\n" +
                "\t@Path(\"/addDTO/\")\n" +
                "\t@Consumes({MediaType.APPLICATION_JSON})\n" +
                "\t@Produces({MediaType.APPLICATION_JSON})\n" +
                "\tpublic Response addDTO("+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO);\n");
        fileWriter.append("}");
        fileWriter.close();
    }

    static void genRsServiceImpl(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + tableInfo.tableName+"RsServiceImpl.java");
        fileWriter.write("package com.tav.service.rest;\n" +
                "\n" +
                "import com.tav.service.business."+ tableInfo.tableName+"BusinessImpl;\n" +
                "import com.tav.service.dto."+ tableInfo.tableName+"DTO;\n" +
                "import com.tav.service.dto.SearchCommonFinalDTO;\n" +
                "import com.tav.service.dto.ServiceResult;\n" +
                "import java.util.List;\n" +
                "import java.util.Date;" +
                "import javax.ws.rs.core.Response;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n");
        fileWriter.append("\npublic class "+ tableInfo.tableName+"RsServiceImpl implements "+ tableInfo.tableName+"RsService{\n" +
                "\n" +
                "\t@Autowired\n" +
                "\tprivate "+ tableInfo.tableName+"BusinessImpl "+uncapitalize(tableInfo.tableName)+"BusinessImpl;\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {\n" +
                "\t\tList<"+ tableInfo.tableName+"DTO> lst = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.getAll(searchDTO, offset, limit);\n" +
                "\t\tif (lst == null) {\n" +
                "\t\t\treturn Response.status(Response.Status.BAD_REQUEST).build();\n" +
                "\t\t} else {\n" +
                "\t\t\treturn Response.ok(lst).build();\n" +
                "\t\t}\n" +
                "\t}\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response getCount(SearchCommonFinalDTO searchDTO) {\n" +
                "\t\tint result = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.getCount(searchDTO);\n" +
                "\t\treturn Response.ok(result).build();\n" +
                "\t}\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response getOneById(Long id) {\n" +
                "\t\t"+ tableInfo.tableName+"DTO result = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.getOneObjById(id);\n" +
                "\t\treturn Response.ok(result).build();\n" +
                "\t}\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response deleteList(SearchCommonFinalDTO searchDTO) {\n" +
                "\t\tServiceResult result = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.deleteList(searchDTO);\n" +
                "\t\tif (\"FAIL\".equals(result.getError())) {\n" +
                "\t\t\treturn Response.status(Response.Status.BAD_REQUEST).build();\n" +
                "\t\t} else {\n" +
                "\t\t\treturn Response.ok(result).build();\n" +
                "\t\t}\n" +
                "\t}\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response updateObj("+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO) {\n" +
                "\t\tServiceResult result = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.updateObj("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                "\t\treturn Response.ok(result).build();\n" +
                "\t}\n");
        fileWriter.append("\n\t@Override\n" +
                "\tpublic Response addDTO("+ tableInfo.tableName+"DTO "+uncapitalize(tableInfo.tableName)+"DTO) {\n" +
                "\t\tServiceResult result = "+uncapitalize(tableInfo.tableName)+"BusinessImpl.addDTO("+uncapitalize(tableInfo.tableName)+"DTO);\n" +
                "\t\treturn Response.ok(result).build();\n" +
                "\t}\n");
        fileWriter.append("\n}");
        fileWriter.close();
    }

    static void genBean(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\" + "bean.txt");
        fileWriter.append("<jaxrs:server id=\""+uncapitalize(tableInfo.tableName)+"RsServiceRest\"\naddress=\"/"+uncapitalize(tableInfo.tableName)+"RsServiceRest\">\n" +
                "\t\t<jaxrs:providers>\n" +
                "\t\t\t<ref bean=\"jsonProvider\" />\n" +
                "\t\t</jaxrs:providers>\n" +
                "\t\t<jaxrs:serviceBeans>\n" +
                "\t\t\t<bean id=\""+uncapitalize(tableInfo.tableName)+"RsServiceClass\"\nclass=\"com.tav.service.rest."+ tableInfo.tableName+"RsServiceImpl\"/>\n" +
                "\t\t</jaxrs:serviceBeans>\n" +
                "\t</jaxrs:server>");
        fileWriter.close();
    }

    public static void genService(TableInfo tableInfo, String folder) throws IOException {
        File dir = new File(folder);
        dir.mkdirs();
        genBO(tableInfo, folder);
        genDTO(tableInfo, folder);
        genDAO(tableInfo, folder);
        genBusiness(tableInfo, folder);
        genBusinessImpl(tableInfo, folder);
        genRsService(tableInfo, folder);
        genRsServiceImpl(tableInfo, folder);
        genBean(tableInfo, folder);
    }
}
