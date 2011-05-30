package com.zaubersoftware.gnip4j.mule.example;
import org.mule.construct.SimpleFlowConstruct;
import org.mule.tck.FunctionalTestCase;

/** test */
public final class GnipNamespaceHandlerTestCase extends FunctionalTestCase {
    
    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    /** test */
    public void testExistsFlow() throws Exception {
        SimpleFlowConstruct flow = lookupFlowConstruct("incomingGnip");
        
        System.in.read();
    }
    
    /** test */
    private SimpleFlowConstruct lookupFlowConstruct(String name) {
        return (SimpleFlowConstruct) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}