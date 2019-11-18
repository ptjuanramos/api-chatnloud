/*
 * Copyright 2019
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.chatnloud.api.exception.handler;

import com.chatnloud.api.model.response.ResponseError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;

public class ExceptionHandlerUtil {

    public static List<ResponseError.SubError> buildListSubErrors(List<FieldError> errors) {
        if(errors == null) {
            return null;
        }

        List<ResponseError.SubError> subErrors = new ArrayList<>();
        errors.forEach(e -> {
            ResponseError.SubError newSubError = new ResponseError.SubError();
            newSubError.setField(e.getField());
            newSubError.setError(e.getRejectedValue().toString());
        });

        return subErrors;
    }

    /**
     *
     * @param mainMessage
     * @param status
     * @param fieldErrors
     * @return
     */
    public static ResponseEntity<Object> wrapExceptionInformation(String mainMessage, HttpStatus status, List<FieldError> fieldErrors) {
        ResponseError responseError = new ResponseError();
        responseError.setMessage(mainMessage);
        responseError.setStatus(status);
        responseError.setTimestamp(LocalDateTime.now());
        responseError.setSubErrors(buildListSubErrors(fieldErrors));

        return new ResponseEntity<>(responseError, status);
    }

    /**
     *
     * @param mainMessage
     * @param status
     * @return
     */
    public static ResponseEntity<Object> wrapExceptionInformation(String mainMessage, HttpStatus status) {
        return wrapExceptionInformation(mainMessage, status, null);
    }
}
