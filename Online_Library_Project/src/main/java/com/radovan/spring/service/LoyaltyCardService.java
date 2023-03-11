package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;

public interface LoyaltyCardService {

	List<LoyaltyCardRequestDto> listAllCardRequests();
	
	void authorizeRequest(Integer cardRequestId);
	
	void rejectRequest(Integer cardRequestId);
	
	LoyaltyCardRequestDto addCardRequest();
	
	LoyaltyCardRequestDto getRequestByCustomerId(Integer customerId);
	
	List<LoyaltyCardDto> listAllLoyaltyCards();
	
	LoyaltyCardDto getCardByCardId(Integer cardId);
	
	LoyaltyCardDto updateLoyaltyCard(Integer cardId,LoyaltyCardDto card);

	void deleteLoyaltyCard(Integer loyaltyCardId);
	
	
	
}
