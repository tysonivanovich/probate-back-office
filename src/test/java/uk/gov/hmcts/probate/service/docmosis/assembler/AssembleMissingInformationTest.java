package uk.gov.hmcts.probate.service.docmosis.assembler;

import org.junit.Test;
import uk.gov.hmcts.probate.model.ccd.raw.ParagraphDetail;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AssembleMissingInformationTest {

    private AssemblerBase assemblerBase = new AssemblerBase();

    private AssembleMissingInformation assembleMissingInformation = new AssembleMissingInformation(assemblerBase);

    @Test
    public void testMissingInfoWill() {

        CaseData caseData = CaseData.builder().build();

        List<ParagraphDetail> response = assembleMissingInformation.missingInfoWill(ParagraphCode.MissInfoWill, caseData);
        assertEquals("MissInfoWill", response.get(0).getCode());
        assertEquals("FL-PRB-GNO-ENG-00126.docx", response.get(0).getTemplateName());
        assertEquals("Text", response.get(0).getEnableType().name());
        assertEquals("Original Will or Codicil", response.get(0).getLabel());
        assertEquals("Will / Codicil", response.get(0).getTextValue());
        assertEquals(null, response.get(0).getTextAreaValue());
        assertEquals(null, response.get(0).getDynamicList());
    }

    @Test
    public void testMissingInfoDeathCert() {

        CaseData caseData = CaseData.builder().build();
        List<ParagraphDetail> response = assembleMissingInformation.missingInfoDeathCert(ParagraphCode.MissInfoDeathCert, caseData);
        assertEquals("MissInfoDeathCert", response.get(0).getCode());
        assertEquals("FL-PRB-GNO-ENG-00128.docx", response.get(0).getTemplateName());
        assertEquals("Text", response.get(0).getEnableType().name());
        assertEquals("The one supplied was unclear / One was not supplied", response.get(0).getTextValue());
        assertEquals("Death Certificate", response.get(0).getLabel());
        assertEquals(null, response.get(0).getTextAreaValue());
        assertEquals(null, response.get(0).getDynamicList());
    }

    @Test
    public void testMissingInfoChangeOfApplicant() {

        CaseData caseData = CaseData.builder().primaryApplicantForenames("primary fn").primaryApplicantSurname("primary sn").build();

        List<ParagraphDetail> response =
                assembleMissingInformation.missingInfoChangeOfApplicant(ParagraphCode.MissInfoChangeApp, caseData);
        assertEquals("MissInfoChangeApp", response.get(0).getCode());
        assertEquals("FL-PRB-GNO-ENG-00127.docx", response.get(0).getTemplateName());
        assertEquals("Text", response.get(0).getEnableType().name());
        assertEquals("Name change of applicant", response.get(0).getLabel());
        assertEquals("primary fn primary sn", response.get(0).getTextValue());
        assertEquals(null, response.get(0).getTextAreaValue());
        assertEquals(null, response.get(0).getDynamicList());
    }
}
