package com.tav.service.rest;

import com.tav.service.business.EvaluatePlan6BusinessImpl;
import com.tav.service.dto.EvaluatePlan6DTO;
import com.tav.service.dto.SearchCommonFinalDTO;
import com.tav.service.dto.ObjectCommonSearchDTO;
import com.tav.service.dto.ServiceResult;
import java.util.List;
import java.util.Date;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

public class EvaluatePlan6RsServiceImpl implements EvaluatePlan6RsService{

	@Autowired
	private EvaluatePlan6BusinessImpl evaluatePlan6BusinessImpl;

	@Override
	public Response getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {
		List<EvaluatePlan6DTO> lst = evaluatePlan6BusinessImpl.getAll(searchDTO, offset, limit);
		if (lst == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(lst).build();
		}
	}

	@Override
	public Response getCount(SearchCommonFinalDTO searchDTO) {
		int result = evaluatePlan6BusinessImpl.getCount(searchDTO);
		return Response.ok(result).build();
	}

	@Override
	public Response getOneById(Long id) {
		EvaluatePlan6DTO result = evaluatePlan6BusinessImpl.getOneObjById(id);
		return Response.ok(result).build();
	}

	@Override
	public Response deleteList(ObjectCommonSearchDTO searchDTO) {
		ServiceResult result = evaluatePlan6BusinessImpl.deleteList(searchDTO);
		if ("FAIL".equals(result.getError())) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			return Response.ok(result).build();
		}
	}

	@Override
	public Response updateObj(EvaluatePlan6DTO evaluatePlan6DTO) {
		ServiceResult result = evaluatePlan6BusinessImpl.updateObj(evaluatePlan6DTO);
		return Response.ok(result).build();
	}

	@Override
	public Response addDTO(EvaluatePlan6DTO evaluatePlan6DTO) {
		ServiceResult result = evaluatePlan6BusinessImpl.addDTO(evaluatePlan6DTO);
		return Response.ok(result).build();
	}

}