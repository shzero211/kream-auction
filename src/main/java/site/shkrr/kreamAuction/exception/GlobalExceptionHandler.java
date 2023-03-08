package site.shkrr.kreamAuction.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.exception.brand.BrandNotFoundException;
import site.shkrr.kreamAuction.exception.brand.DuplicateBrandNameException;
import site.shkrr.kreamAuction.exception.brand.UpLoadBrandImgFailException;
import site.shkrr.kreamAuction.exception.payment.PaymentException;
import site.shkrr.kreamAuction.exception.payment.RequestBillingKeyException;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationKeyIsNullException;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationNumNotMatchException;
import site.shkrr.kreamAuction.exception.user.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity handleDuplicateEmailException(DuplicateEmailException ex){
        log.debug(String.valueOf(ex));
        return  Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePhoneNumException.class)
    public ResponseEntity handleDuplicatePhoneNumException(DuplicatePhoneNumException ex){
        log.debug(String.valueOf(ex));
        return  Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.debug(String.valueOf(ex));

        BindingResult bindingResult=ex.getBindingResult();

        Map<String,String> map=new HashMap<>();

        for(FieldError fieldError: bindingResult.getFieldErrors()){
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
    return Utils.response.ofException(map);
    }

    @ExceptionHandler(JsonToMapException.class)
    public ResponseEntity handleJsonToMapException(JsonToMapException ex){
        log.debug(String.valueOf(ex));
        return  Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(MapToJsonException.class)
    public ResponseEntity handleMapToJsonException(MapToJsonException ex){
        log.debug(String.valueOf(ex));
        return  Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(CertificationNumNotMatchException.class)
    public ResponseEntity handleSmsCertificationNumException(CertificationNumNotMatchException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(CertificationKeyIsNullException.class)
    public ResponseEntity handleCertificationKeyIsNullException(CertificationKeyIsNullException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(LoginEmailHasNotEntityException.class)
    public ResponseEntity handleLoginEmailHasNotEntityException(LoginEmailHasNotEntityException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(LoginPasswordNotMatchException.class)
    public ResponseEntity handleLoginPasswordNotMatchException(LoginPasswordNotMatchException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(LoginRefreshNotFoundUser.class)
    public ResponseEntity handleLoginRefreshNotFoundUser(LoginRefreshNotFoundUser ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenIsNotValid.class)
    public ResponseEntity handleRefreshTokenIsNotValid(RefreshTokenIsNotValid ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(EmailNotSignUpException.class)
    public ResponseEntity handleEmailNotSignUpException(EmailNotSignUpException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(ChangePasswordUserNotFoundException.class)
    public ResponseEntity handleChangePasswordUserNotFoundException(ChangePasswordUserNotFoundException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler( BeforePasswordNotMatchException.class)
    public ResponseEntity handleBeforePasswordNotMatchException(BeforePasswordNotMatchException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(DuplicateBrandNameException.class)
    public ResponseEntity handleDuplicateBrandNameException(DuplicateBrandNameException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(UpLoadBrandImgFailException.class)
    public ResponseEntity handleUpLoadBrandImgFailException(UpLoadBrandImgFailException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity handleBrandNotFoundException(BrandNotFoundException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(RequestBillingKeyException.class)
    public ResponseEntity handleBrandNotFoundException(RequestBillingKeyException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofException(ex.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity handlePaymentException(PaymentException ex){
        log.debug(String.valueOf(ex));
        return Utils.response.ofExceptionForAPI(ex.getErrorResponse());
    }
}
