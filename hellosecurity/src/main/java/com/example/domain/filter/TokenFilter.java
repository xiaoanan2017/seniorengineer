package com.example.domain.filter;

import com.example.domain.entity.LoginUser;
import com.example.domain.entity.User;
import com.example.domain.exception.BizException;
import com.example.domain.mapper.UserMapper;
import com.example.domain.util.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** 定义 JWT token 过滤器
 * 自定义Filter 有很多种方式，比如
 * implements Filter
 * extends GenericFilter
 * 但这些都会有个问题就是 Filter可能会重跑2次 （加入securityFilterChain 会执行一次，被 代理类引入servlet容器之后也会执行一次）
 * 推荐继承 spring的 OncePerRequestFilter 可以保证过滤器只会跑一次

 *
 * @author xiaoaa
 * @date 2023/5/20 14:55
 **/
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private UserMapper userMapper;

    /**
     * 1.获取token
     * 2.解析 token 中的 userId
     * 3.从redis中获取用户信息
     * 4.存入 SecurityContextHolder
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //没token 当前过滤器直接放行 （后续由security其他过滤器进行处理）
            filterChain.doFilter(request, response);
            return;
        }
        Integer userId = JwtUtils.getUserId(token);
        //根据userId 获取用户信息
        User user = userMapper.getUserById(Long.valueOf(userId));
        if (Objects.isNull(user)) {
            throw new BizException("用户未登录");
        }
        // 存入 securityContextHolder
        // 这个构造参数会执行 super.setAuthenticated(true) 表示已认证
        List<String> list = new ArrayList<>(Arrays.asList("test1", "admin"));
        LoginUser loginUser = new LoginUser(user, list);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser.getUsername(), loginUser.getPassword(), loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //todo 从redis中获取
        //继续执行后续过滤器
        filterChain.doFilter(request, response);
    }
}
