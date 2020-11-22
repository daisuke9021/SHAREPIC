package com.sharepic.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sharepic.dao.PictureDao;
import com.sharepic.flow.PictureUploadFlow;
import com.sharepic.picture.Picture;
import com.sharepic.service.PictureService;
import com.sharepic.service.S3Service;
import com.sharepic.service.UserService;
import com.sharepic.util.PropertyUtils;

@Controller
@RequestMapping("/Sharepic")
public class SharepicController {

	@Autowired
	UserService userService;
	@Autowired
	PictureUploadFlow pictureUploadFlow;
	@Autowired
	PictureService pictureService;
	@Autowired
	S3Service s3Service;
	@Autowired
	PictureDao pictureDao;

	@RequestMapping("/Pictures/Home")
	public String home(Model model,
					   @CookieValue(value="sessionStatus", required=false) String sessionStatus,
					   @CookieValue(value="sessionUser", required=false) String sessionUser) {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		Map<String,List<Picture>> pictureMap = new HashMap<>();
		List <String> topicList = new ArrayList<>();

		try {
			pictureMap = pictureService.getHomePictures();
			topicList = pictureService.getFilteredTopicList();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("写真が取得できませんでした");
		}

		for(Map.Entry<String,List<Picture>> entry : pictureMap.entrySet()) {
			System.out.println("トピック名：" + entry.getKey());
			for(Picture picture : entry.getValue()) {
				System.out.println(picture);
			}
 		}

		for(String topic : topicList) {
			System.out.println("トピック名：" + topic);
 		}

		model.addAttribute("topicList", topicList);
		model.addAttribute("pictureList", pictureMap);

		return "home";
	}

