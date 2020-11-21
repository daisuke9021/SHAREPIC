package com.sharepic.picture;

import lombok.Data;

@Data
public class CondPicture {

	// S3オブジェクトのURL
	private String objectUrl;
	// 投稿者名
	private String poster;
	// トピックID
	private String topicId;
	// トピック
	private String topic;

	public CondPicture() {}
	public CondPicture(String objectUrl, String poster, String topic) {
		this.objectUrl = objectUrl;
		this.poster = poster;
		this.topic = topic;
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
		sb.append(System.lineSeparator());
		sb.append("-------------------------");

		return sb.toString();
	}

}
