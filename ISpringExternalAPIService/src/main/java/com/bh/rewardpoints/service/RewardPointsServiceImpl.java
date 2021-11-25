package com.bh.rewardpoints.service;

import java.util.ArrayList;
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
import org.springframework.http.ResponseEntity;
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
	public List<UserResponse> getAllUsers() {
		logger.info("getAllUsers....");
		List<UserResponse> usersList = new ArrayList<>();
		ResponseEntity<?> responseEntity = restTemplate.getForEntity(rwpointsApiBaseUrl + "/rewardpoints/users", List.class);
		if(responseEntity != null) {
			usersList = (List<UserResponse>) responseEntity.getBody();
		}
		return usersList; 
	}
	@Override
	public UserResponse findUserByUserId(String userId) {	
		logger.info("find User By Id {} : ", userId);
		ResponseEntity<?> responseEntity = restTemplate.getForEntity(rwpointsApiBaseUrl + "/rewardpoints/users/"+userId, UserResponse.class);
		if(responseEntity != null) {
		 return (UserResponse) responseEntity.getBody();
		}
		return null;
	}
	@Override
	public UserResponse withdrawalPoints(String userId, Long withdrawal) throws UserNotFoundException {

		HttpHeaders headers = new HttpHeaders();
		Map<String, String> map = new HashMap<>();
		map.put("id", userId);
		map.put("withdrawal", String.valueOf(withdrawal));
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<UserResponse> responseEntity = restTemplate.postForEntity(rwpointsApiBaseUrl + "/rewardpoints/points/{id}/{withdrawal}", requestEntity, UserResponse.class, map);
		if(responseEntity != null) {
			 return (UserResponse) responseEntity.getBody();
			}
		return null;
	}
}
