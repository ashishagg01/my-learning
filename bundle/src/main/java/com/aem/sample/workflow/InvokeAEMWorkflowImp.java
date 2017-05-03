package com.aem.sample.workflow;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
//Sling Imports
import com.adobe.granite.workflow.model.WorkflowModel;

// This is a component so it can provide or consume services
@Component
@Service
public class InvokeAEMWorkflowImp implements InvokeAEMWorkflow {

    @Reference
    private ResourceResolverFactory resolverFactory;

    public String StartWorkflow(String workflowName, String workflowContent) {

        try
        {
            // Invoke the adaptTo method to create a Session
            ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);

            // Create a workflow session
            WorkflowSession wfSession = resourceResolver.adaptTo(WorkflowSession.class);

            // Get the workflow model
            WorkflowModel wfModel = wfSession.getModel(workflowName);

            // Get the workflow data
            // The first param in the newWorkflowData method is the payloadType. Just a fancy name to let it know what type of workflow it is working
            // with.
            WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", workflowContent);

            // Run the Workflow.
            wfSession.startWorkflow(wfModel, wfData);

            return workflowName + " has been successfully invoked on this content: " + workflowContent;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
