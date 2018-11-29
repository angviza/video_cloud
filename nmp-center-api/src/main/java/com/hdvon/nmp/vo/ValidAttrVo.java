package com.hdvon.nmp.vo;

import java.io.Serializable;

import com.hdvon.nmp.generate.FunTypeEnum;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ValidAttrVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2583371437907841549L;
	
	public ValidAttrVo() {
		super();
	}
	public ValidAttrVo(String funType, int[] validCols) {
		if(FunTypeEnum.DEPARTMENT.getVal().equals(funType)) {
			this.codeCol = validCols[0];
			this.nameCol = validCols[1];
			this.pcodeCol = validCols[2];
			this.xzqhCol = validCols[3];
		}else if(FunTypeEnum.ORG.getVal().equals(funType)) {
			this.codeCol = validCols[0];
			this.nameCol = validCols[1];
			this.pcodeCol = validCols[2];
			this.bussCol = validCols[3];
		}else if(FunTypeEnum.CAMERA.getVal().equals(funType)) {
			this.sbbmCol = validCols[0];
			this.sbmcCol = validCols[1];
			this.xzqhCol = validCols[2];
			this.addressCol = validCols[3];
			this.encoderCol = validCols[4];
			this.bussCol = validCols[5];
			this.projectCol = validCols[6];
		}
		
	}
	//树结构数据
	private int codeCol;
	private int nameCol;
	private int pcodeCol;
	
	//摄像机
	private int sbbmCol;
	private int sbmcCol;
	private int xzqhCol;
	private int addressCol;
	private int encoderCol;
	private int bussCol;
	private int projectCol;

}
