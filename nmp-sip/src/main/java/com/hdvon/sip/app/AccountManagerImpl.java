package com.hdvon.sip.app;

import javax.sip.ClientTransaction;

import com.hdvon.sip.utils.SipConstants;

import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.UserCredentials;
/**
 * 信令服务器注册账号管理
 * @author wanshaojian
 *
 */
public class AccountManagerImpl implements AccountManager {

	String registerName;
	String registerPwd;
	
	public AccountManagerImpl(String registerName,String registerPwd) {
		// TODO Auto-generated constructor stub
		this.registerName = registerName;
		this.registerPwd = registerPwd;
	}
	
	@Override
	public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
		// TODO Auto-generated method stub
		return new UserCredentialsImpl(registerName,SipConstants.SIP_DOMAIN,registerPwd);
	}

}
