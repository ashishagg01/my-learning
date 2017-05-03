package org.mylearning.aem.workflow;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component
@Service
@Properties({
        @Property(name = "service.description", value = "A sample implementation of a dynamic participant chooser."),
        @Property(name = "service.vendor", value = "SampleVendor"),
        @Property(name = ParticipantStepChooser.SERVICE_PROPERTY_LABEL, value = "Sample Participant Chooser") })
public class MyDynamicParticipant implements ParticipantStepChooser {

    private static final String TYPE_JCR_PATH = "JCR_PATH";

    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
        WorkflowData workflowData = workItem.getWorkflowData();
        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            // Write your custom code here.
            String path = workflowData.getPayload().toString();
            String pathFromArgument = args.get("PROCESS_ARGS", String.class);
            if (pathFromArgument != null && path.startsWith(pathFromArgument)) {
                return "admin";
            }
        }
        return "administrators";
    }
}