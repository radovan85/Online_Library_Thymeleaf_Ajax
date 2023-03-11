package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.WishListService;

@Service
@Transactional
public class WishListServiceImpl implements WishListService{
	
	@Autowired
	private WishListRepository wishListRepository;


	@Override
	public void deleteWishList(Integer wishListId) {
		// TODO Auto-generated method stub
		wishListRepository.clearWishList(wishListId);
		wishListRepository.deleteById(wishListId);
		wishListRepository.flush();
	}

}
