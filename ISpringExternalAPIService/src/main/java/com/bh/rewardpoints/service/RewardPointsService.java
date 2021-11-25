package com.bh.rewardpoints.service;

import java.util.List;

import com.bh.rewardpoints.exception.UserNotFoundException;
import com.bh.rewardpoints.model.UserResponse;

public interface RewardPointsService {

	public List<UserResponse> getAllUsers();
	public UserResponse findUserByUserId(String userId);
	public UserResponse withdrawalPoints(String userId, Long withdrawal) throws UserNotFoundException;
}
