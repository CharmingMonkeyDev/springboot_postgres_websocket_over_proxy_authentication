package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	@Autowired
	IUserService userService;

	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

	//@PreAuthorize("@roleChecker.hasValidRole(#principal)")
	@RequestMapping(value = "/customer/me",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> user(Principal principal) throws UnsupportedEncodingException {
		System.out.println(principal);
		User user = userService.getUserFromJwt(SecurityUtils.getDecodedJwt(principal));
		DynamicBean jsonObject = new DynamicBean();
		jsonObject.addProperty("messages",
				String.format("Welcome again ``%s``. And Happy nice day!", principal.getName()));
		jsonObject.addProperty("prefix",user.getPrefixPhoneNumber());
		jsonObject.addProperty("phone",user.getPhoneNumber());
		return ResponseEntity.ok(jsonObject);
	}

	//getotp
	//call outh/otp/token
	//if ok send otp
	//not ok return message

	//verify outh/otp/tokenverify
	//if ok return token
	//else return message

}
