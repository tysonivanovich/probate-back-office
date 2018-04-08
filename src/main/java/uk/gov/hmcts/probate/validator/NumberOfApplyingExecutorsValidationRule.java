package uk.gov.hmcts.probate.validator;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.exception.model.FieldErrorResponse;
import uk.gov.hmcts.probate.model.ccd.CCDData;
import uk.gov.hmcts.probate.model.ccd.Executor;
import uk.gov.hmcts.probate.service.BusinessValidationMessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Component
class NumberOfApplyingExecutorsValidationRule implements SolExecutorDetailsValidationRule {

    public static final String TOO_MANY_EXECUTORS = "tooManyExecutors";
    public static final int MAX_EXECUTORS = 4;

    private final BusinessValidationMessageService businessValidationMessageService;

    @Override
    public List<FieldErrorResponse> validate(CCDData ccdData) {
        return Optional.ofNullable(ccdData)
            .map(this::getErrorCodeForInvalidNumberOfApplyingExecutors)
            .map(List::stream)
            .orElse(Stream.empty())
            .map(code -> businessValidationMessageService.generateError(BUSINESS_ERROR, code))
            .collect(Collectors.toList());
    }

    private List<String> getErrorCodeForInvalidNumberOfApplyingExecutors(CCDData ccdData) {
        List<String> allErrorCodes = new ArrayList<>();
        long countApplying = ccdData.getExecutors().stream().filter(Executor::isApplying).count();

        if (countApplying > MAX_EXECUTORS) {
            allErrorCodes.add(TOO_MANY_EXECUTORS);
        }

        return allErrorCodes;
    }
}
