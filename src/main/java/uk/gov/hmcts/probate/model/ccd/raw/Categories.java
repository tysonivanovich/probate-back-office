package uk.gov.hmcts.probate.model.ccd.raw;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class Categories {

    private final List<String> selectedCategories = new ArrayList();
    private final List<String> entSelectedParagraphs = new ArrayList();
    private final List<String> ihtSelectedParagraphs = new ArrayList();
    private final List<String> missInfoSelectedParagraphs = new ArrayList();
    private final List<String> willSelectedParagraphs = new ArrayList();
    private final List<String> incapacitySelectedParagraphs = new ArrayList();
    private final List<String> forDomSelectedParagraphs = new ArrayList();
    private final List<String> lifeAndMinoritySelectedParagraphs = new ArrayList();
    private final List<String> sotSelectedParagraphs = new ArrayList();
    private final List<String> witnessSelectedParagraphs = new ArrayList();
    private final List<String> solsGeneralSelectedParagraphs = new ArrayList();
    private final List<String> solsCertsSelectedParagraphs = new ArrayList();
    private final List<String> solsAffidavitSelectedParagraphs = new ArrayList();
    private final List<String> solsRedeclareSelectedParagraphs = new ArrayList();

    @JsonIgnore
    public List<List<String>> getAllSelectedCategories() {
        return Arrays.asList(entSelectedParagraphs, ihtSelectedParagraphs, missInfoSelectedParagraphs,
                willSelectedParagraphs, forDomSelectedParagraphs, incapacitySelectedParagraphs,
                lifeAndMinoritySelectedParagraphs, sotSelectedParagraphs, witnessSelectedParagraphs,
                solsGeneralSelectedParagraphs, solsCertsSelectedParagraphs, solsAffidavitSelectedParagraphs,
                solsRedeclareSelectedParagraphs);
    }
}
