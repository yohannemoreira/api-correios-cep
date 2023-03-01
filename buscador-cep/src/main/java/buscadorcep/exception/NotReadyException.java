package buscadorcep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Serviço indisponível no momento. Por favor, aguarde!") //503
public class NotReadyException extends Exception{
}
