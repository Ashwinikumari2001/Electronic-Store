package com.lcwd.electronic.store.ElectronicStore.exception;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFound ex){

        logger.info("Exception Handler Invoked");
        ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message(ex.getMessage())
                    .success(true)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseMessage);

    }
   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidExecption(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors=ex.getBindingResult().getAllErrors();
        HashMap<String,Object> map=new HashMap<>();
        allErrors.stream().forEach(objectError->{
            String message= objectError.getDefaultMessage();
            String field=((FieldError)objectError).getField();
            map.put(field,message);
        });
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequest ex){
        logger.info("Bad Api Request");
        ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message(ex.getMessage())
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseMessage);

    }
}
