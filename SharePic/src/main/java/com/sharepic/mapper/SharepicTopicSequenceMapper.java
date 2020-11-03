package com.sharepic.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SharepicTopicSequenceMapper {
	
	int getTopicSequence();

}
