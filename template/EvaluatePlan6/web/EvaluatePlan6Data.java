package com.tav.web.data;

import com.tav.web.bo.ServiceResult;
import com.tav.web.common.Config;
import com.tav.web.common.RestRequest;
import com.tav.web.dto.EvaluatePlan6DTO;
import com.tav.web.dto.SearchCommonFinalDTO;
import com.tav.web.dto.ObjectCommonSearchDTO;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvaluatePlan6Data {
	protected static final Logger logger = Logger.getLogger(EvaluatePlan6Data.class);
	private static final String subUrl = "/evaluatePlan6RsServiceRest";

	@Autowired
	private Config config;

	// get all
	public List<EvaluatePlan6DTO> getAll(SearchCommonFinalDTO searchDTO, Integer offset, Integer limit) {
		String url = config.getRestURL() + subUrl + "/getAll/" + offset + "/" + limit;
		try {
			List<EvaluatePlan6DTO> jsResult = RestRequest.postAndReturnObjectArray(url, searchDTO, EvaluatePlan6DTO.class);
			if (jsResult == null) {
				return null;
			} else {
				return jsResult;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	//get count
	public Integer getCount(SearchCommonFinalDTO searchDTO) {
		String url = config.getRestURL() + subUrl + "/getCount";
		return (Integer) RestRequest.postAndReturnObject(url, searchDTO, Integer.class);
	}

	public ServiceResult addObj(EvaluatePlan6DTO cbChaDTO) {
		String url = config.getRestURL() + subUrl + "/addDTO/";
		ServiceResult result = (ServiceResult) RestRequest.postAndReturnObject(url, cbChaDTO, ServiceResult.class);
		return result;
	}
	public ServiceResult updateBO(EvaluatePlan6DTO cbChaDTO) {
		String url = config.getRestURL() + subUrl + "/updateBO/";
		ServiceResult result = (ServiceResult) RestRequest.postAndReturnObject(url, cbChaDTO, ServiceResult.class);
		return result;
	}

	public ServiceResult deleteObj(ObjectCommonSearchDTO objectCommonSearchDTO) {
		String url = config.getRestURL() + subUrl + "/deleteList/";
		ServiceResult result = (ServiceResult) RestRequest.postAndReturnObject(url, objectCommonSearchDTO, ServiceResult.class);
		return result;
	}

	public EvaluatePlan6DTO getOneById(Long id) {
		String url = config.getRestURL() + subUrl + "/getOneById/" + id;
		EvaluatePlan6DTO item = (EvaluatePlan6DTO) RestRequest.getObject(url, EvaluatePlan6DTO.class);
		return item;
	}


}