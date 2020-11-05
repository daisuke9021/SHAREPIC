package com.sharepic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sharepic.service.UserService;

@Controller
@RequestMapping("/Sharepic/Users")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping("/top")
	public String top(Model model) {

		//セッションが残っている場合
//		if() {
//			return "home";
//		}

		return "login";
	}

	@RequestMapping("/login")
	public String login(Model model,
						@RequestParam("username") String username,
						@RequestParam("password") String password) {

		//ログイン処理
		int searchResult = userService.searchUser(username, password);
		if (searchResult == 0) {
			return "loginFailed";
		}
		//セッション開始
		//あとで実装する

		return "loginSuccess";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		return "register";
	}


}
