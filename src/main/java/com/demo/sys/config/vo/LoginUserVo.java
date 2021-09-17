package com.demo.sys.config.vo;

import com.auth0.jwt.interfaces.Claim;
import com.demo.common.model.vo.Constants;
import com.demo.common.utils.JwtToken;
import com.demo.common.utils.StringUtil;

import java.util.Map;

/**
 * <p>Description:  登录信息</p>
 * <p>Title: LoginUserVo.java</p>
 * @author Rwq
 */
public class LoginUserVo {
	/**
     * 得到用户Id
     * @param token
     * @return
     * @throws Exception
     */
	public static String getUserId(String token) throws Exception{
		if(StringUtil.isEmpty(token)){
			return "";
		}
		Map<String,Claim> claim = JwtToken.verifyToken(token);
		String userId = claim.get(Constants.USERID).asString();
		return userId;
	}
	
	/**
     * 得到用户名称
     * @param token
     * @return
     * @throws Exception
     */
	public static String getUserName(String token) throws Exception{
		if(StringUtil.isEmpty(token)){
			return "";
		}
		Map<String,Claim> claim = JwtToken.verifyToken(token);
		String userName = claim.get(Constants.USERNAME).asString();
		return userName;
	}

	/**
	 * 得到用户所属机构
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String getUserOrg(String token) throws Exception{
		if(StringUtil.isEmpty(token)){
			return "";
		}
		Map<String,Claim> claim = JwtToken.verifyToken(token);
		return claim.get(Constants.ORGUUID).asString();
	}
	
	/**
     * 得到用户权限ids
     * @param token
     * @return
     * @throws Exception
     */
	public static String getRoleIds(String token) throws Exception{
		if(StringUtil.isEmpty(token)){
			return "";
		}
		Map<String,Claim> claim = JwtToken.verifyToken(token);
		String roleIds = claim.get(Constants.ROLEIDS).asString();
		return roleIds;
	}
	
	
}
