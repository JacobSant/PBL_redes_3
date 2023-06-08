package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/private/transfer")
public class PrivateAccountTransferController {
    private final PrivateAccountTransferService privateAccountTransferService;

    public PrivateAccountTransferController(PrivateAccountTransferService privateAccountTransferService) {
        this.privateAccountTransferService = privateAccountTransferService;
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

        return privateAccountTransferService.create(request, token);
    }
}
