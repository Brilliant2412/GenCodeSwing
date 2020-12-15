package com.tav.service.dao;

import com.tav.service.base.db.dao.BaseFWDAOImpl;
import com.tav.service.bo.EvaluatePlan1BO;
import com.tav.service.dto.EvaluatePlan1DTO;
import com.tav.service.dto.ObjectCommonSearchDTO;
import com.tav.service.dto.SearchCommonFinalDTO;
import com.tav.service.dto.ServiceResult;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;import org.hibernate.Criteria;
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

@Repository("evaluatePlan1DAO")
public class EvaluatePlan1DAO extends BaseFWDAOImpl<EvaluatePlan1BO, Long>{
    
    public List<EvaluatePlan1DTO> getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {
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
		sqlCommand.append("tbl.dept_id as dept_id, ");
		sqlCommand.append("d1.dept_name as dept_idST, ");
		sqlCommand.append("tbl.dept_other as dept_other, ");
		sqlCommand.append("tbl.the_bases as the_bases, ");
		sqlCommand.append("tbl.the_purpose as the_purpose, ");
		sqlCommand.append("tbl.place_and_time as place_and_time, ");
		sqlCommand.append("tbl.test_content as test_content, ");
		sqlCommand.append("tbl.the_participants as the_participants, ");
		sqlCommand.append("tbl.organization_perform as organization_perform, ");
		sqlCommand.append("tbl.master_id as master_id ");

		sqlCommand.append(" FROM evaluate_plan1 tbl ");
		sqlCommand.append(" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'");
		sqlCommand.append(" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '705'");
		sqlCommand.append(" left join department d1 on d1.dept_id = tbl.dept_id");

		sqlCommand.append(" WHERE 1=1 ");
	//String
 	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
            sqlCommand.append(" and (   ");
	    sqlCommand.append("  LOWER(tbl.plan_number) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.dept_other) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.the_participants) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.organization_perform) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.master_id) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append(" )   ");
        }

	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            sqlCommand.append(" and tbl.expertise_organ in (:listLong1) ");
        }

	if (!StringUtil.isEmpty(searchDTO.getDouble1())) {
		sqlCommand.append(" and tbl.right_long >= (:double1) ");
	}
	if (!StringUtil.isEmpty(searchDTO.getDouble2())) {
		sqlCommand.append(" and tbl.right_long <= (:double2) ");
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
			.addScalar("dept_id", LongType.INSTANCE)
			.addScalar("dept_idST", StringType.INSTANCE)
			.addScalar("dept_other", StringType.INSTANCE)
			.addScalar("the_bases", StringType.INSTANCE)
			.addScalar("the_purpose", StringType.INSTANCE)
			.addScalar("place_and_time", StringType.INSTANCE)
			.addScalar("test_content", StringType.INSTANCE)
			.addScalar("the_participants", StringType.INSTANCE)
			.addScalar("organization_perform", StringType.INSTANCE)
			.addScalar("master_id", StringType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(EvaluatePlan1DTO.class))
			.setFirstResult(offset);
		if (limit != null && limit != 0) {
			query.setMaxResults(limit);
		}
	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
		q.setParameter("stringKeyWord", "%" + searchDTO.getStringKeyWord() + "%");
	}
	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            q.setParameterList("listLong1", searchDTO.getListLong1());
        }
	if (!StringUtil.isEmpty(searchDTO.getDouble1())) {
		q.setParameter("double1", searchDTO.getDouble1());
	}
	if (!StringUtil.isEmpty(searchDTO.getDouble2())) {
		q.setParameter("double2", searchDTO.getDouble2());
	}
	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            try {
                q.setParameter("string1", formatter.parse(searchDTO.getString1() + " 00:00:00"));
                q.setParameter("string2", formatter.parse(searchDTO.getString2() + " 23:59:59"));
            } catch (ParseException ex) {
            }
        }
		return query.list();
	}

