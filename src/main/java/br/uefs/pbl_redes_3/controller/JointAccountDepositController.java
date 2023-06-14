package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.request.DepositRequest;
import br.uefs.pbl_redes_3.response.DepositResponse;
import br.uefs.pbl_redes_3.service.JointAccountDepositService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/joint/deposit")
public class JointAccountDepositController {
    private final JointAccountDepositService jointAccountDepositService;

    public JointAccountDepositController(JointAccountDepositService jointAccountDepositService) {
        this.jointAccountDepositService = jointAccountDepositService;
    }

    @PostMapping()
    public DepositResponse create(@RequestBody DepositRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            throw new RequestException(HttpStatus.FORBIDDEN, "ACCESS TOKEN NOT FOUND");
        }
        if (token.isEmpty() || token.isBlank()) {
            throw new RequestException(HttpStatus.FORBIDDEN, "ACCESS TOKEN NOT FOUND");
        }
        return jointAccountDepositService.create(request, token);
    }
}
