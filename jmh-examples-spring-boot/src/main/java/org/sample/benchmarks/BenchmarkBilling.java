package org.sample.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.sample.billing.model.Currency;
import org.sample.billing.model.Customer;
import org.sample.billing.model.Invoice;
import org.sample.billing.model.LineItem;
import org.sample.billing.persistence.InvoiceRepository;
import org.sample.billing.service.InvoiceService;
import org.sample.billing.service.impl.InvoiceServiceImpl;
import org.sample.billing.service.traced.TracedInvoiceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import java.math.BigDecimal;

@SpringBootApplication(scanBasePackages = "org.sample.billing.*")
public class BenchmarkBilling {

    @State(Scope.Thread)
    public static class StateVariables {
        //Spring bean used in doTearDown method
        InvoiceRepository repository;

        //Spring bean used in benchmark method
        InvoiceService invoiceService;
        InvoiceService invoiceTracedService;

        //Data objects
        Invoice invoice;
        LineItem item;

        private int accountNumber = 0;

        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "jaegerTracer");

            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceService = c.getBean("invoiceServiceImpl", InvoiceServiceImpl.class);
            repository = c.getBean(InvoiceRepository.class);
        }

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
        }
    }

    public static class StateVariablesJaeger extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "jaegerTracer");

            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesNoopTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "noopTracer");

            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesMockTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "mockTracer");

            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public static class StateVariablesHaystack extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "haystackTracer");

            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceTracedService =  c.getBean(TracedInvoiceService.class);
            repository = c.getBean(InvoiceRepository.class);
        }
    }

    public Invoice doBenchmarkBillingNotInstrumented(StateVariables state) {
        //Create invoice
        return doIssueInvoice(state, state.invoiceService);
    }

    public Invoice doBenchmarkBillingNoopTracer(StateVariablesNoopTracer state) {
        //Create invoice
        return doIssueInvoice(state, state.invoiceTracedService);
    }

    public Invoice doBenchmarkBillingJaegerTracer(StateVariablesJaeger state) {
        //Create invoice
        return doIssueInvoice(state, state.invoiceTracedService);
    }

    public Invoice doBenchmarkBillingHaystackTracer(StateVariablesHaystack state) {
        return doIssueInvoice(state, state.invoiceTracedService);
    }

    private Invoice doIssueInvoice(StateVariables state, InvoiceService invoiceService) {
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


