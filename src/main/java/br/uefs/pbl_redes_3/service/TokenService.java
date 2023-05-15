package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RegisterException;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.LoginRequest;
import br.uefs.pbl_redes_3.response.LoginResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;

    public TokenService(final ClientRepository clientRepository, final TokenRepository tokenRepository, final ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
    }


    public LoginResponse create(LoginRequest request) {
        Optional<ClientModel> expectedClient = clientRepository.findByEmail(request.getEmail());
        if (expectedClient.isPresent()) {
            ClientModel client = expectedClient.get();
            if (authenticate(client, request)) {
                TokenModel token = new TokenModel();
                Date currentDate = new Date();
                token.setClientId(client.getId());
                token.setIssuedAt(currentDate);
                token.setExpiresAt(currentDate.getTime() + 500000000);
                token.setToken(generateToken());
                tokenRepository.save(token);
                return modelMapper.map(token, LoginResponse.class);
            } else {
                throw new RegisterException(HttpStatus.UNAUTHORIZED, "NOT AUTHENTICATED");
            }
        } else {
            throw new RegisterException(HttpStatus.NOT_FOUND, "CLIENT NOT FOUND");
        }
    }

    private String generateToken() {
        String token = "";
        byte[] bytes = new byte[128];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            token = new String(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    private boolean authenticate(ClientModel client, LoginRequest request) {
        return client.getEmail().equals(request.getEmail()) && client.getPassword().equals(request.getPassword());
    }
}
