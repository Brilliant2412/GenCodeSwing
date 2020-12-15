package com.tav.service.dao;

import com.tav.service.base.db.dao.BaseFWDAOImpl;
import com.tav.service.bo.EvaluatePlan6BO;
import com.tav.service.dto.EvaluatePlan6DTO;
import com.tav.service.dto.SearchCommonFinalDTO;
import com.tav.service.dto.ServiceResult;
import com.tav.service.common.StringUtil;
import java.text.ParseException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("evaluatePlan6DAO")
public class EvaluatePlan6DAO extends BaseFWDAOImpl<EvaluatePlan6BO, Long>{
    
    public List<EvaluatePlan6DTO> getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sqlCommand = new StringBuilder();
		sqlCommand.append(" SELECT ");
		sqlCommand.append("tbl.gid as gid, ");
		sqlCommand.append("tbl.expertise_organ as expertise_organ, ");
		sqlCommand.append("mst1.dvs_name as expertise_organST, ");
		sqlCommand.append("tbl.plan_number as plan_number, ");
		sqlCommand.append("tbl.place1 as place1, ");
		sqlCommand.append("mst2.dvs_name as place1ST, ");
		sqlCommand.append("to_char(tbl.expertise_date, 'DD/MM/YYYY') as expertise_dateST, ");
		sqlCommand.append("tbl.dept_id1 as dept_id1, ");
		sqlCommand.append("d1.dvs_name as dept_id1ST, ");
		sqlCommand.append("tbl.dept_other1 as dept_other1, ");
		sqlCommand.append("tbl.the_bases1 as the_bases1, ");
		sqlCommand.append("tbl.the_purpose1 as the_purpose1, ");
		sqlCommand.append("tbl.place_and_time1 as place_and_time1, ");
		sqlCommand.append("tbl.test_content as test_content, ");
		sqlCommand.append("tbl.the_participants as the_participants, ");
		sqlCommand.append("tbl.organization_perform as organization_perform, ");
		sqlCommand.append("tbl.master_id as master_id, ");
		sqlCommand.append("tbl.file as file ");

		sqlCommand.append(" FROM evaluate_plan6 tbl ");
		sqlCommand.append(" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'");
		sqlCommand.append(" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '701'");
		sqlCommand.append(" left join department d1 on d1.dvs_value = tbl.dept_id1 AND d1.dvs_group_cd = '701'");

		sqlCommand.append(" WHERE 1=1 ");
	//String
 	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
            sqlCommand.append(" and (   ");
	    sqlCommand.append("  LOWER(tbl.plan_number) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.dept_other1) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.the_participants) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.organization_perform) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.master_id) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append(" )   ");
        }

	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            sqlCommand.append(" and tbl.expertise_organ in (:listLong1) ");
        }

	if (searchDTO.getListLong2() != null && !searchDTO.getListLong2().isEmpty()) {
            sqlCommand.append(" and tbl.place1 in (:listLong2) ");
        }

	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            sqlCommand.append("  and ( tbl.expertise_date between (:string1) and (:string2)    ) ");
        }
		sqlCommand.append(" ORDER BY tbl.gid ");
		Query query = getSession().createSQLQuery(sqlCommand.toString())
			.addScalar("gid", LongType.INSTANCE)
			.addScalar("expertise_organ", LongType.INSTANCE)
			.addScalar("expertise_organST", StringType.INSTANCE)
			.addScalar("plan_number", StringType.INSTANCE)
			.addScalar("place1", LongType.INSTANCE)
			.addScalar("place1ST", StringType.INSTANCE)
			.addScalar("expertise_dateST", StringType.INSTANCE)
			.addScalar("dept_id1", LongType.INSTANCE)
			.addScalar("dept_id1ST", StringType.INSTANCE)
			.addScalar("dept_other1", StringType.INSTANCE)
			.addScalar("the_bases1", StringType.INSTANCE)
			.addScalar("the_purpose1", StringType.INSTANCE)
			.addScalar("place_and_time1", StringType.INSTANCE)
			.addScalar("test_content", StringType.INSTANCE)
			.addScalar("the_participants", StringType.INSTANCE)
			.addScalar("organization_perform", StringType.INSTANCE)
			.addScalar("master_id", StringType.INSTANCE)
			.addScalar("file", FileType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(EvaluatePlan6DTO.class))
			.setFirstResult(offset);
		if (limit != null && limit != 0) {
			query.setMaxResults(limit);
		}
	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
		query.setParameter("stringKeyWord", "%" + searchDTO.getStringKeyWord() + "%");
	}
	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            query.setParameterList("listLong1", searchDTO.getListLong1());
        }
	if (searchDTO.getListLong2() != null && !searchDTO.getListLong2().isEmpty()) {
            query.setParameterList("listLong2", searchDTO.getListLong2());
        }
	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            try {
                query.setParameter("string1", formatter.parse(searchDTO.getString1() + " 00:00:00"));
                query.setParameter("string2", formatter.parse(searchDTO.getString2() + " 23:59:59"));
            } catch (ParseException ex) {
            }
        }
		return query.list();
	}

