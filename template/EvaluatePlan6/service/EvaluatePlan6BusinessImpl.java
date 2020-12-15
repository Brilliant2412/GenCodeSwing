package com.tav.service.business;

import com.tav.service.base.db.business.BaseFWBusinessImpl;
import com.tav.service.bo.EvaluatePlan6BO;
import com.tav.service.common.Constants;
import com.tav.service.dao.EvaluatePlan6DAO;
import com.tav.service.dao.ObjectReationDAO;
import com.tav.service.dto.EvaluatePlan6DTO;
import com.tav.service.dto.ObjectCommonSearchDTO;
import com.tav.service.dto.SearchCommonFinalDTO;
import com.tav.service.dto.ObjectSearchDTO;
import com.tav.service.dto.ServiceResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("evaluatePlan6BusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EvaluatePlan6BusinessImpl extends
		BaseFWBusinessImpl<EvaluatePlan6DAO, EvaluatePlan6DTO, EvaluatePlan6BO> implements EvaluatePlan6Business {

	@Autowired
	private EvaluatePlan6DAO evaluatePlan6DAO;

	@Override
	public EvaluatePlan6DAO gettDAO() { return evaluatePlan6DAO; }

	public List<EvaluatePlan6DTO> getAll(SearchCommonFinalDTO searchDTOTmp, Integer offset, Integer limit) {
		List<EvaluatePlan6DTO> lstDTO = evaluatePlan6DAO.getAll(searchDTOTmp, offset, limit);
		return lstDTO;
	}

	public Integer getCount(SearchCommonFinalDTO searchDTO) { return evaluatePlan6DAO.getCount(searchDTO); }

	//GET ONE
	public EvaluatePlan6DTO getOneObjById(Long gid) {
		EvaluatePlan6DTO dto = evaluatePlan6DAO.getOneObjById(gid);
		return dto;
	}

	//add
	public ServiceResult addDTO(EvaluatePlan6DTO evaluatePlan6DTO) {
		EvaluatePlan6BO bo = evaluatePlan6DAO.addDTO(evaluatePlan6DTO);
		ServiceResult serviceResult = new ServiceResult();
		serviceResult.setId(bo.getGid());
		return serviceResult;
	}

	//update
	public ServiceResult updateObj(EvaluatePlan6DTO evaluatePlan6DTO) {
		ServiceResult result;
		EvaluatePlan6BO bo = evaluatePlan6DAO.addDTO(evaluatePlan6DTO);
		result = new ServiceResult();
		return result;
	}

	//delete
	public ServiceResult deleteList(ObjectCommonSearchDTO searchDTO) {
		ServiceResult result = evaluatePlan6DAO.deleteList(searchDTO.getLstFirst());
		return result;
	}

}