package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.model.ccd.ocr.ValidationResponse;
import uk.gov.hmcts.probate.model.ccd.ocr.ValidationResponseStatus;
import uk.gov.hmcts.probate.model.ocr.OCRRequest;
import uk.gov.hmcts.probate.service.ocr.FormType;
import uk.gov.hmcts.probate.service.ocr.OCRMapper;
import uk.gov.hmcts.probate.service.ocr.OCRToCCDMandatoryField;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/forms", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_VALUE)
@RestController
@Api(tags = "Manage bulk scanning data")
public class OCRFormsController {

    private final OCRMapper ocrMapper;
    private final OCRToCCDMandatoryField ocrToCCDMandatoryField;

    @ApiOperation(value = "Pre-validate OCR data", notes = "Will return validation errors as warnings. ")
    @ApiResponses({
            @ApiResponse(code = 200, response = ValidationResponse.class, message = "Validation executed successfully"),
            @ApiResponse(code = 400, message = "Request failed due to malformed syntax"),
            @ApiResponse(code = 403, message = "S2S token is not authorized, missing or invalid"),
            @ApiResponse(code = 404, message = "Form type not found")
    })
    @PostMapping(path = "/{form-type}/validate-ocr", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ValidationResponse> validateExceptionRecord(@PathVariable("form-type") String formType,
                                                                      @Valid @RequestBody OCRRequest ocrRequest) {
        log.info("Validate ocr data for form type: {}", formType);
        FormType.isFormTypeValid(formType);
        List<String> warnings = ocrToCCDMandatoryField
                .ocrToCCDMandatoryFields(ocrMapper.ocrMapper(ocrRequest.getOcrFields()),
                        FormType.valueOf(formType));

        ValidationResponse validationResponse =
                ValidationResponse.builder().warnings(warnings)
                        .status(warnings.isEmpty() ? ValidationResponseStatus.SUCCESS : ValidationResponseStatus.WARNINGS).build();
        return ResponseEntity.ok(validationResponse);
    }
}
