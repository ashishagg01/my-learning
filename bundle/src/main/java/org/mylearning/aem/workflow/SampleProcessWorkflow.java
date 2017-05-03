package org.mylearning.aem.workflow;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;


@Component(
        label = "CQ Workflow Process",
        description = "Sample Workflow Process implementation",
        metatype = false,
        immediate = false)
@Properties({
        @Property(
                name = "service.description",
                value = "CQ Workflow Process implementation.",
                propertyPrivate = true
        ),
        @Property(
                label = "Workflow Label",
                name = "process.label",
                value = "CQ Workflow Process",
                description = "CQ Workflow Process description"
        )
})
@Service
public class SampleProcessWorkflow implements WorkflowProcess {
    @Reference
    ResourceResolverFactory resourceResolverFactory;

    private static final Logger log = LoggerFactory.getLogger(SampleProcessWorkflow.class);

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {

        final WorkflowData workflowData = workItem.getWorkflowData();
        final String type = workflowData.getPayloadType();
        if (!type.equals("JCR_PATH")) {
            return;
        }
        final String path = workflowData.getPayload().toString();
        try {
            // Node node = workflowSession.getNode(path);
            /* Write your custom code here. */
        } catch (Exception ex) {
            /* Use this block for Exception Handling. */
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

}