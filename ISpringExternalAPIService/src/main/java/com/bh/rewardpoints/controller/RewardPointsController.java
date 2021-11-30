package com.bh.rewardpoints.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.rewardpoints.exception.UserNotFoundException;
import com.bh.rewardpoints.model.UserResponse;
import com.bh.rewardpoints.service.RewardPointsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="User Reward Points External API", description = "External Api for ISpring")
@RequestMapping("/external")
@CrossOrigin
public class RewardPointsController {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    @Inject
    private RewardPointsService rewardPointsService;
    
    @Operation(summary="getAllUsers",description="External API to fetch all user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all ok"),
            @ApiResponse(responseCode = "400", description = "bad request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Permission denied")
    })
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() throws UserNotFoundException {   
    	List<UserResponse> usersList = rewardPointsService.getAllUsers();
    	logger.info("All Users :{} ", usersList);
        return ResponseEntity.ok(usersList);
    }
    @Operation(summary="getUserbyUserId",description="External API to fetch user by User Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all ok"),
            @ApiResponse(responseCode = "400", description = "bad request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Permission denied")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserbyUserId(@PathVariable("id") String userId) throws UserNotFoundException {  
    	logger.info("getUserbyUserId : {}", userId);
    	UserResponse userResponse = rewardPointsService.findUserByUserId(userId);
        return ResponseEntity.ok(userResponse);
    }  
    @Operation(summary="withdrawPoints",description="API to withdraw points from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all ok"),
            @ApiResponse(responseCode = "400", description = "bad request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Permission denied")
    })
    @PostMapping("/points/{id}/{withdrawal}")
    public ResponseEntity<?> withdrawPoints(@PathVariable("id") String userId, @PathVariable("withdrawal") String withdrawal) throws UserNotFoundException, NumberFormatException{   
    	logger.info("User {} trying to withdraw reward points {}", userId, withdrawal);
    	UserResponse user = rewardPointsService.withdrawalPoints(userId, Long.valueOf(withdrawal));  
        return ResponseEntity.ok(user);
    } 
}
