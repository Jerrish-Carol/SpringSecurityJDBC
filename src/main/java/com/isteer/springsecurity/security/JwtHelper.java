package com.isteer.springsecurity.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.controller.HomeController;
import com.isteer.springsecurity.dao.UserDaoImpl;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserPrincipal;
import com.isteer.springsecurity.entities.UserResult;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
	
	@Autowired
	private UserDaoImpl uDAO;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    
    private static final Logger auditlogger = LoggerFactory.getLogger(JwtHelper.class);

    private SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf"));

    private String doGenerateToken(Map<String, Object> claims, String subject) {

    	
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(secret, SignatureAlgorithm.HS512).compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        UserResult user = uDAO.getUserWithRoleByEmail(username);
        UserPrincipal userprincipal = new UserPrincipal(user,uDAO);
        return (username.equals(userprincipal.getUsername()) && !isTokenExpired(token));
    }


}
