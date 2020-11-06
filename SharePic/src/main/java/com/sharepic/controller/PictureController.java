package com.sharepic.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@RequestMapping("/home")
	public String home(Model model) {
		return "home";
	}

	@RequestMapping("/Get")
	public String getPictures(Model model) {

		List<Picture> pictureList = new ArrayList<>();

		try {
			pictureList = pictureService.getHomePictures();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("写真が取得できませんでした");
		}

		model.addAttribute("pictureList", pictureList);

		for(Picture picture : pictureList) {
			System.out.println(picture);
		}

		return "index.html";
	}

	@PostMapping("/Upload")
	public String uploadPicture(Model model,
								@RequestParam("picture") MultipartFile multipartFile,
								@RequestParam("fileType") String fileType
								) throws Exception {

		//ファイルが空の場合、投稿失敗ページへ遷移
		if (multipartFile.isEmpty()) {
			return "uploadFailed";
		}

		//所定の場所にファイルを取り込む
		String poster = "chihiro";
		String topic = "food";
		String caption = "この前はたのしかったなあ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String uploadTime = sdf.format(new Date());
		String fileName = poster + "_" + topic + "_" + uploadTime + "." + fileType;
		String workspace = PropertyUtils.getProperties("application").getString("upload_file_workspace");
		Path tmpFile = Paths.get(workspace + fileName);
		Files.write(tmpFile, multipartFile.getBytes());

		//取り込んだファイルをS3にアップロードする
		Picture picture = new Picture(tmpFile.toString(),poster,topic,caption);
		boolean s3UploadResult = pictureUploadFlow.uploadPicture(picture);

		//取り組んだファイルを削除する
		Files.delete(tmpFile);

		//アップロードに失敗した場合、投稿失敗ページ遷移
		if(!s3UploadResult) {
			return "uploadFailed";
		}

		//Picture_storeに登録する


		return "uploadSuccess";
	}



}
