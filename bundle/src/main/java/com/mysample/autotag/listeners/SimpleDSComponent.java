package com.mysample.autotag.listeners;
import java.util.Arrays;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
// Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// AEM Tagging Imports
import com.day.cq.tagging.JcrTagManagerFactory;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * Just a simple DS Component
 */
@Component(metatype = true)
@Service
public class SimpleDSComponent implements Runnable, EventListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private BundleContext bundleContext;

    // Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final String NAMESPACE = "/etc/tags/mysample";

    @Reference
    JcrTagManagerFactory tmf;

    private Session session;

    private ObservationManager observationManager;

    // Inject a Sling ResourceResolverFactory to create a Session requited by the EventHandler
    @Reference
    private SlingRepository repository;

    public void run() {
        log.info("Running...");
    }

    // Place app logic here to define the AEM Custom Event Handler
    protected void activate(ComponentContext ctx) {
        this.bundleContext = ctx.getBundleContext();

        try
        {

            // Invoke the adaptTo method to create a Session
            ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            session = resourceResolver.adaptTo(Session.class);

            // Setup the event handler to respond to a new claim under content/claim....

            observationManager = session.getWorkspace().getObservationManager();
            final String[] types = { "cq:Page", "nt:unstructured" };
            final String path = "/"; // define the path
            observationManager.addEventListener(this, Event.NODE_ADDED, path, true, null, null, false);
            log.info("Observing property changes to {} nodes under {}", Arrays.asList(types), path);

        } catch (Exception e)
        {
            log.error("unable to register session", e);

        }
    }

    protected void deactivate(ComponentContext componentContext) throws RepositoryException {

        if (observationManager != null) {
            observationManager.removeEventListener(this);
        }
        if (session != null) {
            session.logout();
            session = null;
        }
    }

    public void onEvent(EventIterator it) {

        log.info("IN ONEVENT!");

        try {
            while (it.hasNext()) {
                Event event = it.nextEvent();
                log.info("AutoTagListener - new add event: ", event.getPath());

                Node pageContentNode = session.getNode(event.getPath());

                if ((pageContentNode == null) || !pageContentNode.getPrimaryNodeType().isNodeType("cq:PageContent")) {
                    log.debug("Skip processing node: " + event.getPath());
                    return;
                }

                Node pageNode = pageContentNode.getParent();

                if ((pageNode == null) || !pageNode.getPrimaryNodeType().isNodeType("cq:Page")) {
                    log.debug("Skip processing node: " + pageNode);
                    return;
                }

                TagManager tMgr = tmf.getTagManager(session);
                Tag superTag = tMgr.resolve(NAMESPACE);
                Tag tag = null;

                if (superTag == null) {
                    tag = tMgr.createTag(NAMESPACE, "My Sample", "My Sample tags", true);
                    log.info("Tag Name Space created : ", tag.getPath());
                }

                tag = tMgr.createTag(NAMESPACE + "/" + pageNode.getName(), pageNode.getName(), "Auto tag : " + pageNode.getName(), true);

                String tagArray[] = new String[1];
                tagArray[0] = tag.getNamespace().getName() + ":" + tag.getPath().substring(tag.getPath().indexOf(NAMESPACE) + NAMESPACE.length() + 1);

                pageContentNode.setProperty("cq:tags", tagArray);
                session.save();
                log.info("End ONEVENT!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
