<#assign className = table.className>
package ${mapperPackage};

import tk.mybatis.mapper.common.Mapper;
import ${enityPackage}.${className};
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface ${className}Mapper extends Mapper<${className}> , MySqlMapper<${className}>{

}