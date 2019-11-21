package com.dimanddim.erentbackend.api.v1.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dimanddim.erentbackend.api.v1.repositories.JwtTokensWhiteListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private long validityInMilliseconds = 7200000; // 2h

    private long shortValidityInMilliseconds = 1200000; // 20m

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokensWhiteListRepository tokensWhiteListRepository;

    @Value("${jwt.secret}")
    private String signKey;

    public String createToken(String username, List<String> roles, boolean shortValidity){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = shortValidity ? new Date(now.getTime() + shortValidityInMilliseconds) 
                                      : new Date(now.getTime() + validityInMilliseconds);


        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

	public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
	}

	public boolean validateToken(String token) {
        if(tokensWhiteListRepository.findById(token).isPresent()){
            try{
                Jws<Claims> claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token);

                if(claims.getBody().getExpiration().before(new Date())){
                    return false;
                }

                return true;
            } catch(JwtException | IllegalArgumentException e) {
                return false;
            }
        }

        return false;
	}

	public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //Throws exception so it MUST be used only after validate token.
    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody().getSubject();
    }

    public Key getSignKey(){
        byte[] keyBytes = Base64.getDecoder().decode(signKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
}
