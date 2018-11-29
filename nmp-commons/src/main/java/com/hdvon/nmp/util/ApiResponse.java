/**
 * @Project: rcp-java
 * @Copyright: ©2017  广州弘度信息科技有限公司. 版权所有
 */
package com.hdvon.nmp.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接口响应类
 * </p>
 *
 * @author guoweijun
 * @since 2018-04-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApiResponse<T> {
        private String code;
        private String message;
        private T data;


        /**
         * 请求成功
         * @return
         */
        public ApiResponse<T> ok(){
                return ok(ResponseCode.SUCCESS.getMessage());
        }

        /**
         * 请求成功
         * @param msg
         * @return
         */
        public ApiResponse<T> ok(String msg){
                this.setCode(ResponseCode.SUCCESS.getCode());
                this.setMessage(msg);
                return this;
        }

        /**
         * 请求失败
         * @param
         * @return
         */
        public ApiResponse<T> error(){
                this.setCode(ResponseCode.FAILURE.getCode());
                this.setMessage(ResponseCode.FAILURE.getMessage());
                return this;
        }
        /**
         * 请求失败
         * @param msg
         * @return
         */
        public ApiResponse<T> error(String msg){
                this.setCode(ResponseCode.FAILURE.getCode());
                this.setMessage(msg);
                return this;
        }


        /**
         * 设置响应信息
         * @param responseCode
         * @return
         */
        public ApiResponse<T> setResponseCode(ResponseCode responseCode){
                this.setCode(responseCode.getCode());
                this.setMessage(responseCode.getMessage());
                return this;
        }
}
