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
		System.out.println("ログイン処理｜ユーザ名：" + username);
		int searchResult = userService.searchUser(username, password);
		if (searchResult == 0) {
			return "loginFailed";
		}
		//セッション開始
		//あとで実装する

		return "loginSuccess";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {

		//セッション切断
		//あとで切断する

		return "logout";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/registerExec")
	public String registerExec(Model model,
			@RequestParam("username") String username,
			@RequestParam("password1") String password) {

		//ユーザ名が使われていたら"usernameAlreadyUsed"に画面遷移
		int searchResult = userService.searchUser(username, password);
		if (searchResult > 0) {
			return "userAlreadyUsed";
		}

		//登録処理結果によって遷移先を変える
		int insertResult = userService.registerUser(username, password);
		if (insertResult == 0) {
			return "registerFailed";
		}

		return "registerSuccess";
	}


}
