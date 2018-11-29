package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.UserFilepathVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>用户文件存储路径 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IUserFilepathService{

	List<UserFilepathVo> getUserFilepath(Map<String ,String> map);

	void saveUserFilepath(UserFilepathVo vo, UserVo user);
	
}
