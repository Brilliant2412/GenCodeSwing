/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;
import controller.CodeGenerator;
import static controller.CodeGenerator.uncapitalize;
import java.io.FileWriter;
import java.io.IOException;
import model.ColumnProperty;
import model.TableInfo;
/**
 *
 * @author hungy
 */
public class Tung {

    public static String resSearch(String tenTruong,String moTa,String kieuDL,String kieuNhap){
        String res = "";
        if (kieuDL.equals("Long") || kieuDL.equals("Double")){
            if (kieuNhap.equals("Combobox")){
                res = "\t\t\t\t<label class=\"col-lg-1 control-label\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-2 selectContainer\">\n" +
                        "\t\t\t\t\t<class=\"form-control\" placeholder=\""+moTa+"\" id=\"cb"+tenTruong+"Search\">" +
                        "\t\t\t\t</div>\n";
            }else{
                res = "\t\t\t\t<label class=\"col-lg-1 control-label\">"+moTa+"</label>\n" +
                        "\t\t\t\t<div class=\"col-lg-1 selectContainer\">\n" +
                        "\t\t\t\t\t<div <input type=\"number\" class=\"form-control\" placeholder=\"Từ\" id=\""+tenTruong+"SearchFrom\"> </div> \n" +
                        "\t\t\t\t</div>\n" +
                        "\t\t\t\t<div class=\"col-lg-1\">\n" +
                        "\t\t\t\t\t<div <input type=\"number\" class=\"form-control\" placeholder=\"Đến\" id=\""+tenTruong+"SearchTo\"> </div> \n" +
                        "\t\t\t\t</div>\n";
            }
        }else if (kieuDL.equals("Date")){
            res = "\t\t\t\t<label class=\"col-lg-1 control-label\">"+moTa+"</label>\n" +
                    "\t\t\t\t<div class=\"col-lg-1 selectContainer\">\n" +
                    "\t\t\t\t\t<div <input type=\"text\" class=\"dateCalendar\" placeholder=\"Từ\" id=\""+tenTruong+"SearchFrom\"> </div> \n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t\t<div class=\"col-lg-1\">\n" +
                    "\t\t\t\t\t<div <input type=\"text\" class=\"dateCalendar\" placeholder=\"Đến\" id=\""+tenTruong+"SearchTo\"> </div> \n" +
                    "\t\t\t\t</div>\n";
        }
        return res;
    }



