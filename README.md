This is a demo that explains how to extend the functionality of an agent locally.

### DalClientModuleInstrumentation.java

The entry class for the plugin, which implements the `InstrumentationModule` interface and provides SPI 
to load the class during arex-agent-java startup

### DalClientInstrumentation.java

Implement specific bytecode modification logic, telling arex-agent-java which class, method to modify, and when to execute

### DalClientAdvice.java

Implement actual recording and replay logic, or your own logic;

Please refer to the detailed: [arex-plugin-development-tutorial]()