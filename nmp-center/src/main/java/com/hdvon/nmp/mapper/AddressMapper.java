package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.AddressAndCameraParamVo;
import com.hdvon.nmp.vo.AddressAndCameraVo;
import com.hdvon.nmp.vo.AddressVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface AddressMapper extends Mapper<Address> , MySqlMapper<Address> {

    List<AddressVo> selectByParam(Map<String, Object> param);

    /**
     * 获取地址树
     * @return
     */
    List<AddressVo>  getAddressTree();
    
    List<AddressVo> selectStatusAddressList(String addressId);
    
	String selectMaxCode(Map<String, String> param);
}