public Integer getCount(SearchCommonFinalDTO searchDTO) {
           SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
           StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append(" SELECT ");
        sqlCommand.append(" COUNT(1)");
        sqlCommand.append(" FROM  evaluate_plan6 tbl ");
        sqlCommand.append(" WHERE 1=1 ");
	//String
 	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
            sqlCommand.append(" and (   ");
	    sqlCommand.append("  LOWER(tbl.plan_number) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.dept_other1) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.the_participants) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.organization_perform) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.master_id) like LOWER(:stringKeyWord)   ");
            sqlCommand.append(" )   ");
}	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            sqlCommand.append(" and tbl.expertise_organ in (:listLong1) ");
        }

	if (searchDTO.getListLong2() != null && !searchDTO.getListLong2().isEmpty()) {
            sqlCommand.append(" and tbl.place1 in (:listLong2) ");
        }

	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            sqlCommand.append("  and ( tbl.expertise_date between (:string1) and (:string2)    ) ");
        }
        Query query = getSession().createSQLQuery(sqlCommand.toString());
	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
		query.setParameter("stringKeyWord", "%" + searchDTO.getStringKeyWord() + "%");
	}
	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            query.setParameterList("listLong1", searchDTO.getListLong1());
        }
	if (searchDTO.getListLong2() != null && !searchDTO.getListLong2().isEmpty()) {
            query.setParameterList("listLong2", searchDTO.getListLong2());
        }
	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            try {
                query.setParameter("string1", formatter.parse(searchDTO.getString1() + " 00:00:00"));
                query.setParameter("string2", formatter.parse(searchDTO.getString2() + " 23:59:59"));
            } catch (ParseException ex) {
            }
        }
        return ((BigInteger) query.uniqueResult()).intValue();
}
	//get one
	public EvaluatePlan6DTO getOneObjById(Long id) {
		StringBuilder sqlCommand = new StringBuilder();
		sqlCommand.append(" SELECT ");
		sqlCommand.append("tbl.gid as gid, ");
		sqlCommand.append("tbl.expertise_organ as expertise_organ, ");
		sqlCommand.append("mst1.dvs_name as expertise_organST, ");
		sqlCommand.append("tbl.plan_number as plan_number, ");
		sqlCommand.append("tbl.place1 as place1, ");
		sqlCommand.append("mst2.dvs_name as place1ST, ");
		sqlCommand.append("to_char(tbl.expertise_date, 'DD/MM/YYYY') as expertise_dateST, ");
		sqlCommand.append("tbl.dept_id1 as dept_id1, ");
		sqlCommand.append("d1.dvs_name as dept_id1ST, ");
		sqlCommand.append("tbl.dept_other1 as dept_other1, ");
		sqlCommand.append("tbl.the_bases1 as the_bases1, ");
		sqlCommand.append("tbl.the_purpose1 as the_purpose1, ");
		sqlCommand.append("tbl.place_and_time1 as place_and_time1, ");
		sqlCommand.append("tbl.test_content as test_content, ");
		sqlCommand.append("tbl.the_participants as the_participants, ");
		sqlCommand.append("tbl.organization_perform as organization_perform, ");
		sqlCommand.append("tbl.master_id as master_id, ");
		sqlCommand.append("tbl.file as file ");

		sqlCommand.append(" FROM evaluate_plan6 tbl ");
		sqlCommand.append(" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'");
		sqlCommand.append(" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '701'");
		sqlCommand.append(" left join department d1 on d1.dvs_value = tbl.dept_id1 AND d1.dvs_group_cd = '701'");
		sqlCommand.append(" WHERE tbl.gid = :gid");
		Query query = getSession().createSQLQuery(sqlCommand.toString())
			.addScalar("gid", LongType.INSTANCE)
			.addScalar("expertise_organ", LongType.INSTANCE)
			.addScalar("expertise_organST", StringType.INSTANCE)
			.addScalar("plan_number", StringType.INSTANCE)
			.addScalar("place1", LongType.INSTANCE)
			.addScalar("place1ST", StringType.INSTANCE)
			.addScalar("expertise_dateST", StringType.INSTANCE)
			.addScalar("dept_id1", LongType.INSTANCE)
			.addScalar("dept_id1ST", StringType.INSTANCE)
			.addScalar("dept_other1", StringType.INSTANCE)
			.addScalar("the_bases1", StringType.INSTANCE)
			.addScalar("the_purpose1", StringType.INSTANCE)
			.addScalar("place_and_time1", StringType.INSTANCE)
			.addScalar("test_content", StringType.INSTANCE)
			.addScalar("the_participants", StringType.INSTANCE)
			.addScalar("organization_perform", StringType.INSTANCE)
			.addScalar("master_id", StringType.INSTANCE)
			.addScalar("file", FileType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(EvaluatePlan6DTO.class));
		query.setParameter("gid", id);
		EvaluatePlan6DTO item = (EvaluatePlan6DTO) query.uniqueResult();
		return item;
	}

	//delete
	@Transactional
	public ServiceResult deleteList(List<Long> listIds) {
		ServiceResult result = new ServiceResult();
		Query q = getSession().createQuery("DELETE FROM EvaluatePlan6BO WHERE gid IN (:listIds)");
		q.setParameterList("listIds", listIds);
		try {
			q.executeUpdate();
		} catch (ConstraintViolationException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(ConstraintViolationException.class.getSimpleName());
			result.setConstraintName(e.getConstraintName());
		} catch (JDBCConnectionException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(JDBCConnectionException.class.getSimpleName());
			}
		return result;
	}

	//update
	@Transactional
	public ServiceResult updateObj(EvaluatePlan6DTO dto) {
		ServiceResult result = new ServiceResult();
		EvaluatePlan6BO bo = dto.toModel();
		try {
			getSession().merge(bo);
		} catch (ConstraintViolationException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(ConstraintViolationException.class.getSimpleName());
			result.setConstraintName(e.getConstraintName());
		} catch (JDBCConnectionException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(JDBCConnectionException.class.getSimpleName());
		} catch (HibernateException e) {
			log.error(e);
			result.setError(e.getMessage());
		}
		return result;
	}

	@Transactional
	public EvaluatePlan6BO addDTO(EvaluatePlan6DTO dto) {
		ServiceResult result = new ServiceResult();
		Session session1 = getSession();
		EvaluatePlan6BO BO = new EvaluatePlan6BO();
		try {
			BO = (EvaluatePlan6BO) session1.merge(dto.toModel());
		} catch (JDBCConnectionException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(JDBCConnectionException.class.getSimpleName());
		} catch (ConstraintViolationException e) {
			log.error(e);
			result.setError(e.getMessage());
			result.setErrorType(ConstraintViolationException.class.getSimpleName());
			result.setConstraintName(e.getConstraintName());
		} catch (HibernateException e) {
			log.error(e);
			result.setError(e.getMessage());
		}
		return BO;
	}
}