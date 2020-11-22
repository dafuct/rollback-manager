package com.makarenko.userservice.exception;

import com.makarenko.userservice.service.RollBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.makarenko.userservice.util.ErrorMessages.COMMON_ERROR;

@ControllerAdvice
public class UserExceptionHandler {

    @Autowired
    private RollBackService rollBackService;

    @ExceptionHandler({UserException.class})
    public ResponseEntity<?> handleRuntimeException(UserException exception, HttpServletRequest req) {
        String txId = req.getParameter("txId");
        rollBackService.sendErrorMessage(txId);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleHibernateException(HttpServletRequest req) {
        String txId = req.getParameter("txId");
        rollBackService.sendErrorMessage(txId);
        return ResponseEntity.badRequest().body(COMMON_ERROR.getMessage());
    }
}
