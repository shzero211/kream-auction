package site.shkrr.kreamAuction.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.exception.user.DuplicateEmailException;
import site.shkrr.kreamAuction.exception.user.DuplicatePhoneNumException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity handleDuplicateEmailException(DuplicateEmailException ex){
        log.debug(String.valueOf(ex));
        return  Utils.ResponseUtil.ofException(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePhoneNumException.class)
    public ResponseEntity handleDuplicatePhoneNumException(DuplicatePhoneNumException ex){
        log.debug(String.valueOf(ex));
        return  Utils.ResponseUtil.ofException(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.debug(String.valueOf(ex));

        BindingResult bindingResult=ex.getBindingResult();

        Map<String,String> map=new HashMap<>();

        for(FieldError fieldError: bindingResult.getFieldErrors()){
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }

    return Utils.ResponseUtil.ofException(map);
    }
}
