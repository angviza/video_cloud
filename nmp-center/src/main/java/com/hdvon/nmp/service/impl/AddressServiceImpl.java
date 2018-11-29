package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.config.redis.BaseRedisDao;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Cameragrouop;
import com.hdvon.nmp.entity.EncodeserverMapping;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.AddressMapper;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.mapper.EncodeserverMappingMapper;
import com.hdvon.nmp.mapper.TreeNodeMapper;
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.AddressVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>地址表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 * @param
 */
@Service
@Slf4j
public class AddressServiceImpl implements IAddressService {
	
	@Autowired
	private AddressMapper addressMapper;

    @Autowired
    private CameraMappingMapper cameraMappingMapper;

    @Autowired
    private EncodeserverMappingMapper encodeserverMappingMapper;
    
    @Resource
	BaseRedisDao redisDao;
    
    @Autowired
    private TreeCodeService treeCodeService;
    
    @Autowired
    private TreeNodeMapper treeNodeMapper;


    @Override
    public PageInfo<AddressVo> getAddressListByPage(AddressVo addressVo, TreeNodeChildren treeNodeChildren, PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
        Map<String ,Object> param = new HashMap<>();
        param.put("addrIds",treeNodeChildren.getAddressNodeIds());
        param.put("search",addressVo.getName());
        List<AddressVo> list = addressMapper.selectByParam(param);
        return new PageInfo<>(list);
    }


    @Override
    public AddressVo getAddressById(String id) {

        Address address = addressMapper.selectByPrimaryKey(id);
        if(address == null) {
        	throw new ServiceException("地址不存在！");
        }
        AddressVo addressVo = Convert.convert(AddressVo.class, address);
        //设置父节点名称
        if(address.getPid() != null){
            Address parent = addressMapper.selectByPrimaryKey(address.getPid());
            if(parent != null){
            	addressVo.setParentName(parent.getName());
            }
        }else{
        	addressVo.setParentName("");
        }
        return addressVo;
    }

    @Override
    public void saveAddress(UserVo userVo , AddressVo addressVo){
        Address address = Convert.convert(Address.class,addressVo);
        // 定义全局 example
        Example example = new Example(Address.class);
        // 校验代码与上级代码的关系
        if(StrUtil.isNotEmpty(address.getPid()) && !address.getPid().equals("0")){
            example.createCriteria().andEqualTo("id",address.getPid());
            List<Address> list=addressMapper.selectByExample(example);
            if (list.size() == 0){
                throw new ServiceException("上级地址不存在");
            }
        }else{//根节点
            address.setPid("0");
        }

       //新增
        if(StrUtil.isBlank(address.getId())){
            //检查是否存在同名
            example.clear();
            Example.Criteria criteria = example.createCriteria();
            //criteria.andEqualTo("pid",address.getPid()).andEqualTo("name",address.getName());
            criteria.andEqualTo("name",address.getName());
            int count = addressMapper.selectCountByExample(example);
            if(count > 0 ){
                throw new ServiceException("该地址名称已存在");
            }
            address.setName(address.getName());

            address.setId(IdGenerator.nextId());
            if(!address.getPid().equals("0")) {
                //生成地址编号,减少页面输入错误，给用户带来不必要的麻烦
            	String newCode = treeCodeService.genAddressCode(address.getPid());
                address.setCode(newCode);
            }else {
                //获取编码
            	String pCode = treeCodeService.genAddressPCode();
                address.setCode(pCode);
            }
            Date date = new Date();
            address.setCreateUser(userVo.getAccount());
            address.setCreateTime(date);
            address.setUpdateUser(userVo.getAccount());
            address.setUpdateTime(date);
            addressMapper.insertSelective(address);
        }else{
            //修改
            example.clear();
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotEqualTo("id",address.getId()).andEqualTo("name",address.getName());
            int count = addressMapper.selectCountByExample(example);
            if(count > 0 ){
                throw new ServiceException("该地址名称已被使用");
            }

    	    Address oldAddr = addressMapper.selectByPrimaryKey(address.getId());
            //修改了上级地址
    	    if(!address.getPid().equals(oldAddr.getPid())) {
                //修改前的当前节点的code
    	    	String oldCode = oldAddr.getCode();
                //修改后的新的父节点id
    	    	String newPid = address.getPid();
                //根据新的父节点的id生成当前节点新的code
    	    	String newCode = treeCodeService.genAddressCode(newPid);
    	    	address.setCode(newCode);
    	    	
    	    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	    	paramMap.put("oldCode", oldCode);
    	    	paramMap.put("newCode", newCode);
    	    	paramMap.put("type", TreeType.ADDRESS.getVal());
                //修改子节点编号
    	    	treeNodeMapper.updateChildNodesCode(paramMap);
    	    }
    	    
            address.setUpdateUser(userVo.getAccount());
            address.setUpdateTime(new Date());
            addressMapper.updateByPrimaryKeySelective(address);
        }
    }