	@RequestMapping("/Pictures/Topic")
	public String topic(Model model,
						@CookieValue(value="sessionStatus", required=false) String sessionStatus,
						@CookieValue(value="sessionUser", required=false) String sessionUser,
						@RequestParam("topic") String topic) {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		//トピックに紐づく写真を取得する
		List<Picture> pictureList = new ArrayList<>();
		try {
			pictureList = pictureService.getPictureByTopic(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("topic", topic);
		model.addAttribute("pictureList", pictureList);

		return "topic";
	}

	@RequestMapping("/Pictures/Mypage")
	public String mypage(Model model,
						 @CookieValue(value="sessionStatus", required=false) String sessionStatus,
						 @CookieValue(value="sessionUser", required=false) String sessionUser) {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		//投稿者に紐づく写真を取得する
		List<Picture> pictureList = new ArrayList<>();
		//あとでここ修正する
		//String poster = セッションからユーザ名を取得する
		try {
			pictureList = pictureService.getPictureByPoster(sessionUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("poster", sessionUser);
		model.addAttribute("pictureList", pictureList);

		return "mypage";
	}

	@RequestMapping("/Pictures/Picture")
	public String picture(Model model,
						  @CookieValue(value="sessionStatus", required=false) String sessionStatus,
						  @CookieValue(value="sessionUser", required=false) String sessionUser,
						  @RequestParam("objectUrl") String objectUrl,
						  @RequestParam("poster") String poster,
						  @RequestParam("topic") String topic,
						  @RequestParam("caption") String caption
						  ) {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		Picture picture = new Picture();
		picture.setObjectUrl(objectUrl);
		picture.setPoster(poster);
		picture.setTopic(topic);
		picture.setCaption(caption);

		model.addAttribute("picture", picture);

		return "picture";
	}

	@RequestMapping("/Pictures/Upload")
	public String upload(Model model,
						 @CookieValue(value="sessionStatus", required=false) String sessionStatus,
						 @CookieValue(value="sessionUser", required=false) String sessionUser) {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		//トピックリストを取得する
		List<String> topicList = new ArrayList<>();

		try {
			topicList = pictureService.getFilteredTopicList();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("写真が取得できませんでした");
		}
		model.addAttribute("topicList", topicList);

		return "upload";
	}

	@RequestMapping(value = "/Pictures/UploadPicture", method = RequestMethod.POST)
	public String uploadPicture(Model model,
								@CookieValue(value="sessionStatus", required=false) String sessionStatus,
								@CookieValue(value="sessionUser", required=false) String sessionUser,
								@RequestParam("picture") MultipartFile multipartFile,
								@RequestParam("fileType") String fileType,
								@RequestParam("topic") String topic,
								@RequestParam("caption") String caption
								) throws Exception {

		//セッションがなければログイン画面へ遷移
		if(!checkSessionStatus(sessionStatus,sessionUser)) {
			return "login";
		}

		//所定の場所にファイルを取り込む
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String uploadTime = sdf.format(new Date());
		String fileName = sessionUser + "_" + uploadTime + "." + fileType;
		String workspace = PropertyUtils.getProperties("application").getString("upload_file_workspace");
		Path tmpFile = Paths.get(workspace + fileName);
		try {
			Files.write(tmpFile, multipartFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("一時ファイル作成に失敗しました。");
			return "uploadFailed";
		}

		//取り込んだファイルをS3にアップロード＆NoSQL(PICTURE_STORE)に登録する
		Picture picture = new Picture(tmpFile.toString(),sessionUser,topic,caption);
		boolean s3UploadResult = pictureUploadFlow.uploadPicture(picture);

		//取り組んだファイルを削除する
		Files.delete(tmpFile);

		//アップロードに失敗した場合、投稿失敗ページ遷移
		if(!s3UploadResult) {
			return "uploadFailed";
		}

		return "uploadSuccess";
	}

	@RequestMapping("/Users/top")
	public String top(Model model,
					  @CookieValue(value="sessionStatus", required=false) String sessionStatus,
					  @CookieValue(value="sessionUser", required=false) String sessionUser) {

		//セッションが残っている場合
		if(checkSessionStatus(sessionStatus,sessionUser)) {
			Map<String,List<Picture>> pictureMap = new HashMap<>();
			List<String> topicList = new ArrayList<>();

			try {
				pictureMap = pictureService.getHomePictures();
				topicList = pictureService.getFilteredTopicList();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("写真が取得できませんでした");
			}

			for(Map.Entry<String,List<Picture>> entry : pictureMap.entrySet()) {
				System.out.println("トピック名：" + entry.getKey());
				for(Picture picture : entry.getValue()) {
					System.out.println(picture);
				}
	 		}

			for(String topic : topicList) {
				System.out.println(topic);
			}

			model.addAttribute("topicList", topicList);
			model.addAttribute("pictureList", pictureMap);
			return "home";
		}

		return "login";
	}

	@RequestMapping("/Users/login")
	public String login(Model model,
						HttpServletResponse response,
						@RequestParam("username") String username,
						@RequestParam("password") String password) {

		//ログイン処理
		System.out.println("ログイン処理｜ユーザ名：" + username);
		int searchResult = userService.searchUser(username, password);
		if (searchResult == 0) {
			return "loginFailed";
		}

		//ログイン時のCookie格納処理
		setLoginCookie(response, username);

		return"loginSuccess";
	}

	@RequestMapping("/Users/logout")
	public String logout(Model model,
						 HttpServletResponse response) {

		//ログアウト状態を示す値をCookieにセットする
		setLogoutCookie(response);

		return "logout";
	}


	@RequestMapping("/Users/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/Users/registerExec")
	public String registerExec(Model model,
							   HttpServletResponse response,
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

		//ログイン時のCookie格納処理
		setLoginCookie(response, username);

		return "registerSuccess";
	}

	/**
	 * ログイン状態の確認
	 * @param status
	 * @param username
	 * @return
	 */
	private boolean checkSessionStatus(String status, String username) {
		System.out.println("KEY：status｜｜VALUE：" + status);
		System.out.println("KEY：username｜｜VALUE：" + username);

		boolean loginStatus = status != null && status.equals("ON");
		boolean hasUsername = username != null && !username.isEmpty();
		return loginStatus && hasUsername;
	}

	/**
	 * ログイン時にCookieをセットする処理
	 * @param response
	 */
	private void setLoginCookie(HttpServletResponse response, String username) {
		//Cookieをセットする
		System.out.println("Cookieをセットします。");
		Cookie statusCookie = new Cookie("sessionStatus","ON");
		Cookie usernameCookie = new Cookie("sessionUser",username);
		statusCookie.setPath("/");
		usernameCookie.setPath("/");
		response.addCookie(statusCookie);
		response.addCookie(usernameCookie);
	}

	/**
	 * ログアウト時にCookieをセットする処理
	 * @param response
	 */
	private void setLogoutCookie(HttpServletResponse response) {
		//Cookieをセットする
		System.out.println("Cookieをセットします。");
		Cookie statusCookie = new Cookie("sessionStatus","OFF");
		Cookie usernameCookie = new Cookie("sessionUser","logoutUser");
		statusCookie.setPath("/");
		usernameCookie.setPath("/");
		response.addCookie(statusCookie);
		response.addCookie(usernameCookie);
	}

}
