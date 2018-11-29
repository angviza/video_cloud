package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.UserPictureVo;

public interface IUserPictureService{

	void save(UserPictureVo picture);
	
	UserPictureVo queryRecord(String userId);
	
}