    static void genControllerSearch(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\ControllerSearch.jsp.txt");
        fileWriter.write("package com.tav.web.controller;\n" +
                "\n" +
                "import com.google.common.base.Strings;\n" +
                "import com.tav.web.common.DateUtil;import com.google.gson.Gson;\n" +
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
                "import com.tav.web.data.EvaluatePlan1Data;\n" +
                "import com.tav.web.dto.EvaluatePlan1DTO;\n" +
                "import com.tav.web.dto.ImportErrorMessage;\n" +
                "import java.util.Date;import com.tav.web.dto.ObjectCommonSearchDTO;\n" +
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
                "import org.springframework.web.servlet.ModelAndView;\n" +
                "\n" +
                "@Controller\n" +
                "public class EvaluatePlan1Controller extends SubBaseController {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private EvaluatePlan1Data evaluatePlan1Data;\n" +
                "\n" +
                "    @RequestMapping(\"/\" + ErpConstants.RequestMapping.EVALUATE_PLAN1)\n" +
                "    public ModelAndView agent(Model model, HttpServletRequest request) {\n" +
                "        return new ModelAndView(\"evaluatePlan1\");\n" +
                "    }\n" +
                "\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.GET_ALL_EVALUATE_PLAN1}, method = RequestMethod.GET)\n" +
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

        fileWriter.append("\n" +
                "            List<EvaluatePlan1DTO> lst = new ArrayList<>();\n" +
                "            Integer totalRecords = 0;\n" +
                "            totalRecords = evaluatePlan1Data.getCount(searchDTO);\n" +
                "            if (totalRecords > 0) {\n" +
                "                lst = evaluatePlan1Data.getAll(searchDTO, offset, limit);\n" +
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
                "    @RequestMapping(value = \"/\" + ErpConstants.RequestMapping.GET_EVALUATE_PLAN1_BY_ID, method = RequestMethod.GET)\n" +
                "    public @ResponseBody\n" +
                "    EvaluatePlan1DTO getOneById(HttpServletRequest request) {\n" +
                "        Long id = Long.parseLong(request.getParameter(\"gid\"));\n" +
                "        return evaluatePlan1Data.getOneById(id);\n" +
                "    }\n" +
                "\n" +
                "    //add\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.ADD_EVALUATE_PLAN1}, method = RequestMethod.POST, produces = ErpConstants.LANGUAGE)\n" +
                "    @ResponseBody\n" +
                "    public String addOBJ(@ModelAttribute(\"evaluatePlan1Form\") EvaluatePlan1DTO evaluatePlan1DTO, MultipartHttpServletRequest multipartRequest,\n" +
                "            HttpServletRequest request) throws ParseException {\n" +
                "\n" +
                "        JSONObject result;\n" +
                "        String error = validateForm(evaluatePlan1DTO);\n" +
                "        ServiceResult serviceResult;\n" +
                "        if (error != null) {\n" +
                "            return error;\n" +
                "        } else {\n" +
                "            if (!StringUtil.isEmpty(evaluatePlan1DTO.getExpertise_date())) {\n" +
                "                        evaluatePlan1DTO.setExpertise_date(DateUtil.formatDate(evaluatePlan1DTO.getExpertise_date()));\n" +
                "            }\n" +
                "            serviceResult = evaluatePlan1Data.addObj(evaluatePlan1DTO);\n" +
                "            processServiceResult(serviceResult);\n" +
                "            result = new JSONObject(serviceResult);\n" +
                "        }\n" +
                "        return result.toString();\n" +
                "    }\n" +
                "\n" +
                "    //update\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.UPDATE_EVALUATE_PLAN1}, method = RequestMethod.POST, produces = ErpConstants.LANGUAGE)\n" +
                "    @ResponseBody\n" +
                "    public String updateOBJ(@ModelAttribute(\"evaluatePlan1Form\") EvaluatePlan1DTO evaluatePlan1DTO, MultipartHttpServletRequest multipartRequest,\n" +
                "            HttpServletRequest request) throws ParseException {\n" +
                "\n" +
                "        JSONObject result;\n" +
                "        String error = validateForm(evaluatePlan1DTO);\n" +
                "        ServiceResult serviceResult;\n" +
                "        if (error != null) {\n" +
                "            return error;\n" +
                "        } else {\n" +
                "            if (!StringUtil.isEmpty(evaluatePlan1DTO.getExpertise_date())) {\n" +
                "                        evaluatePlan1DTO.setExpertise_date(DateUtil.formatDate(evaluatePlan1DTO.getExpertise_date()));\n" +
                "            }\n" +
                "            serviceResult = evaluatePlan1Data.updateBO(evaluatePlan1DTO);\n" +
                "            processServiceResult(serviceResult);\n" +
                "            result = new JSONObject(serviceResult);\n" +
                "        }\n" +
                "        return result.toString();\n" +
                "    }\n" +
                "\n" +
                "    //validate\n" +
                "    private String validateForm(EvaluatePlan1DTO cbChaDTO) {\n" +
                "        List<ValidationResult> lsError = new ArrayList<>();\n" +
                "        if (lsError.size() > 0) {\n" +
                "            Gson gson = new Gson();\n" +
                "            return gson.toJson(lsError);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @RequestMapping(value = {\"/\" + ErpConstants.RequestMapping.DELETE_EVALUATE_PLAN1}, method = RequestMethod.POST,\n" +
                "            produces = \"text/html;charset=utf-8\")\n" +
                "    public @ResponseBody\n" +
                "    String deleteObj(@ModelAttribute(\"objectCommonSearchDTO\") ObjectCommonSearchDTO objectCommonSearchDTO,\n" +
                "            HttpServletRequest request) {\n" +
                "        HttpSession session = request.getSession();\n" +
                "        ServiceResult serviceResult = evaluatePlan1Data.deleteObj(objectCommonSearchDTO);\n" +
                "        processServiceResult(serviceResult);\n" +
                "        JSONObject result = new JSONObject(serviceResult);\n" +
                "        return result.toString();\n" +
                "    }\n" +
                "\n" +
                "}\n");

        fileWriter.close();
    }

    static void genDAOSearch(TableInfo tableInfo, String folder) throws IOException {
        FileWriter fileWriter = new FileWriter(folder + "\\DAOSearch.txt");
        fileWriter.write("package com.tav.service.dao;\n" +
                "\n" +
                "import com.tav.service.base.db.dao.BaseFWDAOImpl;\n" +
                "import com.tav.service.bo.EvaluatePlan1BO;\n" +
                "import com.tav.service.dto.EvaluatePlan1DTO;\n" +
                "import com.tav.service.dto.ObjectCommonSearchDTO;\n" +
                "import com.tav.service.dto.ServiceResult;\n" +
                "import java.math.BigInteger;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.List;\n" +
                "import java.util.Date;import org.hibernate.Criteria;\n" +
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
                "@Repository(\"evaluatePlan1DAO\")\n" +
                "public class EvaluatePlan1DAO extends BaseFWDAOImpl<EvaluatePlan1BO, Long>{\n" +
                "    \n" +
                "    public List<EvaluatePlan1DTO> getAll(ObjectCommonSearchDTO searchDTO, Integer offset, Integer limit) {\n" +
                "        SimpleDateFormat formatter = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n" +
                "        StringBuilder sqlCommand = new StringBuilder();\n" +
                "\t\tsqlCommand.append(\" SELECT \");\n" +
                "\t\tsqlCommand.append(\"tbl.gid as gid, \");\n" +
                "\t\tsqlCommand.append(\"tbl.expertise_organ as expertise_organ, \");\n" +
                "\t\tsqlCommand.append(\"mst1.dvs_name as expertise_organST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.plan_number as plan_number, \");\n" +
                "\t\tsqlCommand.append(\"tbl.place1 as place1, \");\n" +
                "\t\tsqlCommand.append(\"mst2.dvs_name as place1ST, \");\n" +
                "\t\tsqlCommand.append(\"to_char(tbl.expertise_date, 'DD/MM/YYYY') as expertise_dateST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.dept_id as dept_id, \");\n" +
                "\t\tsqlCommand.append(\"d1.dept_name as dept_idST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.dept_other as dept_other, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_bases as the_bases, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_purpose as the_purpose, \");\n" +
                "\t\tsqlCommand.append(\"tbl.place_and_time as place_and_time, \");\n" +
                "\t\tsqlCommand.append(\"tbl.test_content as test_content, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_participants as the_participants, \");\n" +
                "\t\tsqlCommand.append(\"tbl.organization_perform as organization_perform, \");\n" +
                "\t\tsqlCommand.append(\"tbl.master_id as master_id \");\n" +
                "\n" +
                "\t\tsqlCommand.append(\" FROM evaluate_plan1 tbl \");\n" +
                "\t\tsqlCommand.append(\" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'\");\n" +
                "\t\tsqlCommand.append(\" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '705'\");\n" +
                "\t\tsqlCommand.append(\" left join department d1 on d1.dept_id = tbl.dept_id\");\n" +
                "\n" +
                "\t\tsqlCommand.append(\" WHERE 1=1 \");\n" +
                "\t//String\n" +
                " \tif (!StringUtil.isEmpty(objectSearchCommonDTO.getStringKeyWord())) {\n" +
                "            sqlCommand.append(\" and (   \");\n");
        int count_str =0;
        int count_cb =0;
        int count_db =0;
        int count_long =0;
        int count_date =0;

        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("String"))
                {
                    count_str++;
                    if (count_str==1)
                    {
                        fileWriter.append("\t    sqlCommand.append(\"  LOWER(tbl."+colProp.getColName()+") like LOWER(:stringKeyWord)   \");\n");
                    }
                    else {
                        fileWriter.append("\t    sqlCommand.append(\"  OR LOWER(tbl." + colProp.getColName() + ") like LOWER(:stringKeyWord)   \");\n");
                    }
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

        }
        fileWriter.append("\t    sqlCommand.append(\" )   \");\n" +
                "        }\n" +
                "\n");
        int t1=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Long") )
                {
                    if (colProp.getInputType().equals("Combobox"))
                    {
                        t1++;
                        fileWriter.append("\tif (objectSearchCommonDTO.getListLong"+t1+"() != null && !objectSearchCommonDTO.getListLong"+t1+"().isEmpty()) {\n" +
                                "            sqlCommand.append(\" and tbl."+colProp.getColName()+" in (:listLong"+t1+") \");\n" +
                                "        }\n\n");
                    }
                }

            }

        }

        int t2=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Long") )
                {
                    if (!colProp.getInputType().equals("Combobox"))
                    {
                        t2++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+t2+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" >= (:long"+(t2++)+") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+t2+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" <= (:long"+t2+") \");\n" +
                                "\t}\n");
                    }
                }
            }
        }

        int t3=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Double") )
                {
                    if (!colProp.getInputType().equals("Combobox"))
                    {
                        t3++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+t3+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" >= (:double"+(t3++)+") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+t3+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" <= (:double"+t3+") \");\n" +
                                "\t}\n");
                    }
                }

            }

        }

        int t4=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Date") ) {
                    t4++;
                    fileWriter.append("\tif (  (objectSearchCommonDTO.getString"+t4+"() != null && !objectSearchCommonDTO.getString"+t4+"().isEmpty())   &&   (objectSearchCommonDTO.getString"+(t4+1)+"() != null && !objectSearchCommonDTO.getString"+(t4+1)+"().isEmpty())  ) {\n" +
                            "            sqlCommand.append(\"  and ( tbl."+colProp.getColName()+" between (:string"+t4+") and (:string"+(t4+1)+")    ) \");\n" +
                            "        }\n");

                }

            }

        }
        fileWriter.append("\tsqlCommand.append(\" ORDER BY tbl.gid \");\n" +
                "\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n" +
                "\t\t\t.addScalar(\"gid\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_organ\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_organST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"plan_number\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place1\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place1ST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_dateST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_id\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_idST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_other\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_bases\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_purpose\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place_and_time\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"test_content\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_participants\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"organization_perform\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"master_id\", StringType.INSTANCE)\n" +
                "\t\t\t.setResultTransformer(Transformers.aliasToBean(EvaluatePlan1DTO.class))\n" +
                "\t\t\t.setFirstResult(offset);\n" +
                "\t\tif (limit != null && limit != 0) {\n" +
                "\t\t\tquery.setMaxResults(limit);\n" +
                "\t\t}\n");
        //String
        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getStringKeyWord())) {\n" +
                "\t\tq.setParameter(\"stringKeyWord\", \"%\" + objectSearchCommonDTO.getStringKeyWord() + \"%\");\n" +
                "\t}\n");
        //Combobox
        for (int i = 0; i <count_cb ; i++) {

            fileWriter.append("\tif (objectSearchCommonDTO.getListLong"+i+"() != null && !objectSearchCommonDTO.getListLong"+i+"().isEmpty()) {\n" +
                    "            q.setParameterList(\"listLong"+i+"\", objectSearchCommonDTO.getListLong"+i+"());\n" +
                    "        }\n");

        }
        //double
        for (int i = 0; i <count_db ; i+=2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+i+"())) {\n" +
                    "\t\tq.setParameter(\"double"+i+"\", objectSearchCommonDTO.getDouble"+i+"());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+(i+1)+"())) {\n" +
                    "\t\tq.setParameter(\"double"+(i+1)+"\", objectSearchCommonDTO.getDouble"+(i+1)+"());\n" +
                    "\t}\n");

        }
        //Long
        for (int i = 0; i <count_long ; i+=2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+i+"())) {\n" +
                    "\t\tq.setParameter(\"long"+i+"\", objectSearchCommonDTO.getLong"+i+"());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+(i+1)+"())) {\n" +
                    "\t\tq.setParameter(\"long"+(i+1)+"\", objectSearchCommonDTO.getLong"+(i+1)+"());\n" +
                    "\t}\n");

        }

        //date
        for (int i = 0; i <count_date ; i+=2) {

            fileWriter.append("\tif (  (objectSearchCommonDTO.getString"+i+"() != null && !objectSearchCommonDTO.getString"+i+"().isEmpty())   &&   (objectSearchCommonDTO.getString2() != null && !objectSearchCommonDTO.getString2().isEmpty())  ) {\n" +
                    "            try {\n" +
                    "                q.setParameter(\"string"+i+"\", formatter.parse(objectSearchCommonDTO.getString"+i+"() + \" 00:00:00\"));\n" +
                    "                q.setParameter(\"string"+(i+1)+"\", formatter.parse(objectSearchCommonDTO.getString"+(i+1)+"() + \" 23:59:59\"));\n" +
                    "            } catch (ParseException ex) {\n" +
                    "            }\n" +
                    "        }\n");

        }
        fileWriter.append("\t\treturn query.list();\n" +
                "\t}\n" +
                "\n" +
                "public Integer getCount(ObjectCommonSearchDTO searchDTO) {\n" +
                "        SimpleDateFormat formatter = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n" +
                "        StringBuilder sqlCommand = new StringBuilder();\n" +
                "        sqlCommand.append(\" SELECT \");\n" +
                "        sqlCommand.append(\" COUNT(1)\");\n" +
                "        sqlCommand.append(\" FROM  evaluate_plan1 tbl \");\n" +
                "        sqlCommand.append(\" WHERE 1=1 \");\n" +
                "\t//String\n" +
                " \tif (!StringUtil.isEmpty(objectSearchCommonDTO.getStringKeyWord())) {\n" +
                "            sqlCommand.append(\" and (   \");\n");
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
            }
        }
        fileWriter.append("\t    sqlCommand.append(\" )   \");\n" +
                "        }\n" +
                "\n");
        int c1=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Long") )
                {
                    if (colProp.getInputType().equals("Combobox"))
                    {
                        c1++;
                        fileWriter.append("\tif (objectSearchCommonDTO.getListLong"+c1+"() != null && !objectSearchCommonDTO.getListLong"+c1+"().isEmpty()) {\n" +
                                "            sqlCommand.append(\" and tbl."+colProp.getColName()+" in (:listLong"+c1+") \");\n" +
                                "        }\n\n");
                    }
                }

            }

        }

        int c2=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Long") )
                {
                    if (!colProp.getInputType().equals("Combobox"))
                    {
                        t2++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+c2+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" >= (:long"+(c2++)+") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+c2+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" <= (:long"+c2+") \");\n" +
                                "\t}\n");
                    }
                }
            }
        }

        int c3=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Double") )
                {
                    if (!colProp.getInputType().equals("Combobox"))
                    {
                        t3++;
                        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+c3+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" >= (:double"+(c3++)+") \");\n" +
                                "\t}\n" +
                                "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+t3+"())) {\n" +
                                "\t\tsqlCommand.append(\" and tbl."+colProp.getColName()+" <= (:double"+c3+") \");\n" +
                                "\t}\n");
                    }
                }

            }

        }

        int c4=0;
        for (int i = 0; i < tableInfo.columns.size(); i++) {
            ColumnProperty colProp = tableInfo.columns.get(i);
            if (colProp.isSearch())
            {
                if (colProp.getColType().equals("Date") ) {
                    t4++;
                    fileWriter.append("\tif (  (objectSearchCommonDTO.getString"+c4+"() != null && !objectSearchCommonDTO.getString"+c4+"().isEmpty())   &&   (objectSearchCommonDTO.getString"+(c4+1)+"() != null && !objectSearchCommonDTO.getString"+(c4+1)+"().isEmpty())  ) {\n" +
                            "            sqlCommand.append(\"  and ( tbl."+colProp.getColName()+" between (:string"+c4+") and (:string"+(c4+1)+")    ) \");\n" +
                            "        }\n");

                }

            }

        }
        fileWriter.append("        Query query = getSession().createSQLQuery(sqlCommand.toString());\n");
        //String
        fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getStringKeyWord())) {\n" +
                "\t\tq.setParameter(\"stringKeyWord\", \"%\" + objectSearchCommonDTO.getStringKeyWord() + \"%\");\n" +
                "\t}\n");
        //Combobox
        for (int i = 0; i <count_cb ; i++) {

            fileWriter.append("\tif (objectSearchCommonDTO.getListLong"+i+"() != null && !objectSearchCommonDTO.getListLong"+i+"().isEmpty()) {\n" +
                    "            q.setParameterList(\"listLong"+i+"\", objectSearchCommonDTO.getListLong"+i+"());\n" +
                    "        }\n");

        }
        //double
        for (int i = 0; i <count_db ; i+=2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+i+"())) {\n" +
                    "\t\tq.setParameter(\"double"+i+"\", objectSearchCommonDTO.getDouble"+i+"());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getDouble"+(i+1)+"())) {\n" +
                    "\t\tq.setParameter(\"double"+(i+1)+"\", objectSearchCommonDTO.getDouble"+(i+1)+"());\n" +
                    "\t}\n");

        }
        //Long
        for (int i = 0; i <count_long ; i+=2) {

            fileWriter.append("\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+i+"())) {\n" +
                    "\t\tq.setParameter(\"long"+i+"\", objectSearchCommonDTO.getLong"+i+"());\n" +
                    "\t}\n" +
                    "\tif (!StringUtil.isEmpty(objectSearchCommonDTO.getLong"+(i+1)+"())) {\n" +
                    "\t\tq.setParameter(\"long"+(i+1)+"\", objectSearchCommonDTO.getLong"+(i+1)+"());\n" +
                    "\t}\n");

        }

        //date
        for (int i = 0; i <count_date ; i+=2) {

            fileWriter.append("\tif (  (objectSearchCommonDTO.getString"+i+"() != null && !objectSearchCommonDTO.getString"+i+"().isEmpty())   &&   (objectSearchCommonDTO.getString2() != null && !objectSearchCommonDTO.getString2().isEmpty())  ) {\n" +
                    "            try {\n" +
                    "                q.setParameter(\"string"+i+"\", formatter.parse(objectSearchCommonDTO.getString"+i+"() + \" 00:00:00\"));\n" +
                    "                q.setParameter(\"string"+(i+1)+"\", formatter.parse(objectSearchCommonDTO.getString"+(i+1)+"() + \" 23:59:59\"));\n" +
                    "            } catch (ParseException ex) {\n" +
                    "            }\n" +
                    "        }\n");

        }

        fileWriter.append("        return ((BigInteger) query.uniqueResult()).intValue();\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "    }\t//get one\n" +
                "\tpublic EvaluatePlan1DTO getOneObjById(Long id) {\n" +
                "\t\tStringBuilder sqlCommand = new StringBuilder();\n" +
                "\t\tsqlCommand.append(\" SELECT \");\n" +
                "\t\tsqlCommand.append(\"tbl.gid as gid, \");\n" +
                "\t\tsqlCommand.append(\"tbl.expertise_organ as expertise_organ, \");\n" +
                "\t\tsqlCommand.append(\"mst1.dvs_name as expertise_organST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.plan_number as plan_number, \");\n" +
                "\t\tsqlCommand.append(\"tbl.place1 as place1, \");\n" +
                "\t\tsqlCommand.append(\"mst2.dvs_name as place1ST, \");\n" +
                "\t\tsqlCommand.append(\"to_char(tbl.expertise_date, 'DD/MM/YYYY') as expertise_dateST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.dept_id as dept_id, \");\n" +
                "\t\tsqlCommand.append(\"d1.dept_name as dept_idST, \");\n" +
                "\t\tsqlCommand.append(\"tbl.dept_other as dept_other, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_bases as the_bases, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_purpose as the_purpose, \");\n" +
                "\t\tsqlCommand.append(\"tbl.place_and_time as place_and_time, \");\n" +
                "\t\tsqlCommand.append(\"tbl.test_content as test_content, \");\n" +
                "\t\tsqlCommand.append(\"tbl.the_participants as the_participants, \");\n" +
                "\t\tsqlCommand.append(\"tbl.organization_perform as organization_perform, \");\n" +
                "\t\tsqlCommand.append(\"tbl.master_id as master_id \");\n" +
                "\n" +
                "\t\tsqlCommand.append(\" FROM evaluate_plan1 tbl \");\n" +
                "\t\tsqlCommand.append(\" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'\");\n" +
                "\t\tsqlCommand.append(\" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '705'\");\n" +
                "\t\tsqlCommand.append(\" left join department d1 on d1.dept_id = tbl.dept_id\");\n" +
                "\t\tsqlCommand.append(\" WHERE tbl.gid = :gid\");\n" +
                "\t\tQuery query = getSession().createSQLQuery(sqlCommand.toString())\n" +
                "\t\t\t.addScalar(\"gid\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_organ\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_organST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"plan_number\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place1\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place1ST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"expertise_dateST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_id\", LongType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_idST\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"dept_other\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_bases\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_purpose\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"place_and_time\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"test_content\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"the_participants\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"organization_perform\", StringType.INSTANCE)\n" +
                "\t\t\t.addScalar(\"master_id\", StringType.INSTANCE)\n" +
                "\t\t\t.setResultTransformer(Transformers.aliasToBean(EvaluatePlan1DTO.class));\n" +
                "\t\tquery.setParameter(\"gid\", id);\n" +
                "\t\tEvaluatePlan1DTO item = (EvaluatePlan1DTO) query.uniqueResult();\n" +
                "\t\treturn item;\n" +
                "\t}\n" +
                "\n" +
                "\t//delete\n" +
                "\t@Transactional\n" +
                "\tpublic ServiceResult deleteList(List<Long> listIds) {\n" +
                "\t\tServiceResult result = new ServiceResult();\n" +
                "\t\tQuery q = getSession().createQuery(\"DELETE FROM EvaluatePlan1BO WHERE gid IN (:listIds)\");\n" +
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
                "\t}\n" +
                "\n" +
                "\t//update\n" +
                "\t@Transactional\n" +
                "\tpublic ServiceResult updateObj(EvaluatePlan1DTO dto) {\n" +
                "\t\tServiceResult result = new ServiceResult();\n" +
                "\t\tEvaluatePlan1BO bo = dto.toModel();\n" +
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
                "\t}\n" +
                "\n" +
                "\t@Transactional\n" +
                "\tpublic EvaluatePlan1BO addDTO(EvaluatePlan1DTO dto) {\n" +
                "\t\tServiceResult result = new ServiceResult();\n" +
                "\t\tSession session1 = getSession();\n" +
                "\t\tEvaluatePlan1BO BO = new EvaluatePlan1BO();\n" +
                "\t\ttry {\n" +
                "\t\t\tBO = (EvaluatePlan1BO) session1.merge(dto.toModel());\n" +
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
                "}");


        fileWriter.close();
    }


}
