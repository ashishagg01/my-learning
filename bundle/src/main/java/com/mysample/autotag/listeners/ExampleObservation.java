package com.mysample.autotag.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Sling Imports

@Component
public class ExampleObservation implements EventListener {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private Session adminSession;
    @Reference
    SlingRepository repository;

    @Activate
    public void activate(ComponentContext context) throws Exception {
        log.info("activating ExampleObservation");
        try {
            adminSession = repository.loginAdministrative(null);
            adminSession.getWorkspace().getObservationManager().addEventListener(
                    this, // handler
                    Event.PROPERTY_ADDED | Event.NODE_ADDED, // binary combination of event types
                    "/apps/my-learning", // path
                    true, // is Deep?
                    null, // uuids filter
                    null, // nodetypes filter
                    false);
        } catch (RepositoryException e) {
            log.error("unable to register session", e);
            throw new Exception(e);
        }
    }

    @Deactivate
    public void deactivate() {
        if (adminSession != null) {
            adminSession.logout();
        }
    }

    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()) {
                log.info("something has been added : {}", eventIterator.nextEvent().getPath());
            }
        } catch (RepositoryException e) {
            log.error("Error while treating events", e);
        }
    }
}
