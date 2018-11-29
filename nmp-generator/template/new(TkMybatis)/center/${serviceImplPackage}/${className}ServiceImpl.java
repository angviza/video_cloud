<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign classPOName = table.className + 'Po'>
<#assign classVOName = table.className + 'Vo'>
package ${serviceImplPackage};

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import ${mapperPackage}.${className}Mapper;
import ${servicePackage}.I${className}Service;

/**
 * <br>
 * <b>功能：</b>${table.remarks}Service<br>
 * <b>作者：</b>${creator}<br>
 * <b>日期：</b>${.now?string("yyyy")}<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) ${.now?string("yyyy")}<br>
 */
@Service
public class ${className}ServiceImpl implements I${className}Service {

	@Autowired
	private ${className}Mapper ${className?uncap_first}Mapper;

}
