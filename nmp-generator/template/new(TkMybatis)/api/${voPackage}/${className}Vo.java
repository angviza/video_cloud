<#include "/macro.include"/>
<#assign className = table.className> 
<#assign classVOName = table.className + 'Vo'>
<#assign classNameLower = className?uncap_first> 
package ${voPackage};

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>${table.remarks} VO类<br>
 * <b>作者：</b>${creator}<br>
 * <b>日期：</b>${.now?string("yyyy")}<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) ${.now?string("yyyy")}<br>
 */
@Data
@ApiModel(value ="${className}")
public class ${classVOName} implements Serializable{

	private static final long serialVersionUID = 1L;

    <#list table.columns as column>
    <#if column.columnNameLower == "createTime" || column.columnNameLower == "updateTime" || column.columnNameLower == "createUser" || column.columnNameLower == "updateUser" >/*</#if>@ApiModelProperty(value = "${column.columnAlias!}")
    private ${column.javaType} ${column.columnNameLower};<#if column.columnNameLower == "createTime" || column.columnNameLower == "updateTime" || column.columnNameLower == "createUser" || column.columnNameLower == "updateUser" >*/</#if>

    </#list>

}

<#macro generateJavaColumns>
<#list table.columns as column>
public ${column.javaType} get${column.columnName}() {
        return this.${column.columnNameLower};
        }
public void set${column.columnName}(${column.javaType} ${column.columnNameLower}) {
        this.${column.columnNameLower} = ${column.columnNameLower};
        }
</#list>
</#macro>

<#macro generateJavaColumns>
	<#list table.columns as column>
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	public void set${column.columnName}(${column.javaType} ${column.columnNameLower}) {
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	</#list>
</#macro>

<#macro generateJavaOneToMany>
	<#list table.exportedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	private Set ${fkPojoClassVar}s = new HashSet(0);
	public void set${fkPojoClass}s(Set<${fkPojoClass}> ${fkPojoClassVar}){
		this.${fkPojoClassVar}s = ${fkPojoClassVar};
	}
	
	public Set<${fkPojoClass}> get${fkPojoClass}s() {
		return ${fkPojoClassVar}s;
	}
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	private ${fkPojoClass} ${fkPojoClassVar};
	
	public void set${fkPojoClass}(${fkPojoClass} ${fkPojoClassVar}){
		this.${fkPojoClassVar} = ${fkPojoClassVar};
	}
	
	public ${fkPojoClass} get${fkPojoClass}() {
		return ${fkPojoClassVar};
	}
	</#list>
</#macro>
