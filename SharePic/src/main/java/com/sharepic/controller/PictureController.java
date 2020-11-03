package com.sharepic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sharepic.picture.Picture;
import com.sharepic.service.PictureService;

@Controller
public class PictureController {

	@Autowired
	PictureService pictureService;

	@RequestMapping("/Pictures")
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



}
