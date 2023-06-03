package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.request.DepositRequest;
import br.uefs.pbl_redes_3.response.DepositResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountDepositService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/private/deposit")
public class PrivateAccountDepositController {
    private final PrivateAccountDepositService privateAccountDepositService;

    public PrivateAccountDepositController(PrivateAccountDepositService privateAccountDepositService) {
        this.privateAccountDepositService = privateAccountDepositService;
    }

    @PostMapping()
    public DepositResponse create(@RequestBody DepositRequest request, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        if(token == null){
            throw new RequestException(HttpStatus.FORBIDDEN,"ACCESS TOKEN NOT FOUND");
        }
        if (token.isEmpty() || token.isBlank()){
            throw new RequestException(HttpStatus.FORBIDDEN,"ACCESS TOKEN NOT FOUND");
        }

        return privateAccountDepositService.create(request,token);
    }
}
