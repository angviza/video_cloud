package com.hdvon.sip.app;

import gov.nist.javax.sip.clientauthutils.UserCredentials;
/**
 * 信令服务器注册用户账号信息
 * @author wanshaojian
 *
 */
public class UserCredentialsImpl implements UserCredentials {
    private String userName;
    private String sipDomain;
    private String password;

    public UserCredentialsImpl(String userName, String sipDomain, String password) {
        this.userName = userName;
        this.sipDomain = sipDomain;
        this.password = password;
    }

	@Override
    public String getPassword() {
        return password;
    }

	@Override
    public String getSipDomain() {
       return sipDomain;
    }

	@Override
    public String getUserName() {
       
        return userName;
    }

}
