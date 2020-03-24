package uk.gov.hmcts.probate.service.exceptionrecord.mapper;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.probate.model.exceptionrecord.ExceptionRecordOCRFields;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;

import static org.junit.Assert.assertEquals;

public class ApplicationTypeMapperTest {

    private static final String SOLS_SOLICITORS_FIRM_NAME = "Chapman and Sons";
    private static final String SOLS_SOLICITORS_REPRESENTITIVE_NAME = "Bob Chapman";

    private ApplicationTypeMapper applicationTypeMapper = new ApplicationTypeMapper();

    private ExceptionRecordOCRFields ocrFields;
    private ExceptionRecordOCRFields ocrFieldsWithSolicitor;
    private ExceptionRecordOCRFields ocrFieldsWithSolicitorNoRepName;
    private ExceptionRecordOCRFields ocrFieldsWithSolicitorNoFirmName;

    @Before
    public void setUpClass() throws Exception {
        ocrFields = ExceptionRecordOCRFields.builder()
                .build();

        ocrFieldsWithSolicitor = ExceptionRecordOCRFields.builder()
                .solsSolicitorFirmName(SOLS_SOLICITORS_FIRM_NAME)
                .solsSolicitorRepresentativeName(SOLS_SOLICITORS_REPRESENTITIVE_NAME)
                .build();

        ocrFieldsWithSolicitorNoRepName = ExceptionRecordOCRFields.builder()
                .solsSolicitorFirmName(SOLS_SOLICITORS_FIRM_NAME)
                .solsSolicitorRepresentativeName(null)
                .build();

        ocrFieldsWithSolicitorNoFirmName = ExceptionRecordOCRFields.builder()
                .solsSolicitorFirmName("")
                .solsSolicitorRepresentativeName(SOLS_SOLICITORS_REPRESENTITIVE_NAME)
                .build();
    }

    @Test
    public void testApplicationTypeCaveatIsPersonal() {
        ApplicationType applicationType = applicationTypeMapper.toApplicationTypeCaveat(ocrFields);
        assertEquals(ApplicationType.PERSONAL, applicationType);
    }

    @Test
    public void testApplicationTypeCaveatIsSolicitor() {
        ApplicationType applicationType = applicationTypeMapper.toApplicationTypeCaveat(ocrFieldsWithSolicitor);
        assertEquals(ApplicationType.SOLICITORS, applicationType);
    }

    @Test
    public void testApplicationTypeCaveatIsSolicitorMissingFirmName() {
        ApplicationType applicationType = applicationTypeMapper.toApplicationTypeCaveat(ocrFieldsWithSolicitorNoFirmName);
        assertEquals(ApplicationType.SOLICITORS, applicationType);
    }

    @Test
    public void testApplicationTypeCaveatIsSolicitorMissingRepName() {
        ApplicationType applicationType = applicationTypeMapper.toApplicationTypeCaveat(ocrFieldsWithSolicitorNoRepName);
        assertEquals(ApplicationType.SOLICITORS, applicationType);
    }
}
