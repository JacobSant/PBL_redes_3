package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.ClientRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import br.uefs.pbl_redes_3.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;


    public ClientController(final ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public ClientResponse create(@RequestBody final ClientRequest request ) {

        return clientService.create(request);
    }

    @GetMapping()
    public ClientResponse findByEmail(@RequestParam("email") final String email){
        return clientService.findByEmail(email);
    }
}
