package tr.com.burakgul.securitydemo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    @Value("${app.jwt.secret.key}")
    private String secretKey;

    @Value("${app.jwt.expire.minutes}")
    private Integer expireMinutes;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (this.expireMinutes * 60*1000)))
                .signWith(SignatureAlgorithm.HS512,this.secretKey)
                .compact();
    }

    private Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean hasTokenNotExpire(String token){
        Claims claims = this.parseToken(token);
        Date now = new Date(System.currentTimeMillis());
        return claims.getExpiration().after(now);
    }

    public String getUsernameFromToken(String token){
        Claims claims = this.parseToken(token);
        return claims.getSubject();
    }

    public boolean hasTokenValid(String token){
        if(getUsernameFromToken(token) != null && hasTokenNotExpire(token)) return true;
        return false;
    }
}
