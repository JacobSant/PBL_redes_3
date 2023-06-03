package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.JointAccountRepository;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.LoginRequest;
import br.uefs.pbl_redes_3.response.LoginResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final PrivateAccountRepository privateAccountRepository;
    private final JointAccountRepository jointAccountRepository;

    public TokenService(final ClientRepository clientRepository,
                        final TokenRepository tokenRepository,
                        final ModelMapper modelMapper,
                        final PrivateAccountRepository privateAccountRepository,
                        final JointAccountRepository jointAccountRepository) {
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
        this.privateAccountRepository = privateAccountRepository;
        this.jointAccountRepository = jointAccountRepository;
    }


    public LoginResponse createPrivateAccountToken(LoginRequest request) {
        int clientCpf = Integer.parseInt(request.getCpf());
        int accountNumber = Integer.parseInt(request.getNumberAccount());
        Optional<ClientModel> clientOptional = clientRepository.findByCpf(clientCpf);
        if (clientOptional.isPresent()) {
            ClientModel client = clientOptional.get();
            Optional<PrivateAccountModel> accountOptional = privateAccountRepository.findByAccountNumber(accountNumber);
            if(accountOptional.isPresent()){
                PrivateAccountModel privateAccount = accountOptional.get();
                if (authenticate(client,privateAccount, request)) {
                    TokenModel token = new TokenModel();
                    Date currentDate = new Date();
                    token.setClientId(client.getId());
                    token.setAccountId(privateAccount.getId());
                    token.setIssuedAt(currentDate);
                    token.setExpiresAt(currentDate.getTime() + 500000000);
                    token.setToken(generateToken());
                    tokenRepository.save(token);
                    return modelMapper.map(token, LoginResponse.class);
                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "NOT AUTHENTICATED");
                }
            }else{
                throw new RequestException(HttpStatus.NOT_FOUND,"PRIVATE ACCOUNT");
            }
        } else {
            throw new RequestException(HttpStatus.NOT_FOUND, "CLIENT");
        }
    }

    public LoginResponse createJointAccountToken(LoginRequest request) {
        int clientCpf = Integer.parseInt(request.getCpf());
        int accountNumber = Integer.parseInt(request.getNumberAccount());
        Optional<ClientModel> clientOptional = clientRepository.findByCpf(clientCpf);
        if (clientOptional.isPresent()) {
            ClientModel client = clientOptional.get();
            Optional<JointAccountModel> accountOptional = jointAccountRepository.findByAccountNumber(accountNumber);
            if(accountOptional.isPresent()){
                JointAccountModel jointAccount = accountOptional.get();
                if (authenticate(client,jointAccount, request)) {
                    TokenModel token = new TokenModel();
                    Date currentDate = new Date();
                    token.setClientId(client.getId());
                    token.setAccountId(jointAccount.getId());
                    token.setIssuedAt(currentDate);
                    token.setExpiresAt(currentDate.getTime() + 500000000);
                    token.setToken(generateToken());
                    tokenRepository.save(token);
                    return modelMapper.map(token, LoginResponse.class);
                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "NOT AUTHENTICATED");
                }
            }else{
                throw new RequestException(HttpStatus.NOT_FOUND,"PRIVATE ACCOUNT");
            }
        } else {
            throw new RequestException(HttpStatus.NOT_FOUND, "CLIENT");
        }
    }

    private String generateToken() {
        String token = "";
        byte[] bytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] messageDigest = md.digest();
            BigInteger number = new BigInteger(1, messageDigest);
            String hashText = number.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            token = hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    private boolean authenticate(ClientModel client, PrivateAccountModel privateAccount, LoginRequest request) {
        return client.getCpf() == Integer.parseInt(request.getCpf())
                && client.getPassword().equals(request.getPassword())
                && privateAccount.getAccountNumber() == Integer.parseInt(request.getNumberAccount());
    }

    private boolean authenticate(ClientModel client, JointAccountModel jointAccount, LoginRequest request) {
        return client.getCpf() == Integer.parseInt(request.getCpf())
                && client.getPassword().equals(request.getPassword())
                && jointAccount.getAccountNumber() == Integer.parseInt(request.getNumberAccount());
    }
}
