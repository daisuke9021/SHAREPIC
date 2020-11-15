package com.sharepic.flow;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sharepic.dao.PictureDao;
import com.sharepic.picture.Picture;
import com.sharepic.service.S3Service;
import com.sharepic.util.PropertyUtils;

@Component
public class PictureUploadFlow {

	@Autowired
	S3Service s3Service;

	@Autowired
	PictureDao pictureDao;

	/**
	 * 写真をS3にアップロードし、NoSQL(Cassandra)に写真情報を登録する。
	 * @param picture 写真オブジェクト
	 * @return uploadResult 登録結果
	 * @throws Exception
	 */
	public boolean uploadPicture(Picture picture) throws Exception {

		//S3に写真アップロード
		boolean uploadResult = s3Service.putObject(picture.getObjectUrl());

		if(uploadResult) {
			//S3のオブジェクトURLに変換する
			String bucketName1 = PropertyUtils.getProperties("application").getString("bucket_name1");
			String bucketName2 = PropertyUtils.getProperties("application").getString("bucket_name2");
			String fileName = new File(picture.getObjectUrl()).getName();
			String objectUrl = "https://" + bucketName1 + ".s3-ap-northeast-1.amazonaws.com/" + bucketName2 + fileName;
			System.out.println("S3オブジェクトURL：" + objectUrl);
			picture.setObjectUrl(objectUrl);
			//NoSQL(Cassandra)に登録する
			try {
				pictureDao.insert(picture);
			} catch (Exception e) {
				e.printStackTrace();
				uploadResult = false;
			}
		}

		return uploadResult;
	}

}
