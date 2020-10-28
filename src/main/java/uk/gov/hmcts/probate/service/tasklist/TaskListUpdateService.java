package uk.gov.hmcts.probate.service.tasklist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseDetails;
import uk.gov.hmcts.probate.model.ccd.raw.response.ResponseCaseData.ResponseCaseDataBuilder;
import uk.gov.hmcts.probate.model.ccd.tasklist.Alert;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListUpdateService {

    public ResponseCaseDataBuilder generateTaskList(CaseDetails caseDetails, ResponseCaseDataBuilder builder) {

        BaseTaskListRenderer progressTabRenderer = TaskListRendererFactory.getTaskListRenderer(caseDetails.getState());
        String progressTabHtml = progressTabRenderer.renderHtml(caseDetails);
        builder.taskList(progressTabHtml);

        return builder;
    }
}