public Integer getCount(ObjectCommonSearchDTO searchDTO) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append(" SELECT ");
        sqlCommand.append(" COUNT(1)");
        sqlCommand.append(" FROM  evaluate_plan1 tbl ");
        sqlCommand.append(" WHERE 1=1 ");
	//String
 	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
            sqlCommand.append(" and (   ");
	    sqlCommand.append("  OR LOWER(tbl.plan_number) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.dept_other) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.the_participants) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.organization_perform) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append("  OR LOWER(tbl.master_id) like LOWER(:stringKeyWord)   ");
	    sqlCommand.append(" )   ");
        }

	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            sqlCommand.append(" and tbl.expertise_organ in (:listLong1) ");
        }

	if (!StringUtil.isEmpty(searchDTO.getDouble1())) {
		sqlCommand.append(" and tbl.right_long >= (:double1) ");
	}
	if (!StringUtil.isEmpty(searchDTO.getDouble2())) {
		sqlCommand.append(" and tbl.right_long <= (:double2) ");
	}
	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (objectSearchCommonDTO.getString2() != null && !objectSearchCommonDTO.getString2().isEmpty())  ) {
            sqlCommand.append("  and ( tbl.expertise_date between (:string1) and (:string2)    ) ");
        }
        Query query = getSession().createSQLQuery(sqlCommand.toString());
	if (!StringUtil.isEmpty(searchDTO.getStringKeyWord())) {
		q.setParameter("stringKeyWord", "%" + searchDTO.getStringKeyWord() + "%");
	}
	if (searchDTO.getListLong1() != null && !searchDTO.getListLong1().isEmpty()) {
            q.setParameterList("listLong1", searchDTO.getListLong1());
        }
	if (!StringUtil.isEmpty(searchDTO.getDouble1())) {
		q.setParameter("double1", searchDTO.getDouble1());
	}
	if (!StringUtil.isEmpty(searchDTO.getDouble2())) {
		q.setParameter("double2", searchDTO.getDouble2());
	}
	if (  (searchDTO.getString1() != null && !searchDTO.getString1().isEmpty())   &&   (searchDTO.getString2() != null && !searchDTO.getString2().isEmpty())  ) {
            try {
                q.setParameter("string1", formatter.parse(searchDTO.getString1() + " 00:00:00"));
                q.setParameter("string2", formatter.parse(searchDTO.getString2() + " 23:59:59"));
            } catch (ParseException ex) {
            }
        }
        return ((BigInteger) query.uniqueResult()).intValue();




    }	//get one
	public EvaluatePlan1DTO getOneObjById(Long id) {
		StringBuilder sqlCommand = new StringBuilder();
		sqlCommand.append(" SELECT ");
		sqlCommand.append("tbl.gid as gid, ");
		sqlCommand.append("tbl.expertise_organ as expertise_organ, ");
		sqlCommand.append("mst1.dvs_name as expertise_organST, ");
		sqlCommand.append("tbl.plan_number as plan_number, ");
		sqlCommand.append("tbl.place1 as place1, ");
		sqlCommand.append("mst2.dvs_name as place1ST, ");
		sqlCommand.append("to_char(tbl.expertise_date, 'DD/MM/YYYY') as expertise_dateST, ");
		sqlCommand.append("tbl.dept_id as dept_id, ");
		sqlCommand.append("d1.dept_name as dept_idST, ");
		sqlCommand.append("tbl.dept_other as dept_other, ");
		sqlCommand.append("tbl.the_bases as the_bases, ");
		sqlCommand.append("tbl.the_purpose as the_purpose, ");
		sqlCommand.append("tbl.place_and_time as place_and_time, ");
		sqlCommand.append("tbl.test_content as test_content, ");
		sqlCommand.append("tbl.the_participants as the_participants, ");
		sqlCommand.append("tbl.organization_perform as organization_perform, ");
		sqlCommand.append("tbl.master_id as master_id ");

		sqlCommand.append(" FROM evaluate_plan1 tbl ");
		sqlCommand.append(" left join mst_division mst1 on mst1.dvs_value = tbl.expertise_organ AND mst1.dvs_group_cd = '701'");
		sqlCommand.append(" left join mst_division mst2 on mst2.dvs_value = tbl.place1 AND mst2.dvs_group_cd = '705'");
		sqlCommand.append(" left join department d1 on d1.dept_id = tbl.dept_id");
		sqlCommand.append(" WHERE tbl.gid = :gid");
		Query query = getSession().createSQLQuery(sqlCommand.toString())
			.addScalar("gid", LongType.INSTANCE)
			.addScalar("expertise_organ", LongType.INSTANCE)
			.addScalar("expertise_organST", StringType.INSTANCE)
			.addScalar("plan_number", StringType.INSTANCE)
			.addScalar("place1", LongType.INSTANCE)
			.addScalar("place1ST", StringType.INSTANCE)
			.addScalar("expertise_dateST", StringType.INSTANCE)
			.addScalar("dept_id", LongType.INSTANCE)
			.addScalar("dept_idST", StringType.INSTANCE)
			.addScalar("dept_other", StringType.INSTANCE)
			.addScalar("the_bases", StringType.INSTANCE)
			.addScalar("the_purpose", StringType.INSTANCE)
			.addScalar("place_and_time", StringType.INSTANCE)
			.addScalar("test_content", StringType.INSTANCE)
			.addScalar("the_participants", StringType.INSTANCE)
			.addScalar("organization_perform", StringType.INSTANCE)
			.addScalar("master_id", StringType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(EvaluatePlan1DTO.class));
		query.setParameter("gid", id);
		EvaluatePlan1DTO item = (EvaluatePlan1DTO) query.uniqueResult();
		return item;
	}

	//delete
	@Transactional
	public ServiceResult deleteList(List<Long> listIds) {
		ServiceResult result = new ServiceResult();
		Query q = getSession().createQuery("DELETE FROM EvaluatePlan1BO WHERE gid IN (:listIds)");
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
	public ServiceResult updateObj(EvaluatePlan1DTO dto) {
		ServiceResult result = new ServiceResult();
		EvaluatePlan1BO bo = dto.toModel();
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
	public EvaluatePlan1BO addDTO(EvaluatePlan1DTO dto) {
		ServiceResult result = new ServiceResult();
		Session session1 = getSession();
		EvaluatePlan1BO BO = new EvaluatePlan1BO();
		try {
			BO = (EvaluatePlan1BO) session1.merge(dto.toModel());
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