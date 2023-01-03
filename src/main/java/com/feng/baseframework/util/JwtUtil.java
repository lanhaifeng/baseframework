package com.feng.baseframework.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * baseframework
 * 2019/2/21 16:30
 * token工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	//过期时间
	private static final long EXPIRE_TIME = 15*60*1000;
	//主题
	private static final String TOKEN_SUBJECT = "MC_SOCK";
	//私钥
	private static final String TOKEN_SECRET = "uLmzEIo/pNCgDh82RuxERU7TUUbMvNLIdLZ0fBV0ryM=";
	//sock ip地址
	private static final String SOCK_HOST_ADDRESS = "SOCK_HOST_ADDRESS";

	/**
	 * 2019/1/30 16:58
	 * 生成token
	 *
	 * @param fromIp  来源ip
	 * @param toIp	  目标ip
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String generateToken(String fromIp){
		try{
			Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
			SecretKey key = generalKey();
			Map<String,Object> claims = new HashMap<>();
			claims.put(SOCK_HOST_ADDRESS, fromIp);

			JwtBuilder builder = Jwts.builder()
					.setClaims(claims)
					.setSubject(TOKEN_SUBJECT)
					.setExpiration(date)
					.signWith(key);

			return builder.compact();
		}catch(Exception e){
			logger.error("生成token失败：" + ExceptionUtils.getFullStackTrace(e));
		}

		return null;
	}

	/**
	 * 2019/1/30 16:59
	 * 解析jwt携带信息
	 *
	 * @param jwt
	 * @author lanhaifeng
	 * @return io.jsonwebtoken.Claims
	 */
	public static Claims parseJWT(String jwt){
		try{
			SecretKey key = generalKey();
			Claims claims = Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(jwt).getBody();
			return claims;
		}catch(Exception e){
			logger.error("解析token信息失败：" + ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}

	/**
	 * 2019/1/30 17:01
	 * 生成私钥
	 *
	 * @param
	 * @author lanhaifeng
	 * @return javax.crypto.SecretKey
	 */
	public static SecretKey generalKey(){
		byte[] encodedKey = Base64.getDecoder().decode(TOKEN_SECRET);
		return Keys.hmacShaKeyFor(encodedKey);
	}

	public static boolean validateToken(String token){
		boolean result = false;
		if(StringUtils.isNotBlank(token)){
			Claims claims = parseJWT(token);
			String sockIp = Optional.ofNullable(claims).map(c->c.get(SOCK_HOST_ADDRESS, String.class)).orElse("");
			//验证逻辑
			if(StringUtils.isNotBlank(sockIp)){
				result = true;
			}
		}

		return result;
	}
}
