package com.hdvon.nmp.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.util.snowflake.IdGenerator;

public class DealYunweiData {
	private static final String url="jdbc:mysql://192.168.2.216:3306/liyong?useUnicode=true&characterEncoding=utf8&useSSL=false";

	private static final String user= "root";

	private static final String password= "rootroot";
	
	public void generateXls(String tableName, boolean isTree) {
		System.out.println("开始生成表["+tableName+"]数据--->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<String> ids = new ArrayList<String>();//对应查询结果顺序的id集合
		List<String> pids = new ArrayList<String>();//对应查询结果顺序的pid集合
		List<Integer> pids_index = new ArrayList<Integer>();//每个节点的父节点在查询结果集合中的索引
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//查询旧的数据集合
		
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url,user,password);
			String sql = "select * from "+tableName;
			cstmt = conn.prepareCall(sql);
			cstmt.execute();
			ResultSet ret = cstmt.getResultSet();
			
			mapOldListToXls(ret,list, ids, pids, tableName);
			
			
			//得到pids_index
			if(isTree) {
				for(int i=0;i<pids.size();i++) {
					if(ids.contains(pids.get(i))) {
						pids_index.add(ids.indexOf(pids.get(i)));
					}else {
						pids_index.add(null);
					}
				}
			}
			//生成新的id集合
			List<Map<String,Object>> new_ids = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ids.size();i++) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(tableName+"_newid", IdGenerator.nextId());
				new_ids.add(map);
			}
			//根据pids中每个pid对应在ids中的索引，得到顺序的新的pid集合
			List<Map<String,Object>> new_pids = new ArrayList<Map<String,Object>>();
			if(isTree) {
				for(int i=0;i<pids_index.size();i++) {
					Map<String,Object> map = new HashMap<String,Object>();
					if(pids_index.get(i)!=null) {
						map.put(tableName+"_newpid", new_ids.get(pids_index.get(i)).get(tableName+"_newid"));
						new_pids.add(map);
					}else {
						map.put(tableName+"_newpid", "0");
						new_pids.add(map);
					}
				}
			}
			
			GenerateXls gx = new GenerateXls();
			List<String> newid_tit = new ArrayList<String>();
			newid_tit.add(tableName+"_newid");
			gx.generateWorkbook("D:\\DealData\\"+tableName+"_newid.xls", tableName+"_newid", "XLS", newid_tit, new_ids);
			if(isTree) {
				List<String> newpid_tit = new ArrayList<String>();
				newpid_tit.add(tableName+"_newpid");
				gx.generateWorkbook("D:\\DealData\\"+tableName+"_newpid.xls", tableName+"_newpid", "XLS", newpid_tit, new_pids);
			}
			generateOldXls(list, gx, tableName);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 将查询的旧的结果数据映射到要生成的xls结果集中
	 * @param ret
	 * @param list
	 * @param ids
	 * @param pids
	 * @param tableName
	 * @throws Exception
	 */
	private void mapOldListToXls(ResultSet ret, List<Map<String,Object>> list, List<String> ids, List<String> pids, String tableName) throws Exception{
		if("t_address".equals(tableName)) {//地址树
			while(ret.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				String id = ret.getString("addressId");
				ids.add(id);
				map.put("id", id);
				map.put("name", ret.getString("addressName"));
				String pid = ret.getString("parentAddressId");
				pids.add(pid);
				map.put("pid", pid);
				map.put("description", ret.getString("description"));
				map.put("createTime", ret.getDate("createTime"));
				map.put("updateTime", ret.getDate("updateTime"));
				list.add(map);
			}
		}else if("t_organization".equals(tableName)) {//组织机构树
			while(ret.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				String id = ret.getString("id");
				ids.add(id);
				map.put("id", id);
				map.put("name", ret.getString("name"));
				String pid = ret.getString("parentId");
				pids.add(pid);
				map.put("pid", pid);
				map.put("description", ret.getString("description"));
				map.put("createTime", ret.getString("createTime"));
				map.put("updateTime", ret.getString("updateTime"));
				list.add(map);
			}
		}else if("t_resource_role".equals(tableName)) {//资源角色树
			
		}else if("t_sysrole".equals(tableName)) {
			
		}else if("sys_dictionarie_type".equals(tableName)) {
			while(ret.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				String id=ret.getString("id");
				ids.add(id);
				map.put("id", id);
				map.put("chName", ret.getString("ch_name"));
				map.put("enName", ret.getString("en_name"));
				map.put("description", ret.getString("rmk"));
				map.put("createTime", null);
				map.put("updateTime", ret.getString("update_time"));
				list.add(map);
			}
		}else if("sys_department".equals(tableName)) {
			//zhvmp
			/*while(ret.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				String id = ret.getString("DeptId");
				ids.add(id);
				map.put("id", id);
				map.put("name", ret.getString("DeptName"));
				String pid = ret.getString("parentDeptId");
				pids.add(pid);
				map.put("pid", pid);
				map.put("createTime", ret.getString("createTime"));
				map.put("updateTime", ret.getString("updateTime"));
				list.add(map);
			}*/
			//运维
			while(ret.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				String id = ret.getString("id");
				ids.add(id);
				map.put("id", id);
				map.put("name", ret.getString("name"));
				String pid = ret.getString("parent_id");
				pids.add(pid);
				map.put("pid", pid);
				map.put("createTime", ret.getString("create_date"));
				map.put("updateTime", ret.getString("update_date"));
				list.add(map);
			}
		}
	}
	/**
	 * @param path
	 * @param name
	 * @param prefix
	 * @param titles
	 * @param list
	 */
	private void generateOldXls(List<Map<String,Object>> list,GenerateXls gx,String tableName) {
		List<String> titles = new ArrayList<String>();
		if("t_address".equals(tableName)) {
			titles.add("id");
			titles.add("name");
			titles.add("pid");
			titles.add("description");
			titles.add("createTime");
			titles.add("updateTime");
		}else if("t_organization".equals(tableName)) {//组织机构树
			titles.add("id");
			titles.add("name");
			titles.add("pid");
			titles.add("description");
			titles.add("createTime");
			titles.add("updateTime");
		}else if("t_resource_role".equals(tableName)) {//资源角色树
			
		}else if("t_sysrole".equals(tableName)) {
			
		}else if("sys_dictionarie_type".equals(tableName)) {
			titles.add("id");
			titles.add("chName");
			titles.add("enName");
			titles.add("description");
			titles.add("createTime");
			titles.add("updateTime");
		}else if("sys_department".equals(tableName)) {//组织机构树
			titles.add("id");
			titles.add("name");
			titles.add("pid");
			titles.add("createTime");
			titles.add("updateTime");
		}
		gx.generateWorkbook("D:\\DealData\\"+tableName+"_old.xls", tableName+"_old", "XLS", titles, list);
	}
	
	
	public static void main(String[] args) {
		DealData dd = new DealData();
		//dd.generateXls("t_address",true);
		//dd.generateXls("t_organization",true);
		//dd.generateXls("sys_dictionarie_type",false);
		dd.generateXls("sys_department",true);
	}
}
