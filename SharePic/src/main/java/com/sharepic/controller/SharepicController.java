package com.sharepic.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sharepic.flow.PictureUploadFlow;
import com.sharepic.picture.Picture;
import com.sharepic.repository.UserForSession;
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
	UserForSession session;

	@RequestMapping("/Pictures/Home")
	public String home(Model model) {

		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
			return "login";
		}

		Map<String,List<Picture>> pictureMap = new HashMap<>();
		Collection<String> topicList = null;

		try {
			pictureMap = pictureService.getHomePictures();
			topicList = pictureService.getTopicList().values();
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

	@RequestMapping("/Pictures/Topic")
	public String topic(Model model,
								@RequestParam("topic") String topic) {
		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
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
	public String mypage(Model model) {

		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
			return "login";
		}

		//投稿者に紐づく写真を取得する
		List<Picture> pictureList = new ArrayList<>();
		String poster = session.getUserName();
		//あとでここ修正する
		//String poster = セッションからユーザ名を取得する
		try {
			pictureList = pictureService.getPictureByPoster(poster);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("poster", poster);
		model.addAttribute("pictureList", pictureList);

		return "mypage";
	}

	@RequestMapping("/Pictures/Picture")
	public String picture(Model model,
								@RequestParam("objectUrl") String objectUrl,
								@RequestParam("poster") String poster,
								@RequestParam("topic") String topic,
								@RequestParam("caption") String caption
								) {
		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
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
	public String upload(Model model) {

		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
			return "login";
		}

		//トピックリストを取得する
		Collection<String> topicList = null;

		try {
			topicList = pictureService.getTopicList().values();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("写真が取得できませんでした");
		}
		model.addAttribute("topicList", topicList);

		return "upload";
	}

	@RequestMapping(value = "/Pictures/UploadPicture", method = RequestMethod.POST)
	public String uploadPicture(Model model,
								@RequestParam("picture") MultipartFile multipartFile,
								@RequestParam("fileType") String fileType,
								@RequestParam("topic") String topic,
								@RequestParam("caption") String caption
								) throws Exception {

		//セッションがなければログイン画面へ遷移
		if(session.getUserName() == null) {
			return "login";
		}

		//投稿者をセッションから取得
		String poster = session.getUserName();

		//所定の場所にファイルを取り込む
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String uploadTime = sdf.format(new Date());
		String fileName = poster + "_" + uploadTime + "." + fileType;
		String workspace = PropertyUtils.getProperties("application").getString("upload_file_workspace");
		Path tmpFile = Paths.get(workspace + fileName);
		Files.write(tmpFile, multipartFile.getBytes());

		//取り込んだファイルをS3にアップロード＆NoSQL(PICTURE_STORE)に登録する
		Picture picture = new Picture(tmpFile.toString(),poster,topic,caption);
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
	public String top(Model model) {

		//セッションが残っている場合
		if(session.getUserName() != null) {
			Map<String,List<Picture>> pictureMap = new HashMap<>();
			Collection<String> topicList = null;

			try {
				pictureMap = pictureService.getHomePictures();
				topicList = pictureService.getTopicList().values();
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
						@RequestParam("username") String username,
						@RequestParam("password") String password) {

		//ログイン処理
		System.out.println("ログイン処理｜ユーザ名：" + username);
		int searchResult = userService.searchUser(username, password);
		if (searchResult == 0) {
			return "loginFailed";
		}
		//セッション開始
		session.setUserName(username);

		return "loginSuccess";
	}

	@RequestMapping("/Users/logout")
	public String logout(Model model) {
		//新しいインスタンスを代入することでセッション破棄する
		session = new UserForSession();
		return "logout";
	}

	@RequestMapping("/Users/register")
	public String register(Model model) {
		return "register";
	}

	@RequestMapping("/Users/registerExec")
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
		//セッション開始
		session.setUserName(username);

		return "registerSuccess";
	}

}
