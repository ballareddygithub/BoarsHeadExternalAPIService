package com.rewardpoints.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bh.rewardpoints.aspect.ExceptionAdvice;
import com.bh.rewardpoints.controller.RewardPointsController;
import com.bh.rewardpoints.model.UserResponse;
import com.bh.rewardpoints.service.RewardPointsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardPointsController.class)
public class RewardPointsControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private RewardPointsController rewardPointsController;

	@MockBean
	private RewardPointsService rewardPointsService;

	@Before
	public void setup() {	
		rewardPointsController = new RewardPointsController();
		this.mockMvc = MockMvcBuilders.standaloneSetup(rewardPointsController)
				 .setControllerAdvice(new ExceptionAdvice()).build();
		ReflectionTestUtils.setField(rewardPointsController, "rewardPointsService", rewardPointsService);
	}

	@Test
	public void getAllUsersTest() throws Exception {

		List<UserResponse> usersList = new ArrayList<>(); 
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail("IL10@infolab.com");
		userResponse.setBhEntity("BHEntity1");
		userResponse.setBalance(12L);
		usersList.add(userResponse);
		Mockito.when(rewardPointsService.getAllUsers()).thenReturn(usersList);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/external/users")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("[*].email").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("[*].balance").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("[*].bhEntity").exists());
	}
	@Test
	public void getUserTest() throws Exception {
		
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail("IL10@infolab.com");
		userResponse.setBhEntity("BHEntity1");
		userResponse.setBalance(12L);
		Mockito.when(rewardPointsService.findUserByUserId(Mockito.anyString())).thenReturn(userResponse);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/external/users/IL10")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("email").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("balance").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("bhEntity").exists());
	}

	@Test
	public void withdrawPointsTest() throws Exception {
		
		UserResponse user = new UserResponse();
		user.setEmail("IL10@infolab.com"); 
		user.setBhEntity("BHEntity1");
		user.setBalance(12L);		
		Mockito.when(rewardPointsService.withdrawalPoints(Mockito.anyString(), Mockito.anyLong())).thenReturn(user);  
		mockMvc.perform(MockMvcRequestBuilders
				.post("/external/points/IL10/20")
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("email").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("balance").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("bhEntity").exists());
	}
}
