package com.example.domain.util;

import com.example.domain.entity.Token;
import com.example.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author xiaoaa
 * @date 2023/5/20 16:00
 **/
public class JwtUtils {

    private static final long expire = 1000 * 60 * 60  * 24;

    private static final String SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLP26";
    // private static final String SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    private static final String TOKEN_ID = "id";

    /**
     * 生成Token
     * @param user
     * @return
     */
    public static Token getJwtToken(User user) {

        Date expireDate = new Date(System.currentTimeMillis() + expire);
        String jwtToken = Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setHeaderParam("alg", "HS2256")
                .setSubject("lin-user")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim(TOKEN_ID, user.getId())
                .claim("userName", user.getUserName())
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return Token.builder()
                .token(jwtToken)
                .expireTime(expireDate)
                .build();
    }

    /**
     * 判断token是否存在与有效
     * @Param jwtToken
     */
    public static boolean checkToken(String jwtToken){
        if (StringUtils.hasText(jwtToken)){
            return false;
        }
        try{
            //验证token
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @Param request
     */
    public static Integer getUserId(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (Integer) body.get(TOKEN_ID);
    }

}
