package com.sharepic.picture;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.Data;

@Data
@Table(keyspace="sharepic_keyspace", name="picture_store")
public class Picture {

	// S3オブジェクトのURL
	@PartitionKey
	@Column(name="object_url")
	private String objectUrl;
	// 投稿者名
	@Column(name="poster")
	private String poster;
	// トピックID
	@Column(name="topic_id")
	private String topicId;
	// トピック
	@Column(name="topic")
	private String topic;
	// キャプション
	@Column(name="caption")
	private String caption;
	// 登録日時
	@Column(name="insert_time")
	private String insertTime;
	// 更新日時
	@Column(name="update_time")
	private String updateTime;

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
		sb.append(insertTime);
		sb.append(System.lineSeparator());
		sb.append("更新日時：");
		sb.append(updateTime);
		sb.append(System.lineSeparator());
		sb.append("-------------------------");

		return sb.toString();
	}

}
