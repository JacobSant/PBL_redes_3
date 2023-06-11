package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.exception.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class RestTemplateResponseExceptionHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode()
                .series() == SERVER_ERROR) {
            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RequestException(HttpStatus.NOT_FOUND,"PRIVATE ACCOUNT");
            }else if(httpResponse.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new RequestException(HttpStatus.UNAUTHORIZED,"INSUFFICIENT BALANCE");
            } else if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RequestException(HttpStatus.BAD_REQUEST,"INVALID BANK ID");
            }
        }
    }
}