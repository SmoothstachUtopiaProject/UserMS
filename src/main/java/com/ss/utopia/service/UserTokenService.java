package com.ss.utopia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.repository.UserTokenRepository;

@Service
public class UserTokenService {
	
	@Autowired
	UserTokenRepository userTokenRepository;

}
