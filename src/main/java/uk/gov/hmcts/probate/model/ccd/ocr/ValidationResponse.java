package uk.gov.hmcts.probate.model.ccd.ocr;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ValidationResponse {

    private ValidationResponseState state;
    private List<String> errors;
    private List<String> warnings;
}
