package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.model.TransactionModel;
import br.uefs.pbl_redes_3.service.RequestService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping ("/request")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping()
    public String receive(@RequestBody TransactionModel request){
        requestService.receive(request);
        return "";
    }
}
