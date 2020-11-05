package com.sharepic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Users")
public class UserController {



	@RequestMapping("/login")
	public String login(Model model,
						@RequestParam("username") String username,
						@RequestParam("password") String password) {

		System.out.println(username);
		System.out.println(password);

		//ログイン処理


		//セッション開始

		return "loginSuccess";

	}

}
