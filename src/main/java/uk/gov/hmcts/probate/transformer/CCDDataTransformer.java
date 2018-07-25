package uk.gov.hmcts.probate.transformer;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.model.ccd.CCDData;
import uk.gov.hmcts.probate.model.ccd.Deceased;
import uk.gov.hmcts.probate.model.ccd.Executor;
import uk.gov.hmcts.probate.model.ccd.Fee;
import uk.gov.hmcts.probate.model.ccd.InheritanceTax;
import uk.gov.hmcts.probate.model.ccd.Solicitor;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.request.CallbackRequest;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static uk.gov.hmcts.probate.model.Constants.YES;
import static uk.gov.hmcts.probate.transformer.CallbackResponseTransformer.PAYMENT_METHOD_VALUE_FEE_ACCOUNT;
import static uk.gov.hmcts.probate.transformer.CallbackResponseTransformer.PAYMENT_REFERENCE_CHEQUE;
import static uk.gov.hmcts.probate.transformer.CallbackResponseTransformer.PAYMENT_REFERENCE_FEE_PREFIX;

@Data
@Component
public class CCDDataTransformer {

    private static final Logger log = LoggerFactory.getLogger(CCDDataTransformer.class);

    public CCDData transform(CallbackRequest callbackRequest) {
        CaseData caseData = callbackRequest.getCaseDetails().getData();

        Solicitor solicitor = Solicitor.builder()
                .firmName(caseData.getSolsSolicitorFirmName())
                .firmPostcode(caseData.getSolsSolicitorFirmPostcode())
                .fullname(caseData.getSolsSOTName())
                .jobRole(caseData.getSolsSOTJobTitle())
                .build();

        Deceased deceased = Deceased.builder()
                .firstname(caseData.getDeceasedForenames())
                .lastname(caseData.getDeceasedSurname())
                .dateOfBirth((caseData.getDeceasedDateOfBirth()))
                .dateOfDeath((caseData.getDeceasedDateOfDeath()))
                .address(caseData.getDeceasedAddress())
                .build();

        InheritanceTax inheritanceTax = InheritanceTax.builder()
                .formName(caseData.getSolsIHTFormId())
                .netValue(caseData.getIhtNetValue())
                .grossValue(caseData.getIhtGrossValue())
                .build();

        Fee fee = Fee.builder()
                .extraCopiesOfGrant(caseData.getExtraCopiesOfGrant())
                .outsideUKGrantCopies(caseData.getOutsideUKGrantCopies())
                .paymentMethod(caseData.getSolsPaymentMethods())
                .paymentReferenceNumber(getPaymentReferenceNumber(caseData))
                .applicationFee(caseData.getApplicationFee())
                .amount(caseData.getTotalFee())
                .feeForUkCopies(caseData.getFeeForUkCopies())
                .feeForNonUkCopies(caseData.getFeeForNonUkCopies())
                .build();

        return CCDData.builder()
                .solicitorReference(getSolicitorAppReference(caseData.getSolsSolicitorAppReference()))
                .caseSubmissionDate(getCaseSubmissionDate(callbackRequest.getCaseDetails().getLastModified()))
                .solicitor(solicitor)
                .deceased(deceased)
                .iht(inheritanceTax)
                .fee(fee)
                .solsAdditionalInfo(caseData.getSolsAdditionalInfo())
                .executors(getAllExecutors(caseData))
                .build();
    }

    private String getPaymentReferenceNumber(CaseData caseData) {
        if (PAYMENT_METHOD_VALUE_FEE_ACCOUNT.equals(caseData.getSolsPaymentMethods())) {
            return PAYMENT_REFERENCE_FEE_PREFIX + caseData.getSolsFeeAccountNumber();
        } else {
            return PAYMENT_REFERENCE_CHEQUE;
        }
    }

    private String getSolicitorAppReference(String solsSolicitorAppReference) {
        return solsSolicitorAppReference == null ? "" : solsSolicitorAppReference;
    }

    private List<Executor> getAllExecutors(CaseData caseData) {
        List<Executor> executors = new ArrayList<>();
        if (caseData.getSolsAdditionalExecutorList() != null) {
            executors = caseData.getSolsAdditionalExecutorList().stream()
                    .map(CollectionMember::getValue)
                    .map(executor -> Executor.builder()
                            .applying(YES.equals(executor.getAdditionalApplying()))
                            .address(executor.getAdditionalExecAddress())
                            .reasonNotApplying(executor.getAdditionalExecReasonNotApplying())
                            .forename(executor.getAdditionalExecForenames())
                            .lastname(executor.getAdditionalExecLastname())
                            .build())
                    .collect(Collectors.toList());
        }

        Executor primaryExecutor = Executor.builder()
                .applying(caseData.isPrimaryApplicantApplying())
                .address(caseData.getPrimaryApplicantAddress())
                .reasonNotApplying(caseData.getSolsPrimaryExecutorNotApplyingReason())
                .forename(caseData.getPrimaryApplicantForenames())
                .lastname(caseData.getPrimaryApplicantSurname())
                .build();

        executors.add(primaryExecutor);

        return executors;
    }

    private LocalDate getCaseSubmissionDate(String[] lastModified) {
        try {
            return LocalDate.of(parseInt(lastModified[0]), parseInt(lastModified[1]), parseInt(lastModified[2]));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | DateTimeException | NullPointerException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }
}
