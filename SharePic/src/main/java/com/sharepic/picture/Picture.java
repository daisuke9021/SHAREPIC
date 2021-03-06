package com.sharepic.picture;

import lombok.Data;

@Data
public class Picture {

	// S3オブジェクトのURL
	private String objectUrl;
	// 投稿者名
	private String poster;
	// トピックID
	private String topicId;
	// トピック
	private String topic;
	// キャプション
	private String caption;
	// 登録日時
	private String insertDate;
	// 更新日時
	private String updateDate;

	public Picture() {}
	public Picture(String objectUrl, String poster, String topic, String caption) {
		this.objectUrl = objectUrl;
		this.poster = poster;
		this.topic = topic;
		this.caption = caption;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OBJECTURL：");
		sb.append(objectUrl);
		sb.append(System.lineSeparator());
		sb.append("POSTER：");
		sb.append(poster);
		sb.append(System.lineSeparator());
		sb.append("TOPIC_ID：");
		sb.append(topicId);
		sb.append(System.lineSeparator());
		sb.append("TOPIC：");
		sb.append(topic);
		sb.append(System.lineSeparator());
		sb.append("CAPTION：");
		sb.append(caption);
		sb.append(System.lineSeparator());
		sb.append("登録日時：");
		sb.append(insertDate);
		sb.append(System.lineSeparator());
		sb.append("更新日時：");
		sb.append(updateDate);
		sb.append(System.lineSeparator());
		sb.append("-------------------------");

		return sb.toString();
	}

}
