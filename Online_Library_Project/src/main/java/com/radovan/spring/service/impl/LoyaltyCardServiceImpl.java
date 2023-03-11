package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.LoyaltyCardRequestRepository;
import com.radovan.spring.service.LoyaltyCardService;

@Service
@Transactional
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

	@Autowired
	private LoyaltyCardRequestRepository requestRepository;

	@Autowired
	private LoyaltyCardRepository cardRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public List<LoyaltyCardRequestDto> listAllCardRequests() {
		// TODO Auto-generated method stub
		List<LoyaltyCardRequestDto> returnValue = new ArrayList<LoyaltyCardRequestDto>();
		Optional<List<LoyaltyCardRequestEntity>> allRequests = Optional.ofNullable(requestRepository.findAll());
		if (!allRequests.isEmpty()) {
			for (LoyaltyCardRequestEntity request : allRequests.get()) {
				LoyaltyCardRequestDto requestDto = tempConverter.cardRequestEntityToDto(request);
				returnValue.add(requestDto);
			}
		}
		return returnValue;
	}

	@Override
	public void authorizeRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		Optional<LoyaltyCardRequestEntity> cardRequest = Optional.ofNullable(requestRepository.getById(cardRequestId));
		if (cardRequest.isPresent()) {
			LoyaltyCardEntity cardEntity = new LoyaltyCardEntity();
			cardEntity.setDiscount(0);
			cardEntity.setPoints(0);
			Optional<CustomerEntity> customerEntity = Optional.ofNullable(cardRequest.get().getCustomer());
			if (customerEntity.isPresent()) {

				CustomerEntity customerValue = customerEntity.get();
				LoyaltyCardEntity storedCard = cardRepository.save(cardEntity);

				customerValue.setLoyaltyCard(storedCard);
				CustomerEntity updatedCustomer = customerRepository.saveAndFlush(customerValue);

				storedCard.setCustomer(updatedCustomer);
				cardRepository.saveAndFlush(storedCard);

				requestRepository.deleteById(cardRequestId);
				requestRepository.flush();
			}

		}

	}

	@Override
	public void rejectRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		requestRepository.deleteById(cardRequestId);
		requestRepository.flush();
	}

	@Override
	public LoyaltyCardRequestDto addCardRequest() {
		// TODO Auto-generated method stub
		LoyaltyCardRequestDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerEntity = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerEntity.isPresent()) {
			LoyaltyCardRequestEntity cardRequestEntity = new LoyaltyCardRequestEntity();
			cardRequestEntity.setCustomer(customerEntity.get());
			LoyaltyCardRequestEntity storedRequest = requestRepository.save(cardRequestEntity);
			returnValue = tempConverter.cardRequestEntityToDto(storedRequest);
		}

		return returnValue;
	}

	@Override
	public LoyaltyCardRequestDto getRequestByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		LoyaltyCardRequestDto returnValue = null;
		Optional<LoyaltyCardRequestEntity> cardRequest = Optional
				.ofNullable(requestRepository.findByCustomerId(customerId));
		if (cardRequest.isPresent()) {
			returnValue = tempConverter.cardRequestEntityToDto(cardRequest.get());
		}

		return returnValue;
	}

	@Override
	public List<LoyaltyCardDto> listAllLoyaltyCards() {
		// TODO Auto-generated method stub
		List<LoyaltyCardDto> returnValue = new ArrayList<LoyaltyCardDto>();
		Optional<List<LoyaltyCardEntity>> allCards = Optional.ofNullable(cardRepository.findAll());
		if (!allCards.isEmpty()) {
			for (LoyaltyCardEntity card : allCards.get()) {
				LoyaltyCardDto cardDto = tempConverter.loyaltyCardEntityToDto(card);
				returnValue.add(cardDto);
			}
		}
		return returnValue;
	}

	@Override
	public LoyaltyCardDto getCardByCardId(Integer cardId) {
		// TODO Auto-generated method stub
		LoyaltyCardDto returnValue = null;
		Optional<LoyaltyCardEntity> cardEntity = Optional.ofNullable(cardRepository.getById(cardId));
		if (cardEntity.isPresent()) {
			returnValue = tempConverter.loyaltyCardEntityToDto(cardEntity.get());
		}
		return returnValue;
	}

	@Override
	public LoyaltyCardDto updateLoyaltyCard(Integer cardId,LoyaltyCardDto card) {
		// TODO Auto-generated method stub
		LoyaltyCardEntity cardEntity = tempConverter.loyaltyCardDtoToEntity(card);
		cardEntity.setLoyalityCardId(cardId);
		LoyaltyCardEntity updatedCard = cardRepository.saveAndFlush(cardEntity);
		LoyaltyCardDto returnValue = tempConverter.loyaltyCardEntityToDto(updatedCard);
		return returnValue;
	}

	@Override
	public void deleteLoyaltyCard(Integer loyaltyCardId) {
		// TODO Auto-generated method stub
		cardRepository.deleteById(loyaltyCardId);
		cardRepository.flush();
	}

}
