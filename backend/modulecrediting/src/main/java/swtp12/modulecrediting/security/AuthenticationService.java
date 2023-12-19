package swtp12.modulecrediting.security;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import swtp12.modulecrediting.dto.CredentialsDTO;
import swtp12.modulecrediting.dto.UserDTO;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    @Value("my-secret-key")
    private String secretKey;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO authenticate(CredentialsDTO credentialsDto) {
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), encodedMasterPassword)) {
            return new UserDTO(1L, "admin", "token");
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDTO findByLogin(String login) {
        if ("login".equals(login)) {
            return new UserDTO(1L, "admin", "token");
        }
        throw new RuntimeException("Invalid login");
    }

    public String createToken(UserDTO user) {
        return user.getId() + "&" + user.getUsername() + "&" + calculateHmac(user);
    }

    public UserDTO findByToken(String token) {
        String[] parts = token.split("&");

        Long userId = Long.valueOf(parts[0]);
        String login = parts[1];
        String hmac = parts[2];

        UserDTO userDto = findByLogin(login);

        if (!hmac.equals(calculateHmac(userDto)) || userId != userDto.getId()) {
            throw new RuntimeException("Invalid Cookie value");
        }

        return userDto;
    }


    private String calculateHmac(UserDTO user) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(user.getId() + "&" + user.getUsername()).getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}