package com.hdvon.nmp.generate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DictionaryVo;

import cn.hutool.core.util.StrUtil;

public class ExcelUtil {
	private static final String[] perCols = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
			"Q","R","S","T","U","V","W","X","Y","Z"};
	
	private static final String rootPcode = "0";//根节点默认父编号

	/**
	 * 校验导入组织机构、部门树结构数据编号、名称不能重复，非根节点的编号必须要有父节点编号
	 * @param list 导入的数据集合
	 * @param treeType 导入的树类型
	 * @param colArr 数据行上面的head、说明等行的行号数组
	 * @param isVirtual 是否虚拟组织
	 * @param orgCodes 所有行政区划编号集合
	 * @param cursRow 第一行到数据行的偏移行数（表头以上的行数）
	 * @param cursCol 父编号偏移编号的列数，在编号右边为正，在编号左边为负
	 * @return
	 */
	public static Map<String,List<String>> checkTreeData(List<Map<String,String>> list, String treeType, int[] colArr, String isVirtual, List<String> orgCodes, int cursRow, int cursCol) {
		List<String> codeList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		List<String> pcodeList = new ArrayList<String>();
		
		List<String> bussList = new ArrayList<String>();//所属业务分组（虚拟组织）
		List<String> xzqhList = new ArrayList<String>();//所属行政区划
		
		String codeKey = TreeType.ORG.getVal().equals(treeType) ? "orgCode" : (TreeType.DEPARTMENT.getVal().equals(treeType) ? "depCode" : "");
		String nameKey = TreeType.ORG.getVal().equals(treeType) ? "name" : (TreeType.DEPARTMENT.getVal().equals(treeType) ? "name" : "");
		String pcodeKey = TreeType.ORG.getVal().equals(treeType) ? "parentOrgCode" : (TreeType.DEPARTMENT.getVal().equals(treeType) ? "parentDepCode" : "");
		
		String bussKey = "bussGroupId";
		String orgKey = "orgId";
		for(Map<String,String> map : list) {
			codeList.add(map.get(codeKey));
			nameList.add(map.get(nameKey));
			pcodeList.add(map.get(pcodeKey));
			
			if(TreeType.DEPARTMENT.getVal().equals(treeType)) {
				xzqhList.add(map.get(orgKey));
			}else {
				if("1".equals(isVirtual)) {
					bussList.add(map.get(bussKey));
				}
			}
		}
		
		/*if("0".equals(isVirtual) && !pcodeList.contains(rootPcode)) {
			throw new ServiceException("不存在根节点,根节点父编号只能为"+rootPcode+"！");
		}*/
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if(codeList.size()>0) {
			List<String> cacheCode = new ArrayList<String>();
			checkDuplicate(codeList, 0, treeType, "code", colArr[0], pcodeList, cacheCode, isVirtual, orgCodes, cursRow, cursCol);
			map.put("codeList", cacheCode);
		}
		if(nameList.size()>0) {
			List<String> cacheName = new ArrayList<String>();
			checkDuplicate(nameList, 0, treeType, "name", colArr[1], null, cacheName, null, null, cursRow, 0);
			map.put("nameList", cacheName);
		}
		if(pcodeList.size()>0) {
			//checkDuplicate(pcodeList, 0, treeType, "pcode", colArr[2], null, null);
			if("1".equals(isVirtual)) {//虚拟组织
				if(pcodeList.contains(rootPcode)) {
					throw new ServiceException("第"+transToExcelCol(colArr[2])+"列第"+pcodeList.indexOf(rootPcode)+"行虚拟组织父编号不能为"+rootPcode+",虚拟组织必须再行政区划下！");
				}
			}
			int countRoot = 0;
			for(String  pcode : pcodeList) {
				if(rootPcode.equals(pcode)) {
					countRoot++;
					if(countRoot>1) {
						throw new ServiceException("只能有一个父编号为"+rootPcode+"的根节点！");
					}
				}
			}
		}
		map.put("pcodeList", pcodeList);
		map.put("bussList", bussList);
		map.put("xzqhList", xzqhList);
		return map;
	}
	/**
	 * 递归校验树结构数据编号，名称，父编号之间的关系：
	 *  1、不能存在重复编号；
	 *  2、所有的父编号都要在编号列表中找到；
	 *  3、每条数据的编号和父编号不能相同
	 *  4、所有的数据只能有一个根节点，且根节点的父编号为0
	 * @param list
	 * @param rowNo 行索引，从0开始
	 * @param treeType 
	 * @param columnType 列类型：code表示编号列，name表示名称列，pcode表示父编号列
	 * @param colNo 列索引，从0开始
	 * @param pcodeList
	 * @param cacheCode
	 * @param cursRow
	 */
	public static void checkDuplicate(List<String> list, int rowNo, String treeType, String columnType, int colNo, List<String> pcodeList, List<String> cacheCode, String isVirtual, List<String> orgCodes, int cursRow, int cursCol) {
		if(rowNo >= list.size()) {
			return;
		}
		String item = list.get(rowNo);
		if(cacheCode != null) {
			cacheCode.add(item);
		}
		list.set(rowNo, null);
		String pcode = null;
		if("code".equals(columnType)) {
			pcode = pcodeList.get(rowNo);
		}
		if(list.contains(item)) {
			int duplicateIndex = list.indexOf(item);
			String errorMsg = generateErrorContent(item, pcode, rowNo, duplicateIndex, treeType, columnType, colNo, "0", cursRow, cursCol);
			throw new ServiceException(errorMsg);
		}else {
			if("code".equals(columnType)) {
				if("0".equals(isVirtual) && !item.equals(item.substring(0, item.length()-2))) {//行政区划父编号规则
					if(!rootPcode.equals(pcode)){
						if(!pcode.equals(item.substring(0, item.length()-2))) {
							String errorMsg = generateErrorContent(item, pcode, rowNo, 0, treeType, columnType, colNo, "3", cursRow, cursCol);
							throw new ServiceException(errorMsg);
						}
						if(!(list.contains(pcode) //剩下的编号集合包含父编号
								|| cacheCode.contains(pcode) //缓存的编号集合包含父编号
								)) {//没有父编号
							if(!orgCodes.contains(pcode)) {// 判断数据库中是否存在虚拟组织行政区划父编号
								String errorMsg = generateErrorContent(item, pcode, rowNo, 0, treeType, columnType, colNo, "1", cursRow, cursCol);
								throw new ServiceException(errorMsg);
							}
						}
					}
				}else if("1".equals(isVirtual)) {
					if(pcode.equals(item)) {//编号与父编号相同
						String errorMsg = generateErrorContent(item, pcode, rowNo, 0, treeType, columnType, colNo, "2", cursRow, cursCol);
						throw new ServiceException(errorMsg);
					}
				}
				
			}
			checkDuplicate(list, rowNo+1, treeType, columnType, colNo, pcodeList, cacheCode, isVirtual, orgCodes, cursRow, cursCol);
		}
	}
	
	/**
	 * 生成异常信息
	 * @param rowNo
	 * @param duplicateIndex 与当前行内容相同的行索引
	 * @param treeType
	 * @param columnType
	 * @param colNo 索引从0开始
	 * @param checkType "0":校验是否存在相同内容的单元格；"1":校验是否存在父节点；"2":校验父编号与编号相同；"3":校验是否是编号的父编号
	 * @param cursRow 与表头以下的数据行偏移行数
	 * @param cursCol 偏移当前colNo列索引的列数
	 * @return
	 */
	private static String generateErrorContent(String code, String pcode, int rowNo, int duplicateIndex, String treeType, String columnType, int colNo, String checkType, int cursRow, int cursCol) {
		StringBuffer sb = new StringBuffer();
		String colStr = "";
		if("0".equals(checkType)) {
			colStr = transToExcelCol(colNo);
			sb.append("第"+colStr+"列第"+(rowNo + 1 + cursRow)+"行与第"+(duplicateIndex+1+cursRow)+"行");
			if("code".equals(columnType)) {
				sb.append("编号相同，请检查！");
			}else if("name".equals(columnType)) {
				sb.append("名称相同，请检查！");
			}
			//else if("pcode".equals(columnType)){
				//sb.append("父编号相同，请检查！");
			//}
			else {
				throw new ServiceException("列名错误！");
			}
		}else if("1".equals(checkType)){
			colStr = transToExcelCol(colNo+cursCol);
			sb.append("第"+(rowNo + 1 + cursRow)+"行第"+colStr+"列父编号不存在，请检查！");
		}else if("2".equals(checkType)) {
			colStr = transToExcelCol(colNo+cursCol);
			sb.append("第"+(rowNo + 1 + cursRow)+"行第"+colStr+"列父编号不能与编号相同，请检查！");
		}else if("3".equals(checkType)) {
			colStr = transToExcelCol(colNo+cursCol);
			sb.append("第"+(rowNo + 1 + cursRow)+"行第"+colStr+"列父编号必须是"+code.substring(0, code.length()-2)+"，请检查！");
		}
		
		return sb.toString();
	}
	
	/**
	 * 根据每列字典值生成单元格的说明内容
	 * @param dicVos
	 * @return
	 */
	public static String generateCellContent(List<DictionaryVo> dicVos) {
		StringBuffer valLeg = new StringBuffer();
		StringBuffer valMapLeg = new StringBuffer();
		if(dicVos != null && dicVos.size() > 0) {
			Collections.sort(dicVos, new Comparator<DictionaryVo>() {

				@Override
				public int compare(DictionaryVo o1, DictionaryVo o2) {
					int compareVal = Integer.parseInt(o1.getValue()) - Integer.parseInt(o2.getValue());
					return compareVal;
				}
    			
    		});
			valLeg.append("可填值：[");
			for(int i=0;i<dicVos.size();i++) {
				DictionaryVo dicVo = dicVos.get(i);
				if(i == 0) {
					valLeg.append(dicVo.getValue());
					valMapLeg.append(dicVo.getValue()+":"+dicVo.getChName());
				}else {
					valLeg.append(","+dicVo.getValue());
					valMapLeg.append(";"+dicVo.getValue()+":"+dicVo.getChName());
				}
			}
			valLeg.append("]\r\n");
			valLeg.append(valMapLeg);
		}
		return valLeg.toString();
	}
	/**
	 * 数字列号转excel列号
	 * @param col 列索引，从0开始
	 * @return
	 */
	public static String transToExcelCol(int col) {
		StringBuffer excelCol = new StringBuffer();
		if(col/perCols.length == 0) {
			excelCol.append(perCols[col]);
		}else if((col+1)/perCols.length>(perCols.length+1)){
			throw new ServiceException("最多支持"+(perCols.length*perCols.length+1)+"列，请检查");
		}else{
			excelCol.append(perCols[col/perCols.length-1]).append(perCols[col%perCols.length]);
		}
		return excelCol.toString();
	}
	/**
	 * 校验非空
	 * @param value
	 * @param row
	 * @param excelCol
	 * @param head
	 * @param cursRow 偏移行数
	 */
	public static void checkNotnull(String value, int row, String excelCol, String head, int cursRow) {
		if(StrUtil.isBlank(value)) {
			throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】不能为空。");
		}
	}
	/**
	 * 校验只能输入特定限制的值，比如字典值
	 * @param value
	 * @param row
	 * @param excelCol
	 * @param head
	 * @param limitVals
	 * @param cursRow 偏移行数
	 */
	public static void checkListVal(String value, int row, String excelCol, String head, List<String> limitVals, int cursRow) {
		if(StrUtil.isNotBlank(value)) {
			if(!limitVals.contains(value)) {
				throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】输入值错误。");
			}
		}
	}
	/**
	 * 校验输入文本
	 * @param value
	 * @param row
	 * @param excelCol
	 * @param head
	 * @param limitVal
	 * @param cursRow 偏移行数
	 */
	public static String checkText(String value, int row, String excelCol, String head, JSONObject limitObj, int cursRow) {
		if(StrUtil.isNotBlank(value)) {
			Set<String> textKeySet = limitObj.keySet();
			JSONArray realVal = new JSONArray();
			JSONArray limitVal = new JSONArray();
			for(String  textKey : textKeySet) {
				realVal.add(textKey);
				limitVal.add(limitObj.get(textKey));
			}
			if(!limitVal.contains(value)) {
				StringBuffer sb = new StringBuffer();
				for(Object str : limitVal) {
					sb.append(str+"、");
				}
				String subContent = sb.toString();
				subContent = subContent.substring(0,subContent.length()-1);
				throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】的输入值错误，只能输入【"+subContent+"】。");
			}else {
				int index = limitVal.indexOf(value);
				value = realVal.get(index).toString();
			}
		}
		return value;
	}
	/**
	 * 校验字段长度限制
	 * @param value
	 * @param head
	 * @param row
	 * @param col
	 * @param length
	 * @param cursRow
	 */
	public static void checkLength(String value, String head, int row, String excelCol, String length, int cursRow){
		if(StrUtil.isNotBlank(value)) {
			if(value.length() > Integer.parseInt(length)) {
				throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】长度不能超过"+length+"。");
			}
		}
	}
	/**
	 * 检验是否符合正则式
	 * @param value
	 * @param head
	 * @param row
	 * @param col
	 * @param optionRegx
	 * @param cursRow
	 */
	public static void checkRegx(String value, String head, int row, String excelCol, String checkVal, int cursRow) {
		if(StrUtil.isNotBlank(value)) {
			String optionRegx = "";
			String errorMsg = "";
			if(CheckRegxEnum.INT.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_INT;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】格式错误！";
			}else if(CheckRegxEnum.DOUBLE.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_DOUBLE;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】格式错误！";
			}else if(CheckRegxEnum.IP.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_IP;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】格式错误！";
			}else if(CheckRegxEnum.SBBM.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_GBBM;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】只能输入20位数字的国标编码。";
			}else if(CheckRegxEnum.VIRTUAL_CODE.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_VIRTUAL_CODE;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】只能输入20位数字的虚拟组织编码。";
			}else if(CheckRegxEnum.JD.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_JD;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】只能输入正确的经度，经度范围【-180.000000~+180.000000】。";
			}else if(CheckRegxEnum.WD.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_WD;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】只能输入正确的纬度，纬度范围【-90.000000~+90.000000】。";
			}else if(CheckRegxEnum.PHONE.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_PHONE;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】格式错误！";
			}else if(CheckRegxEnum.MOBILE.getVal().equals(checkVal)) {
				optionRegx = PropertyConfig.REGX_MOBILE;
				errorMsg = "第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】格式错误！";
			}
			if(StrUtil.isNotBlank(optionRegx)) {
				Pattern p = Pattern.compile(optionRegx);
				if(!p.matcher(value).matches()) {
					throw new ServiceException(errorMsg);
				}
			}
		}
	}
	/**
	 * 校验列取值是否在字典值范围内
	 * @param dicMap
	 * @param row
	 * @param col
	 * @param excelCol
	 */
	public static void checkDicValue(Map<String,List<DictionaryVo>> dicMap, String value, String head, int row, int col, String excelCol, String dicKey, int cursRow) {
		if(StrUtil.isNotBlank(value)) {
			if(dicMap != null) {
				List<DictionaryVo> dicList = dicMap.get(dicKey);
				List<String> dicVals = new ArrayList<String>();
				if(dicList != null) {
					for(DictionaryVo dicVo : dicList) {
						dicVals.add(dicVo.getValue());
					}
				}
				if(dicVals.size()>0) {
					if(!dicVals.contains(value)) {
						throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】值错误，请参考可填值说明！");
					}
				}else {
					ExcelUtil.checkRegx(value, head, row, excelCol, CheckRegxEnum.INT.getVal(), cursRow);
				}
			}
		}
	}
	/**
	 * 校验日期时间数据
	 * @param value
	 * @param head
	 * @param row
	 * @param col
	 * @param sdfs
	 */
	public static void checkDate(String value, String head, int row, String excelCol, JSONArray sdfs, int cursRow) {
		boolean flag = false;
		String allSdf = "";
		if(StrUtil.isNotBlank(value)) {
			for(int i=0;i<sdfs.size();i++) {
				if(i==0) {
					allSdf += sdfs.getString(i);
				}else if(i == sdfs.size()-1){
					allSdf += ("或者"+sdfs.getString(i));
				}else {
					allSdf += ("、"+sdfs.getString(i));
				}
				SimpleDateFormat sdf = new SimpleDateFormat(sdfs.getString(i));
				if(!flag) {
					try {
						sdf.parse(value);
						flag = true;
						break;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			if(!flag) {
				throw new ServiceException("第"+(row+1+cursRow)+"行"+excelCol+"列【"+head+"】日期格式不对,支持的格式为"+allSdf+"。");
			}
		}
	}
	/**
	 * 校验表头
	 */
	public static void checkColHead(Row row, int rowNo, List<CheckAttributeVo> checkAttrs, int lastCellNum, int cursRow) {
		for(int j=0; j<lastCellNum; j++) {
			CheckAttributeVo checkVo = checkAttrs.get(j);
			Cell headCell = row.getCell(j);
			String head = FileUtil.getCellValue(headCell);
			String titleName = checkVo.getName();
			String excelCol = transToExcelCol(j);
			if(!titleName.equals(head)) {
				throw new ServiceException("第"+(rowNo+cursRow)+"行"+excelCol+"列表头错误！");
			}
		}
	}
	/**
	 * 校验每个单元格的值是否符合要求
	 * @param cell
	 * @param headCell
	 * @param row
	 * @param col
	 * @return
	 */
	public static String checkCellValue(Cell cell, Cell headCell, int row, int col, CheckAttributeVo checkVo, Map<String,List<DictionaryVo>> dicMap, Map<String,String> dicKeyMap, int cursRow) {
		String head = FileUtil.getCellValue(headCell);
		String value = FileUtil.getCellValue(cell);
		String excelCol = ExcelUtil.transToExcelCol(col);
		value = checkRow(checkVo, row, col, excelCol, head, value, dicMap, dicKeyMap, cursRow);
		return value;
	}
	
	/**
	 * 校验行数据
	 * @param checkVo
	 * @param row
	 * @param col
	 * @param excelCol
	 * @param head
	 * @param value
	 * @param dicMap
	 * @param dicKeyMap
	 */
	private static String checkRow(CheckAttributeVo checkVo, int row, int col, String excelCol, String head, String value, Map<String, List<DictionaryVo>> dicMap, Map<String,String> dicKeyMap, int cursRow) {
		JSONObject validObj = checkVo.getValid();
		String code = checkVo.getCode();
		String dicKey = null;
		if(dicKeyMap != null) {
			dicKey = dicKeyMap.get(code);
		}
		Set<String> keySet = validObj.keySet();
		for(String key : keySet) {
			String checkVal = (String) validObj.get(key);
			if(CheckTypeEnum.NOTNULL.getVal().equals(key)) {
				ExcelUtil.checkNotnull(value, row, excelCol, head, cursRow);
			}else if(CheckTypeEnum.TEXT.getVal().equals(key)){
				JSONObject jsonObject = JSONObject.parseObject(checkVal);
				value = ExcelUtil.checkText(value, row, excelCol, head, jsonObject, cursRow);
			} if(CheckTypeEnum.LENGTH.getVal().equals(key)) {
				ExcelUtil.checkLength(value, checkVo.getName(), row, excelCol, checkVal, cursRow);
			}else if(CheckTypeEnum.DATE.getVal().equals(key)) {
				JSONArray array = (JSONArray) JSONObject.parse(checkVal);
				ExcelUtil.checkDate(value, head, row, excelCol, array, cursRow);
			}else if(CheckTypeEnum.REGEX.getVal().equals(key)) {
				ExcelUtil.checkRegx(value, head, row, excelCol, checkVal, cursRow);
			}if(CheckTypeEnum.DIC.getVal().equals(key)) {
				ExcelUtil.checkDicValue(dicMap, value, head, row, col, excelCol, dicKey, cursRow);
			}
		}
		return value;
	}
	
	/**
	 * 校验是否空行
	 * @param row
	 * @return
	 */
	public static boolean checkBlankRow(Row row) {
		int lastCellNum = row.getLastCellNum();
		boolean isBlankRow = true;
		for(int j=0; j<lastCellNum; j++) {
     	   Cell cell = row.getCell(j);
     	   String value = FileUtil.getCellValue(cell);
     	  if(StrUtil.isNotBlank(value)) {
     		  isBlankRow = false;
     		  break;
     	  }
        }
		return isBlankRow;
	}
}
