package com.sfa.stock_management.serviceImpl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sfa.stock_management.constant.Constant;
import com.sfa.stock_management.dto.ClientDataGetRequestBody;
import com.sfa.stock_management.dto.ConfigurationResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Value("${sfa.backend.url}")
	private String sfaBackendUrl;

	@Override
	public Response<?> getAllClients(ClientDataGetRequestBody clientDataGetRequestBody) throws URISyntaxException {
		String url = sfaBackendUrl + Constant.FETCH_VENDOR_LIST_FOR_HRMS;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject request = new JsonObject();
		request.addProperty("userId", clientDataGetRequestBody.getUserId());
		request.addProperty("hrmsUserId", clientDataGetRequestBody.getHrmsUserId());
		if (clientDataGetRequestBody.getCityName() != null && clientDataGetRequestBody.getCityName() != "") {
			request.addProperty("cityName", clientDataGetRequestBody.getCityName());
		}
		if (clientDataGetRequestBody.getLocalityName() != null && clientDataGetRequestBody.getLocalityName() != "") {
			request.addProperty("localityName", clientDataGetRequestBody.getLocalityName());
		}
		Gson gson = new Gson();
		String requestBody = gson.toJson(request);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		List<ClientDataGetRequestBody> clientDataGetData = new ArrayList<>();
		if (response != null) {
			JSONObject jsnobject = new JSONObject(response.getBody());
			if (jsnobject.has("data")) {
				JSONArray clientArray = jsnobject.getJSONArray("data");
//				System.out.println(clientArray);
				for (Object obj : clientArray) {
					ClientDataGetRequestBody clientData = new ClientDataGetRequestBody();
					List<String> clientAddress = new ArrayList<>();
					JSONObject ob = new JSONObject(obj.toString());
					Integer id = ob.getInt("id");
					clientData.setId(Long.parseLong(id.toString()));
					String email = "";
					if (ob.has("emailId")) {
						email = ob.getString("emailId");
						if (email != null && email != " ") {
							clientData.setName(ob.getString("name") + "(" + email + ")");
							clientData.setEmail(email);
						}

					} else {
						clientData.setName(ob.getString("name"));
					}
					if (ob.has("address")) {
						clientData.setAddress(ob.getString("address"));
					}

					if (ob.has("stateId")) {
						JSONObject stateObj = ob.getJSONObject("stateId");
						if (stateObj.has("stateCode")) {
							clientData.setStateId(stateObj.getLong("stateCode"));
						}
					}
					if (ob.has("clientBranches")) {
						JSONArray clientBranchArray = new JSONArray(ob.get("clientBranches").toString());
						for (Object cl : clientBranchArray) {
							JSONObject obString = new JSONObject(cl.toString());
							if (obString.has("address")) {
								clientAddress.add(obString.getString("address"));
							}

						}
					}
					clientData.setClientBranches(clientAddress);
					clientDataGetData.add(clientData);
				}
			}

			return new Response<>(HttpStatus.OK.value(), "OK", clientDataGetData);
		} else {
			return new Response<>(HttpStatus.OK.value(), "No content", null);
		}

	}

	@Override
	public Response<?> getAllClientsV2(Long userId, Long hrmsUserId) {
		String url = sfaBackendUrl + Constant.FETCH_VENDOR_LIST_FOR_HRMS;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject request = new JsonObject();
		request.addProperty("userId", userId);
		request.addProperty("hrmsUserId", hrmsUserId);
		Gson gson = new Gson();
		String requestBody = gson.toJson(request);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		List<ClientDataGetRequestBody> clientDataGetData = new ArrayList<>();
		if (response != null) {
			JSONObject jsnobject = new JSONObject(response.getBody());
			if (jsnobject.has("data")) {
				JSONArray clientArray = jsnobject.getJSONArray("data");
				for (Object obj : clientArray) {
					ClientDataGetRequestBody clientData = new ClientDataGetRequestBody();
					JSONObject ob = new JSONObject(obj.toString());
					Integer id = ob.getInt("id");
					clientData.setId(Long.parseLong(id.toString()));
					String email = "";
					if (ob.has("emailId")) {
						email = ob.getString("emailId");
						if (email != null && email != " ") {
							clientData.setName(ob.getString("name") + "(" + email + ")");
							clientData.setEmail(email);
						}

					} else {
						clientData.setName(ob.getString("name"));
					}
					if (ob.has("address")) {
						clientData.setAddress(ob.getString("address"));
					}

					if (ob.has("stateId")) {
						JSONObject stateObj = ob.getJSONObject("stateId");
						if (stateObj.has("stateCode")) {
							clientData.setStateId(stateObj.getLong("stateCode"));
						}
					}
					clientDataGetData.add(clientData);
				}
			}

			return new Response<>(HttpStatus.OK.value(), "OK", clientDataGetData);
		} else {
			return new Response<>(HttpStatus.OK.value(), "No content", null);
		}
	}

	@Override
	public Response<?> getCompanyConfiguration(Long companyId) {
		try {
			String url = sfaBackendUrl + Constant.GET_COMPANY_CONFIGURATIONS;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			JsonObject request = new JsonObject();
			request.addProperty("companyId", companyId);
			Gson gson = new Gson();
			String requestBody = gson.toJson(request);
			HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
			List<ConfigurationResponseDto> configList = new ArrayList<>();
			if (response != null) {
				JSONObject jsnobject = new JSONObject(response.getBody());
				if (jsnobject.has("data")) {
					JSONArray clientArray = jsnobject.getJSONArray("data");
					for (Object obj : clientArray) {
						ConfigurationResponseDto dto = new ConfigurationResponseDto();
						JSONObject ob = new JSONObject(obj.toString());
						if (ob.has("hrmsConfigurationName")) {
							dto.setConfigurationName(ob.getString("hrmsConfigurationName"));
						}
						if (ob.has("id")) {
							dto.setConfigurationId(ob.getLong("id"));
						}
						if (ob.has("isEnable")) {
							dto.setIsEnable(ob.getBoolean("isEnable"));
						}
						configList.add(dto);
					}
				}
			}
			return new Response<>(HttpStatus.OK.value(), "Company configuration list.", configList);
		
		}catch (Exception e) {
			// TODO: handle exception
			return new Response<>(HttpStatus.OK.value(), "Company configuration list.", new ArrayList<>());
		}
	}

}
