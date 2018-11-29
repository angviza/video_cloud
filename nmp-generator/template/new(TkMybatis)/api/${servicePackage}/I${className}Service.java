<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign classPOName = table.className + 'Po'>
<#assign classVOName = table.className + 'Vo'>
package ${servicePackage};

/**
 * <br>
 * <b>功能：</b>${table.remarks} 服务类<br>
 * <b>作者：</b>${creator}<br>
 * <b>日期：</b>${.now?string("yyyy")}<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) ${.now?string("yyyy")}<br>
 */
public interface I${className}Service{

}
