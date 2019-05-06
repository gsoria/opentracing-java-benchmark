package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.billing.BillingApplication;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import io.opentracing.contrib.benchmarks.billing.service.impl.InvoiceServiceImpl;
import org.openjdk.jmh.annotations.*;
import io.opentracing.contrib.benchmarks.billing.model.Currency;
import io.opentracing.contrib.benchmarks.billing.model.Customer;
import io.opentracing.contrib.benchmarks.billing.model.Invoice;
import io.opentracing.contrib.benchmarks.billing.model.LineItem;
import io.opentracing.contrib.benchmarks.billing.persistence.InvoiceRepository;
import io.opentracing.contrib.benchmarks.billing.service.InvoiceService;
import io.opentracing.contrib.benchmarks.billing.service.traced.TracedInvoiceService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import java.math.BigDecimal;

public class BenchmarkBillingBase {

    @State(Scope.Thread)
    public static class StateVariables {
        ConfigurableApplicationContext c;

        //Spring bean used in doTearDown method
        InvoiceRepository repository;

        //Spring bean used in benchmark method
        InvoiceService invoiceService;
        TracedInvoiceService invoiceTracedService;

        //Data objects
        Invoice invoice;
        LineItem item;

        private int accountNumber = 0;

        @Setup(Level.Invocation)
        public void loadInvoice(){
            Customer customer = new Customer("John" , "Doe",
                    "jdoe@gmail.com", "+16044444444",
                    "800123123");
            Invoice i = new Invoice();

            i.setAccountNumber(accountNumber++);
            i.setCustomer(customer);
            i.setCurrency(Currency.CAD);
            invoice = i;

            LineItem it = new LineItem();

            it.setItemDescription("Item example");
            it.setRate(new BigDecimal(10.5));
            it.setQuantity(3);
            item = it;

        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            repository.reset();
            c.close();
        }
    }

    public static class StateVariablesNoInstrumentation extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.NO_INSTRUMENTATION);

            c = SpringApplication.run(BillingApplication.class);
            invoiceService =  c.getBean("invoiceServiceImpl", InvoiceServiceImpl.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesJaeger extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.JAEGERTRACER);

            c = SpringApplication.run(BillingApplication.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesNoopTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.NOOPTRACER);

            c = SpringApplication.run(BillingApplication.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesMockTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.MOCKTRACER);

            c = SpringApplication.run(BillingApplication.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesHaystack extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TracerImplementation.HAYSTACKTRACER);

            c = SpringApplication.run(BillingApplication.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public Invoice doBenchmarkBillingNoInstrumentation(StateVariables state) {
        //Create invoice
        return issueInvoice(state, state.invoiceService);
    }

    public Invoice doBenchmarkBillingNoopTracer(StateVariablesNoopTracer state) {
        //Create invoice
        return issueInvoice(state, state.invoiceTracedService);
    }

    public Invoice doBenchmarkBillingJaegerTracer(StateVariablesJaeger state) {
        //Create invoice
        return issueInvoice(state, state.invoiceTracedService);
    }

    public Invoice doBenchmarkBillingHaystackTracer(StateVariablesHaystack state) {
        return issueInvoice(state, state.invoiceTracedService);
    }

    public Invoice doBenchmarkBillingMockTracer(StateVariablesMockTracer state) {
        return issueInvoice(state, state.invoiceTracedService);
    }

    private Invoice issueInvoice(StateVariables state, InvoiceService invoiceService) {
        //Create invoice
        Long invoiceNumber = invoiceService.createInvoice(state.invoice);

        //Add item / items
        LineItem item = state.item;
        invoiceService.addLineItem(invoiceNumber, item);

        // Issue invoice
        Invoice invoice = invoiceService.getInvoice(invoiceNumber);
        return invoiceService.issueInvoice(invoiceNumber);
    }
}


