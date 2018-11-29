package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.RepUseranalysis;
import com.hdvon.quartz.mapper.UserAnalysisMapper;
import com.hdvon.quartz.service.IUserAnalysisService;
import com.hdvon.quartz.util.IdGenerator;

/**
 * <br>
 * <b>功能：</b>用户行为统计Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserAnalysisServiceImpl implements IUserAnalysisService {

	@Autowired
	private UserAnalysisMapper userAnalysisMapper;

	@Override
	public void saveUserLog(List<RepUseranalysis> camearTotal, List<RepUseranalysis> onlineTotal,
			List<RepUseranalysis> loiginTotal, Map<String, Object> param) {
		
		 String account="";
		 Date startDate=(Date)param.get("startDate");
		 Date endDate=(Date)param.get("endDate");
		 
		 List<RepUseranalysis> useranlysisList=new ArrayList<RepUseranalysis>();
		 for(RepUseranalysis camear:camearTotal) {
			 if(camear.getInviteTotal()==null && camear.getReplayTotal() ==null && 
					 camear.getDownloadTotal() ==null  && camear.getControlTotal() ==null) {
				 continue;
			 }else {
				 account+=camear.getAccount()+",";
		    	 camear.setId(IdGenerator.nextId());
		    	 RepUseranalysis analysis= new RepUseranalysis();
		     	 analysis.setId(IdGenerator.nextId());
		     	 analysis.setAccount(camear.getAccount());
		     	 analysis.setInviteTotal(camear.getInviteTotal());
		     	 analysis.setReplayTotal(camear.getReplayTotal());
		     	 analysis.setDownloadTotal(camear.getDownloadTotal());
		         analysis.setControlTotal(camear.getControlTotal());
		     	 analysis.setStartTime(startDate);
		     	 analysis.setCreatTime(endDate);
		     	 
		     	 useranlysisList.add(analysis);
			 }
	    	
		  }
		//在线时长
        for(RepUseranalysis online:onlineTotal) {
        	if(! account.contains(online.getAccount())) {
        		if(online.getOnlineTotal() !=null && online.getOnlineTotal() !=0 ) {
        			account+=online.getAccount()+",";
            		RepUseranalysis analysis= new RepUseranalysis();
            		analysis.setId(IdGenerator.nextId());
            		analysis.setAccount(online.getAccount());
            		analysis.setOnlineTotal(online.getOnlineTotal());
            		analysis.setStartTime(startDate);
            		analysis.setCreatTime(endDate);
            		
            		useranlysisList.add(analysis);
        		}
        		
        	}else {
        		for(RepUseranalysis user : useranlysisList) {
        			if(user.getAccount().equals(online.getAccount())) {
            			if(online.getOnlineTotal() !=null && online.getOnlineTotal() !=0) {
            				user.setOnlineTotal(online.getOnlineTotal());
            			}
            		}
        		}
        	}
    	}//
        
        //登录次数
        for(RepUseranalysis login :loiginTotal) {
        	if(! account.contains(login.getAccount())) {
        		if(login.getLoiginTotal() !=null && login.getLoiginTotal() !=0) {
        			account+=login.getAccount()+",";
            		RepUseranalysis analysis= new RepUseranalysis();
            		analysis.setId(IdGenerator.nextId());
            		analysis.setAccount(login.getAccount());
            		analysis.setLoiginTotal(login.getLoiginTotal());
            		analysis.setStartTime(startDate);
            		analysis.setCreatTime(endDate);
            		
            		useranlysisList.add(analysis);
        		}
        		
        	}else {
        		for(RepUseranalysis user : useranlysisList) {
        			if(user.getAccount().equals(login.getAccount())) {
            			if(login.getLoiginTotal() !=null && login.getLoiginTotal() !=0) {
            				user.setLoiginTotal(login.getLoiginTotal());
            			}
            		}
        		}
        		
        	}
        }//
		 
	     //写入数据库
	    if(useranlysisList.size() >0) {
	    	 userAnalysisMapper.insertList(useranlysisList);
	     }
	     

	}

}
