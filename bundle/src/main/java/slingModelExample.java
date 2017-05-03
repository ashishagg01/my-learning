import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.day.cq.commons.Externalizer;

// Traditional way to getting values from jcr nodes in Java Class.
@Component
@Service
@Properties({
@Property(name = "adaptables", value = {"Resource" }),
@Property(name = "adapters", value = {"YourCustom" })
})
 
// Retrieving vales from jcr node through inject in Sling Model  
@Model(adaptables = Resource.class)
public class slingModelExample {
@Inject // email property is always looked from Resource( after adapting to ValueMap), if this property value 
/*is not present then it returns null. If this property is in itself not available then it throws exception.*/
private String email;
@Inject @Optional // It is not mandatory to have firstname property in Resource.
private String firstName;
// Use @Named if jcr node property name is different than class variable. 
/*use @Default if you want to assign default values, If empty then assign default value as 'NA'*/
@Inject @Named("surname") @Default(values="NA")
private String lastName;

@Inject // OSGi Service
private Externalizer externalizer;
 
//@PostConstructor annotation defines that this method will run after injecting all field 
@PostConstruct
protected void init() {
// gets executed after the class is created and all variables are injected
}}