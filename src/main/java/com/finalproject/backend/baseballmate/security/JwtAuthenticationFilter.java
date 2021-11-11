package com.finalproject.backend.baseballmate.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request); // 헤더에서 JWT 를 받아옵니다.
        if(token !=null){
            // 클라이언트에서 받은 token값을 서버 콘솔에 찍어줌.
        System.out.print("Server provided token:");
        System.out.println(token);

        }
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // validateToken의 결과가 True이고
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 토큰 인증과정을 거친 결과를 authentication이라는 이름으로 저장해줌.

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            // token이 인증된 상태를 유지하도록 context(맥락)을 유지해주는 부분임.
        }
        chain.doFilter(request, response); //request 받아서 filter를 거친 결과를 response로 내려줌.


    }
}
