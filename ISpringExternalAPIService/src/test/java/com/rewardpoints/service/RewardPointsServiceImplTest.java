package com.rewardpoints.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bh.rewardpoints.exception.UserNotFoundException;
import com.bh.rewardpoints.model.UserResponse;
import com.bh.rewardpoints.service.RewardPointsServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class RewardPointsServiceImplTest {

	@InjectMocks
	private RewardPointsServiceImpl rewardPointsService;

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void getAllUsersTest() {

		List<UserResponse> usersList = new ArrayList<>(); 
		UserResponse user = new UserResponse();
		user.setEmail("IL10@infolab.com");
		user.setBhEntity("BHEntity1");
		user.setBalance(120L);
		usersList.add(user);		
		UserResponse secondUser = new UserResponse();
		secondUser.setEmail("IL11@infolab.com");
		secondUser.setBhEntity("BHEntity2");
		secondUser.setBalance(200L);
		usersList.add(secondUser);	
		ResponseEntity responseEntity = ResponseEntity.ok(usersList);
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
		List<UserResponse> usersListResp = rewardPointsService.getAllUsers();
		Assert.assertNotNull(usersListResp);
		Assert.assertEquals(usersList.get(0).getBhEntity(), usersListResp.get(0).getBhEntity());
		Assert.assertEquals(usersList.get(0).getBalance(), usersListResp.get(0).getBalance());
		Assert.assertEquals(usersList.get(0).getEmail(), usersListResp.get(0).getEmail());
		Assert.assertEquals(usersList.get(1).getBhEntity(), usersListResp.get(1).getBhEntity());
		Assert.assertEquals(usersList.get(1).getBalance(), usersListResp.get(1).getBalance());
		Assert.assertEquals(usersList.get(1).getEmail(), usersListResp.get(1).getEmail());
	}

	@Test
	public void findUserByUserIdTest() {
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail("IL10@infolab.com");
		userResponse.setBhEntity("BHEntity1");
		userResponse.setBalance(120L);
		ResponseEntity responseEntity = ResponseEntity.ok(userResponse);
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
		UserResponse resp = rewardPointsService.findUserByUserId("IL10");
		Assert.assertNotNull(resp);
		Assert.assertEquals(userResponse.getBhEntity(), resp.getBhEntity());
		Assert.assertEquals(userResponse.getBalance(), resp.getBalance());
		Assert.assertEquals(userResponse.getEmail(), resp.getEmail());
	}
	
	@Test
	public void withdrawalPointsTest() throws UserNotFoundException {
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail("IL10@infolab.com");
		userResponse.setBhEntity("BHEntity1");
		userResponse.setBalance(120L);
		ResponseEntity responseEntity = ResponseEntity.ok(userResponse);
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(),  Mockito.anyMap())).thenReturn(responseEntity);
		UserResponse userResp = rewardPointsService.withdrawalPoints("IL10", 10L);
		Assert.assertNotNull(userResp);
		Assert.assertEquals(userResponse.getBalance(), userResp.getBalance());
	}

}
