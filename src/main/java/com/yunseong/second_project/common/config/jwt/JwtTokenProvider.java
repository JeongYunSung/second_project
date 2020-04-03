package com.yunseong.second_project.common.config.jwt;

import com.yunseong.second_project.member.command.application.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("spring.jwt.secret")
    private String secret;

    private long tokenValidMilisecond = 1000L * 60 * 30;

    private final MemberDetailsService memberDetailsService;

    @PostConstruct
    public void init() {
        this.secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
    }

    public String createToken(Long id, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.ES256, this.secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.memberDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String getId(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
