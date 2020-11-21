package com.sharepic.service;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sharepic.util.PropertyUtils;

@Component
public class S3Service {

	/**
	 * 引数に指定されたファイルをS3にアップロードします。
	 * @param objectUrl 画像ファイル
	 * @return
	 * @throws Exception
	 */
	public boolean putObject(String objectUrl) throws Exception {

		System.out.println("【開始】S3アップロード処理");

		boolean uploadResult = false;
		File targetFile = new File(objectUrl);

		//ファイルが読み込めること
		if(!targetFile.canRead()) {
			System.out.println("ファイルが読み込めません。");
			return uploadResult;
		}

		try {
			String bucketName = PropertyUtils.getProperties("application").getString("full_bucket_name");
			AmazonS3 s3Client = makeS3Clinet();
			FileInputStream fis = new FileInputStream(targetFile);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(targetFile.length());
			PutObjectRequest putRequest = new PutObjectRequest(bucketName, targetFile.getName(), fis, metadata);
			//オブジェクトのACLを設定
			putRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			//アップロード
			s3Client.putObject(putRequest);
			uploadResult = true;
			System.out.println("S3アップロード処理完了；" + objectUrl);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("S3アップロード処理に失敗しました。");
		}

		System.out.println("【終了】S3アップロード処理");

		return uploadResult;
	}

	private AmazonS3Client makeS3Clinet() {
		String accessKey = PropertyUtils.getProperties("application").getString("aws.accessKeyId");
		String secretKey = PropertyUtils.getProperties("application").getString("aws.secretKey");
		// 認証オブジェクトを作成
	    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	    // ConfigurationでTimeout時間を30秒に設定
	    ClientConfiguration clientConfiguration = new ClientConfiguration();
	    clientConfiguration.setConnectionTimeout(30000);
	    // AmazonS3Clientをインスタンス化
	    return new AmazonS3Client(credentials, clientConfiguration);
	}

}
