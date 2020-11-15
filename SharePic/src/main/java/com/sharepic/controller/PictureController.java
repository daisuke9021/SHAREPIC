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
import com.sharepic.service.PictureService;
import com.sharepic.service.S3Service;
import com.sharepic.util.PropertyUtils;

@Controller
@RequestMapping("/Sharepic/Pictures")
public class PictureController {

	@Autowired
	PictureUploadFlow pictureUploadFlow;
	@Autowired
	PictureService pictureService;
	@Autowired
	S3Service s3Service;

	@RequestMapping("/Home")
	public String home(Model model) {
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

	@RequestMapping("/Topic")
	public String topic(Model model,
								@RequestParam("topic") String topic) {
		//トピックに紐づく写真を取得する
		List<Picture> pictureList = new ArrayList<>();
		try {
			pictureList = pictureService.getPictureByTopic(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(topic);

		model.addAttribute("topic", topic);
		model.addAttribute("pictureList", pictureList);

		return "topic";
	}

	@RequestMapping("/Mypage")
	public String mypage(Model model) {
		//投稿者に紐づく写真を取得する
		List<Picture> pictureList = new ArrayList<>();
		String poster = "takakuwa";
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

	@RequestMapping("/Picture")
	public String picture(Model model,
								@RequestParam("objectUrl") String objectUrl,
								@RequestParam("poster") String poster,
								@RequestParam("topic") String topic,
								@RequestParam("caption") String caption
								) {
		Picture picture = new Picture();
		picture.setObjectUrl(objectUrl);
		picture.setPoster(poster);
		picture.setTopic(topic);
		picture.setCaption(caption);

		model.addAttribute("picture", picture);

		return "picture";
	}

	@RequestMapping("/Upload")
	public String upload(Model model) {

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

	@RequestMapping(value = "/UploadPicture", method = RequestMethod.POST)
	public String uploadPicture(Model model,
								@RequestParam("picture") MultipartFile multipartFile,
								@RequestParam("fileType") String fileType,
								@RequestParam("topic") String topic,
								@RequestParam("caption") String caption
								) throws Exception {

		//投稿者をセッションから取得
		String poster = "takakuwa";

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

}