    @Override
    public void deleteAddress(UserVo userVo, List<String> addressIds){
        //查询地址树是否存在子级地址
        Example addressExample = new Example(Address.class);
        addressExample.createCriteria().andIn("pid",addressIds);
        int childCount = addressMapper.selectCountByExample(addressExample);
        if(childCount > 0){
            throw new ServiceException("该地址存在子关联");
        }

        //查询地址树是否已关联摄像头
        Example example = new Example(CameraMapping.class);
        example.createCriteria().andIn("addressId",addressIds);
        int count = cameraMappingMapper.selectCountByExample(example);
        if( count > 0 ){
            throw new ServiceException("该地址存在摄像头关联");
        }
        
        //查询地址树是否关联编码器
        Example eme = new Example(EncodeserverMapping.class);
        eme.createCriteria().andIn("addressId",addressIds);
        int encodeCount = encodeserverMappingMapper.selectCountByExample(eme);
        if(encodeCount > 0) {
        	throw new ServiceException("该地址存在关联的编码器");
        }
        
        log.info("用户["+userVo.getAccount()+"]删除了地址树："+addressIds.toString());
        //删除地址表
        Example ae = new Example(Address.class);
        ae.createCriteria().andIn("id",addressIds);
        addressMapper.deleteByExample(ae);
    }

	@Override
	public void batchInsertAddresses(List<Map<String,Object>> addresses, String[] titles) {
		// 先删除原来数据
        Example example = new Example(Address.class);
        //addressMapper.deleteByExample(example);
        // 导入数据
        if(addresses.size() > 0) {
            // Map数据转化为实体类
            List<Address> addList = new ArrayList<>();
            List<String> tempList = new ArrayList<>();
            for(Map<String, Object> obj:addresses){
                example.clear();
                example.createCriteria().andEqualTo("code",obj.get(titles[3]).toString());
                List<Address> temp =addressMapper.selectByExample(example);
                if(temp.size() >0){
                    tempList.add(obj.get(titles[3]).toString());
                }
                Address address = new Address();
                address.setId(obj.get(titles[0]).toString().trim());
                address.setName(obj.get(titles[1]).toString().trim());
                address.setPid(obj.get(titles[2]).toString().trim());
                address.setCode(obj.get(titles[3]).toString().trim());
                address.setDescription(obj.get(titles[4]).toString().trim());
                address.setCreateTime(new Date());
                address.setUpdateTime(new Date());
                addList.add(address);
            }
            if(tempList.size()>0){
                throw new ServiceException("数据库已经存在这些编码："+tempList);
            }
            addressMapper.insertList(addList);
        }
	}

    @Override
    public List<AddressVo> exportAddresses(String seach,TreeNodeChildren treeNodeChildren) {
        Map<String ,Object> param = new HashMap<>();
        param.put("addrIds",treeNodeChildren.getAddressNodeIds());
        param.put("search",seach);
        List<AddressVo> list = addressMapper.selectByParam(param);
        return list;
    }

    // 基于金鹏数据导入
    private List<Address> transMapToAddresses(List<Map<String, Object>> list, String[] titles){
		List<Address> addresses = new ArrayList<Address>();
		List<String> addrIds = new ArrayList<String>();
		List<String> addrPids = new ArrayList<String>();
		
		List<Integer> parent_addr_index = new ArrayList<Integer>();
		
		for(int i=0;i<list.size();i++) {
			Map<String,Object> map = list.get(i);
			Address addr = new Address();
			addr.setId(IdGenerator.nextId());
			String addrId = map.get(titles[0]).toString();
			addrIds.add(addrId);
			
			addr.setName(map.get(titles[1]).toString());
			
			String addrPid = map.get(titles[2]).toString();
			addrPids.add(addrPid);
			
			addr.setDescription(map.get(titles[3]).toString());
			addresses.add(addr);

            addr.setCode(map.get(titles[4]).toString());
            addresses.add(addr);
		}
		for(int i=0;i<addrPids.size();i++) {
			if(addrIds.contains(addrPids.get(i))) {
				parent_addr_index.add(addrIds.indexOf(addrPids.get(i)));
			}else {
				parent_addr_index.add(null);
			}
		}
		for(int i=0;i<addresses.size();i++) {
			if(parent_addr_index.get(i) == null) {
				addresses.get(i).setPid("0");
			}else {
				addresses.get(i).setPid(addresses.get(parent_addr_index.get(i)).getId());
			}
		}
		return addresses;
	}

	@Override
	public List<AddressVo> getStatusAddressList(String addressId) {
		List<AddressVo> list = addressMapper.selectStatusAddressList(addressId);
		return list;
	}

    // 处理code与id 与pid的关系(地址树)
    public  List<Map<String,Object>> transpCodeToId(List<Map<String,Object>> list,String[] titles){
        Example example = new Example(Address.class);
        //生成id 及校验code
        List names = new ArrayList();
        List temp = new ArrayList();
        for(Map<String, Object> item:list){
            item.put(titles[0], IdGenerator.nextId());
            String code = item.get("code").toString();
            // 判断名字是否为空
            if("".equals(item.get("name")) || item.get("name") == null){
                temp.add(item.get("code"));
            }
            int leng = code.length();
            // 校验code是否符合规则
            if(leng ==0 || leng % 3 !=0){
                names.add(item.get("name"));
            } else {
                if (leng != 3) {
                    // 寻找父节点
                    String pcode = item.get(titles[3]).toString().substring(0, item.get(titles[3]).toString().length() - 3);
                    example.clear();
                    example.createCriteria().orEqualTo("code", pcode);
                    List<Address> addArr = addressMapper.selectByExample(example);
                    if (addArr != null && addArr.size() == 1) {
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
                        List<Address> addArr = addressMapper.selectByExample(example);
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
