package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.service.JointAccountTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/joint/transfer")

public class JointAccountTransferController {
    private final JointAccountTransferService jointAccountTransferService;

    public JointAccountTransferController(JointAccountTransferService jointAccountTransferService) {
        this.jointAccountTransferService = jointAccountTransferService;
    }

    @PostMapping()
    public TransferResponse transfer(@RequestBody TransferRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            throw new RequestException(HttpStatus.FORBIDDEN, "ACCESS TOKEN NOT FOUND");
        }
        if (token.isEmpty() || token.isBlank()) {
            throw new RequestException(HttpStatus.FORBIDDEN, "ACCESS TOKEN NOT FOUND");
        }

        return jointAccountTransferService.create(request, token);
    }
}
