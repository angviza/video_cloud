package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.CameraCameragroup;
import com.hdvon.nmp.entity.Cameragrouop;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.ICameragrouopService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameragrouopVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>摄像机分组表
Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
@Slf4j
public class CameragrouopServiceImpl implements ICameragrouopService {

	@Autowired
	private CameragrouopMapper cameragrouopMapper;

    @Autowired
    private CameraCameragroupMapper cameraCameragroupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private TreeCodeService treeCodeService;
    
    @Autowired
    private TreeNodeMapper treeNodeMapper;
    
    @Override
    public PageInfo<CameragrouopVo> getGroupListByPage(CameragrouopVo cameragrouopVo,TreeNodeChildren treeNodeChildren, PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
        Map<String ,Object> param = new HashMap<>();
        param.put("groupIds",treeNodeChildren.getGroupNodeIds());
        param.put("search",cameragrouopVo.getName());
        param.put("orderByCreateTime", true);
        List<CameragrouopVo> list = cameragrouopMapper.selectByParam(param);
        return new PageInfo(list);
    }

    @Override
    public CameragrouopVo getGroupById(String id) {
        if(StrUtil.isBlank(id)){
            return null;
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        List<CameragrouopVo> cameragrouopVos = cameragrouopMapper.selectByParam(param);
        if(cameragrouopVos.size() > 0){
            return cameragrouopVos.get(0);
        }
        return null;
    }

    @Override
    public void saveGroup(UserVo userVo , CameragrouopVo cameragrouopVo) {
        Cameragrouop group = Convert.convert(Cameragrouop.class,cameragrouopVo);
        Example example = new Example(Cameragrouop.class);
        // 校验上级分组
        if(StrUtil.isNotEmpty(group.getPid()) && !group.getPid().equals("0")){
            example.clear();
            example.createCriteria().andEqualTo("id",group.getPid().trim());
            int count = cameragrouopMapper.selectCountByExample(example);
            if(count == 0){
                throw new ServiceException("上级分组不存在");
            }
        }else{//根节点
            group.setPid("0");
        }

        if(StrUtil.isBlank(group.getId())){
            //检查是否存在同级同名
            example.clear();
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("pid",group.getPid()).andEqualTo("name",group.getName());
            int count = cameragrouopMapper.selectCountByExample(example);
            if(count > 0 ){
                throw new ServiceException("该分组名称已存在");
            }

            group.setId(IdGenerator.nextId());
            if(! group.getPid().equals("0")) {
                //生成地址编号
            	String newCode = treeCodeService.genCameraGroupCode(group.getPid().trim());
                group.setCode(newCode);
            }else {
                //生成地址编号
            	String pCode = treeCodeService.genCameraGroupPCode();
                group.setCode(pCode);
            }
            Date date = new Date();
            group.setCreateUser(userVo.getAccount());
            group.setCreateTime(date);
            group.setUpdateUser(userVo.getAccount());
            group.setUpdateTime(date);
            cameragrouopMapper.insertSelective(group);
        }else{
            example.clear();
            Example.Criteria criteria = example.createCriteria();
            //criteria.andNotEqualTo("id",group.getId()).andEqualTo("name",group.getName());
            criteria.andEqualTo("name",group.getName());
            int count = cameragrouopMapper.selectCountByExample(example);
            if(count > 0 ){
                throw new ServiceException("该分组名称已被使用");
            }

        	 Cameragrouop oldGroup = cameragrouopMapper.selectByPrimaryKey(group.getId());
            //修改了上级分组
     	    if(!group.getPid().trim().equals(oldGroup.getPid().trim())) {
                //修改前的当前节点的code
     	    	String oldCode = oldGroup.getCode();
                //修改后的新的父节点id
     	    	String newPid = group.getPid();
                //根据新的父节点的id生成当前节点新的code
     	    	String newCode = treeCodeService.genCameraGroupCode(newPid);
     	    	group.setCode(newCode);
     	    	
     	    	Map<String,Object> paramMap = new HashMap<String,Object>();
     	    	paramMap.put("oldCode", oldCode);
     	    	paramMap.put("newCode", newCode);
     	    	paramMap.put("type", TreeType.GROUP.getVal());
                //修改子节点编号
     	    	treeNodeMapper.updateChildNodesCode(paramMap);
     	    }
            group.setUpdateUser(userVo.getAccount());
            group.setUpdateTime(new Date());
            cameragrouopMapper.updateByPrimaryKeySelective(group);
        }

    }

    @Override
    public void deleteGroup(UserVo userVo, List<String> groupIdList){
        //查询分组树是否存在子级分组
        Example groupExample = new Example(Cameragrouop.class);
        groupExample.createCriteria().andIn("pid",groupIdList);
        int childCount = cameragrouopMapper.selectCountByExample(groupExample);
        if(childCount > 0){
            throw new ServiceException("该分组存在子关联");
        }

        //查询分组树是否已关联摄像头
        Example example = new Example(CameraCameragroup.class);
        example.createCriteria().andIn("cameragroupId",groupIdList);
        int count = cameraCameragroupMapper.selectCountByExample(example);
        if( count > 0 ){
            throw new ServiceException("该分组存在摄像头关联");
        }

        log.info("用户["+userVo.getAccount()+"]删除了分组树："+groupIdList.toString());
        //删除分组表
        Example ce = new Example(Cameragrouop.class);
        ce.createCriteria().andIn("id",groupIdList);
        cameragrouopMapper.deleteByExample(ce);
    }

	@Override
	public List<CameragrouopVo> getRelateWallPlanCameraGroupTree(UserVo userVo , String wallplanId , String channelId){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("wallplanId", wallplanId.trim());
        paramMap.put("channelId", channelId.trim());
        paramMap.put("userId", userVo.getId().trim());
        paramMap.put("isAdmin", userVo.isAdmin());
		List<CameragrouopVo> list = cameragrouopMapper.selectByParam(paramMap);
		return list;
	}

	@Override
	public void batchInsertCameragroups(List<Map<String, Object>> list,String[] titles) {
        // 先删除原来数据
        Example example = new Example(Cameragrouop.class);
        //cameragrouopMapper.deleteByExample(example);
        // 导入数据
        if(list.size() > 0) {
            List<Cameragrouop> addList = new ArrayList<>();
            List<String> tempList = new ArrayList<>();
            // Map数据转化为实体类
            for(Map<String, Object> obj:list){
                example.clear();
                example.createCriteria().andEqualTo("code",obj.get(titles[3]).toString());
                List<Cameragrouop> temp =cameragrouopMapper.selectByExample(example);
                if(temp.size() >0){
                    tempList.add(obj.get(titles[3]).toString());
                }

                Cameragrouop group = new Cameragrouop();
                group.setId(obj.get(titles[0]).toString().trim());
                group.setName(obj.get(titles[1]).toString().trim());
                group.setPid(obj.get(titles[2]).toString().trim());
                group.setCode(obj.get(titles[3]).toString().trim());
                group.setIsPrivate(0);
                group.setDescription(obj.get(titles[4]).toString().trim());
                group.setCreateTime(new Date());
                group.setUpdateTime(new Date());
                addList.add(group);
            }
            if(tempList.size()>0){
                throw new ServiceException("数据库已经存在这些编码："+tempList);
            }
            cameragrouopMapper.insertList(addList);
        }

	}

    @Override
    public List<CameragrouopVo> exportGrpup(String seach,TreeNodeChildren treeNodeChildren) {
        Map<String ,Object> param = new HashMap<>();
        param.put("groupIds",treeNodeChildren.getGroupNodeIds());
        param.put("search",seach);
        param.put("orderByCreateTime", true);
        List<CameragrouopVo> list = cameragrouopMapper.selectByParam(param);
        return list;
    }

    // 从金鹏数据库导入的数据转换为本表数据
    private List<Cameragrouop> transMapToCameragroups(List<Map<String, Object>> list, String[] titles){
		List<Cameragrouop> cameragrouops = new ArrayList<Cameragrouop>();
		List<String> groupIds = new ArrayList<String>();
		List<String> groupPids = new ArrayList<String>();
		
		List<Integer> parentGroupIndex = new ArrayList<Integer>();
		
		for(int i=0;i<list.size();i++) {
			Map<String,Object> map = list.get(i);
			Cameragrouop group = new Cameragrouop();
			group.setId(IdGenerator.nextId());
			String groupId = map.get(titles[0]).toString();
			groupPids.add(groupId);
			
			group.setName(map.get(titles[1]).toString());
			
			String addrPid = map.get(titles[2]).toString();
			groupPids.add(addrPid);
			
			group.setDescription(map.get(titles[3]).toString());
			cameragrouops.add(group);
		}
		for(int i=0;i<groupPids.size();i++) {
			if(groupIds.contains(groupPids.get(i))) {
				parentGroupIndex.add(groupIds.indexOf(groupPids.get(i)));
			}else {
				parentGroupIndex.add(null);
			}
		}
		for(int i=0;i<cameragrouops.size();i++) {
			if(parentGroupIndex.get(i) == null) {
				cameragrouops.get(i).setPid("0");
			}else {
				cameragrouops.get(i).setPid(cameragrouops.get(parentGroupIndex.get(i)).getId());
			}
		}
		return cameragrouops;
	}


	@Override
	public void relateCamerasToCameargroup(UserVo userVo, String cameargroupId, List<String> cameraIdList) {
        //删除自定义分组下关联的所有摄像机
        Example delExa = new Example(CameraCameragroup.class);
        Example.Criteria criteria = delExa.createCriteria().andEqualTo("cameragroupId", cameargroupId);

        //非管理员摄像机权限过滤
        if(!userVo.isAdmin()){
            //查询有权限的摄像机id
            List<String> authCameraIds = cameraMapper.selectAuthCameraIds(userVo.isAdmin(),userVo.getId());
            //增加删除条件，避免删除无权限数据
            criteria.andIn("cameraId",authCameraIds);
            //cameraIds取权限交集!
            cameraIdList.retainAll(authCameraIds);
        }
        cameraCameragroupMapper.deleteByExample(delExa);

        //添加自定义分组下关联的所有摄像机
		List<CameraCameragroup> list = new ArrayList<CameraCameragroup>();
		int sort = 0;//设置组内默认排序序号
        if(cameraIdList != null){
            for(String cameraId : cameraIdList) {
                CameraCameragroup cameraCameragroup = new CameraCameragroup();
                cameraCameragroup.setId(IdGenerator.nextId());
                cameraCameragroup.setCameragroupId(cameargroupId);
                cameraCameragroup.setCameraId(cameraId);
                cameraCameragroup.setSort(sort);
                list.add(cameraCameragroup);
                sort++;
            }
            if(list.size() > 0) {
                cameraCameragroupMapper.insertList(list);
            }
        }
	}

	@Override
	public int getCameragroupCountByPid(String id) {
	    Cameragrouop cameragrouop = new Cameragrouop();
	    cameragrouop.setPid(id);
		return cameragrouopMapper.selectCount(cameragrouop);
	}

    // 处理code与id 与pid的关系(自定义分组树)
    public  List<Map<String,Object>> transpCodeToId(List<Map<String,Object>> list,String[] titles){
        Example example = new Example(Cameragrouop.class);
        //生成id 及校验code
        List names = new ArrayList();
        List temp = new ArrayList();
        for(Map<String, Object> item:list){
            item.put(titles[0], IdGenerator.nextId());
            // 判断名字是否为空
            if("".equals(item.get("name")) || item.get("name") == null){
                temp.add(item.get("code"));
            }
            String code = item.get("code").toString();
            int leng = code.length();
            // 校验code是否符合规则
            if(leng ==0 || leng % 3 !=0){
                names.add(item.get("name"));
            }else {
                if (leng != 3) {
                    // 寻找父节点
                    String pcode = item.get(titles[3]).toString().substring(0, item.get(titles[3]).toString().length() - 3);
                    example.clear();
                    example.createCriteria().orEqualTo("code",pcode);
                    List<Cameragrouop> addArr = cameragrouopMapper.selectByExample(example);
                    if(addArr != null && addArr.size() == 1){
                        item.put(titles[2], addArr.get(0).getId());
                    }
                }
            }

        }

        // 抛出不符合code规则及名称为空
        if (names.size() > 0 || temp.size() > 0){
            throw new ServiceException("excel存在不合法的code编码或者名称为空,请改正，再上传。"+ names+","+temp);
        }

        // titles = {"id","name","pid","code","description"};
        List<Map<String, Object>> tempList = new ArrayList<>();
        List same = new ArrayList();

        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.size(); j++){
                // 判断code是否相同
                if(!list.get(i).get("id").toString().equals(list.get(j).get("id").toString()) && list.get(i).get("code").toString().equals(list.get(j).get("code").toString())){
                    same.add(list.get(j).get("code"));
                }
                // 作为父级遍历
                Map<String, Object> tempMap = list.get(i);
                // 作为子级遍历
                Map<String, Object> tempMap2 = list.get(j);

                // 截取后三位(子级code是长度大于父级长度3)
                String tempIte = null;
                if(tempMap2.get(titles[3]).toString().length() > 3){
                    tempIte = tempMap2.get(titles[3]).toString().substring(0,tempMap2.get(titles[3]).toString().length()-3);
                }

                if(tempMap.get(titles[3]).toString().length() == 3 && tempMap.get(titles[3]).toString().equals(tempMap2.get(titles[3]).toString())){
                    // 确定最高级父节点
                    tempMap2.put(titles[2], "0");
                    tempList.add(tempMap2);
                }
                else if (tempMap.get(titles[3]).toString().equals(tempIte)){
                    // 确定子父级关系
                    tempMap2.put(titles[2], tempMap.get(titles[0]));
                    tempList.add(tempMap2);
                }
                else {
                    if(StrUtil.isBlank(tempMap2.get(titles[2]).toString())){
                        example.clear();
                        example.createCriteria().orEqualTo("code",tempIte);
                        List<Cameragrouop> addArr = cameragrouopMapper.selectByExample(example);
                        if(addArr != null && addArr.size() == 1){
                            tempMap2.put(titles[2], addArr.get(0).getId());
                            tempList.add(tempMap2);
                        }
                    }else {
                        tempList.add(tempMap2);
                    }
                }
            }
        }

        // 抛出不符合code规则的
        if (same.size() > 0){
            throw new ServiceException("excel存在相同code编码,请改正，再上传。"+ same);
        }

        List<Map<String, Object>> tempObject = new ArrayList<>();
        tempList.stream().forEach(p -> {
                    if (!tempObject.contains(p)) {
                        tempObject.add(p);
                    }
                }
        );

        return tempObject;
    }

}
