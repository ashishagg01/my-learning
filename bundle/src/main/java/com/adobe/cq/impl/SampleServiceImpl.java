package com.adobe.cq.impl;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.SampleService;

@Component(
        label = "Sample Service Impl",
        description = "Sample Description",
        metatype = true,
        immediate = false)
@Properties({
        @Property(
                label = "SampleVendor",
                name = "service.vendor",
                value = "SampleVendor",
                propertyPrivate = true
        )
})
@Service
public class SampleServiceImpl implements SampleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final boolean DEFAULT_ENABLED = false;
    private boolean enabled = DEFAULT_ENABLED;

    @Property(
            label = "Service Enable/Disable",
            description = "Enables/Disables the service without nullifying service reference objects. This enable/disabling must be implemented in all public methods of this service.",
            boolValue = DEFAULT_ENABLED)
    public static final String PROP_ENABLED = "prop.enabled";

    /* OSGi Service References */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /*** Service Methods ***/
    public String helloWorld() {
        if (!this.enabled) {
            return "Service has been disabled";
        }
        return "Hello World!";
    }

    public String getName(final String path) throws LoginException {
        ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        Resource resource = resourceResolver.resolve(path);
        if (resource == null) {
            return null;
        }
        return resource.getName();
    }

    /*** OSGi Component Methods ***/
    @Activate
    protected void activate(final ComponentContext componentContext) throws Exception {
        final Map<String, String> properties = (Map<String, String>) componentContext.getProperties();
        configure(properties);
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        this.enabled = false;
    }

    protected void configure(final Map<String, String> properties) {
        // Global Service Enabled/Disable Setting
        this.enabled = PropertiesUtil.toBoolean(properties.get(PROP_ENABLED), DEFAULT_ENABLED);
    }
}