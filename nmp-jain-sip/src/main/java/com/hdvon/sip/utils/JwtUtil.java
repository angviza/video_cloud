package com.hdvon.sip.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hdvon.sip.entity.UserVo;

/**
 *  jwt(json web token)工具类
 */
public class JwtUtil {
    private final static String SECRET = "hdJwtSecret";
    public static String createToken(UserVo userVo){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userVo.getId());
        map.put("loginTime",new Date());
        map.put("ip","127.0.0.1");
        String token = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000 * 60 ))//60天超时时间，jwt不管超时问题，由redis负责超时时间
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }

    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
    }

    public static Claims validateToken(String token) throws Exception {
        Claims claims = null;
        try{
            claims = parseToken(token);
            String userId = claims.get("userId").toString();
        } catch(ExpiredJwtException e) {
            throw new Exception("token超时");
        } catch (InvalidClaimException e) {
            throw new Exception("token无效");
        } catch (Exception e) {
            throw new Exception("token异常");
        }
        return claims;
    }
}
