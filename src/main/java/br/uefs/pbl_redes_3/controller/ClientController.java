package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.ClientRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import br.uefs.pbl_redes_3.service.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    private final ClientService clientService;

    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ClientResponse create(final ClientRequest request) {
        return clientService.create(request);
    }
}
