package com.hdvon.nmp.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class PageParam implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5373005607031031163L;

	@ApiModelProperty(value = "当前页数")
    private int pageNo = 1;

    @ApiModelProperty(value = "每页的数量")
    private int pageSize = 15;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        if(pageNo != null && !"".equals(pageNo)){
            try {
                this.pageNo = Integer.parseInt(pageNo);
            }catch (Exception e){ }
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        if(pageSize != null && !"".equals(pageSize)){
            try {
                this.pageSize = Integer.parseInt(pageSize);
            }catch (Exception e){ }
        }
    }
}
