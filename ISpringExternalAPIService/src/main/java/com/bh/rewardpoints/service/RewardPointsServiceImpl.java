package com.bh.rewardpoints.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.bh.rewardpoints.exception.UserNotFoundException;
import com.bh.rewardpoints.model.UserResponse;

@Named
public class RewardPointsServiceImpl implements RewardPointsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	private RestTemplate restTemplate;

	@Value("${rwpoints.api.service.baseurl}")
	private String rwpointsApiBaseUrl;

	@Override
	@SuppressWarnings("unchecked")
	public List<UserResponse> getAllUsers() throws UserNotFoundException {
		logger.info("getAllUsers....");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<?> responseEntity = restTemplate.exchange(rwpointsApiBaseUrl + "/rewardpoints/users", HttpMethod.GET, entity, List.class);
		if(responseEntity == null || responseEntity.getBody() == null) {
    		throw new UserNotFoundException("No Users Found in database");
    	}
		return (List<UserResponse>) responseEntity.getBody();
	}
	@Override
	public UserResponse findUserByUserId(String userId) throws UserNotFoundException {	
		logger.info("find User By Id {} : ", userId);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		ResponseEntity<?> responseEntity = restTemplate.exchange(rwpointsApiBaseUrl + "/rewardpoints/users/"+userId, HttpMethod.GET, entity, UserResponse.class);
		if(responseEntity == null || responseEntity.getBody() == null) {
			logger.info("User is not availale for the userid : {}", userId);
			throw new UserNotFoundException(String.format("User for userId %s not found", userId));
		}
		return (UserResponse) responseEntity.getBody();
	}
	@Override
	public UserResponse withdrawalPoints(String userId, Long withdrawal) throws UserNotFoundException {

		HttpHeaders headers = new HttpHeaders();
		Map<String, String> map = new HashMap<>();
		map.put("id", userId);
		map.put("withdrawal", String.valueOf(withdrawal));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(rwpointsApiBaseUrl + "/rewardpoints/points/{id}/{withdrawal}",  HttpMethod.POST, requestEntity, UserResponse.class, map);
		if(responseEntity == null  || responseEntity.getBody() == null) {
			logger.info("User is not availale for the userid : {}", userId);
    		throw new UserNotFoundException(String.format("User for userId %s not found", userId));
		}
		return  (UserResponse) responseEntity.getBody();
	}
}
