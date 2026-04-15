package za.co.entelect.java_devcamp.configs;

import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import za.co.entelect.java_devcamp.soap.CreditClient;
import za.co.entelect.java_devcamp.soap.FraudClient;
import za.co.entelect.java_devcamp.soap.SoapMessageSender;

@Configuration
public class SoapConfig {
    @Bean
    public Jaxb2Marshaller creditMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package configured in the build.gradle/pom.xml
        marshaller.setContextPath("za.co.entelect.java_devcamp.creditcheck");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller fraudMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package configured in the build.gradle/pom.xml
        marshaller.setContextPath("za.co.entelect.java_devcamp.fraudcheck");
        return marshaller;
    }

    @Bean
    public CreditClient creditClient(WebServiceTemplateBuilder builder, Jaxb2Marshaller creditMarshaller, SoapMessageSender sender) {
        CreditClient client = new CreditClient();
        builder = builder.setMarshaller(creditMarshaller).setUnmarshaller(creditMarshaller);
        client.setWebServiceTemplate(builder.build());
        client.getWebServiceTemplate().setMessageSender(sender);
        return client;
    }

    @Bean
    public FraudClient fraudClient(WebServiceTemplateBuilder builder, Jaxb2Marshaller fraudMarshaller, SoapMessageSender sender) {
        FraudClient client = new FraudClient();
        builder = builder.setMarshaller(fraudMarshaller).setUnmarshaller(fraudMarshaller);
        client.setWebServiceTemplate(builder.build());
        client.getWebServiceTemplate().setMessageSender(sender);
        return client;
    }



}
