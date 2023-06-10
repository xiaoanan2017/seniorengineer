package com.example.domain.service;

import com.example.domain.entity.LoginUser;
import com.example.domain.entity.Result;
import com.example.domain.entity.Token;
import com.example.domain.entity.User;
import com.example.domain.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author xiaoaa
 * @date 2023/5/20 15:44
 **/
@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public Result<Token> login(User user) {
        /** 构建 用户密码待验证token */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUserName(),user.getPassword());
        /** 直接将账号密码交给 认证器进行验证 */
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // if (Objects.isNull(authenticate)) {
        //     throw new BizException("用户名或密码错误");
        // }
        //认证通过 从认证结果中拿出 完整的User信息 使用userId 生成 jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Token token = JwtUtils.getJwtToken(loginUser.getUser());
        //todo 将user 存入redis

        return Result.success(token);
    }

    /**
     * 登出接口
     * @return
     */
    public Result<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //登出接口也需要鉴权 authentication不为null
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //todo 从redis 中删除对应的用户信息
        return Result.success("登出成功");
    }
}
