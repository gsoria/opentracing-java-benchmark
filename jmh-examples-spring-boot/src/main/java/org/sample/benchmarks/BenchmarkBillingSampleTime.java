package org.sample.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.sample.billing.model.Currency;
import org.sample.billing.model.Customer;
import org.sample.billing.model.Invoice;
import org.sample.billing.model.LineItem;
import org.sample.billing.persistence.InvoiceRepository;
import org.sample.billing.service.InvoiceService;
import org.sample.billing.service.jaegertracer.impl.InvoiceJaegerTracerServiceImpl;
import org.sample.billing.service.nooptracer.impl.InvoiceNoopTracerServiceImpl;
import org.sample.billing.service.notinstrumented.impl.InvoiceServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Random;

@SpringBootApplication(scanBasePackages = "org.sample.billing.*")
public class BenchmarkBillingSampleTime {

    /*
    Sometimes you way want to initialize some variables that your benchmark code
    needs, but which you do not want to be part of the code your benchmark
    measures. Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        //Spring bean used in doTearDown method
        InvoiceRepository repository;

        //Spring bean used in benchmark method
        InvoiceService invoiceService;
        InvoiceService invoiceServiceNoopTracer;
        InvoiceService invoiceServiceJaegerTracer;

        //Data objects
        Invoice invoice;
        LineItem item;

        private static final Random random =  new Random();

        //Tell JMH that this method should be called to setup the state object
        //before it is passed to the benchmark method.
        @Setup(Level.Iteration)
        public void doSetup() {
            ApplicationContext c = SpringApplication.run(
                    BenchmarkBillingSampleTime.class);
            invoiceService = c.getBean(InvoiceServiceImpl.class);
            invoiceServiceNoopTracer = c.getBean(InvoiceNoopTracerServiceImpl.class);
            invoiceServiceJaegerTracer = c.getBean(InvoiceJaegerTracerServiceImpl.class);

            repository = c.getBean(InvoiceRepository.class);
        }

        @Setup(Level.Invocation)
        public void loadInvoice(){
            Customer customer = new Customer("John" , "Doe",
                    "jdoe@gmail.com", "+16044444444",
                    "800123123");
            Invoice i = new Invoice();

            i.setAccountNumber(random.nextInt());
            i.setCustomer(customer);
            i.setCurrency(Currency.CAD);
            invoice = i;

            LineItem it = new LineItem();

            it.setItemDescription("Item example");
            it.setRate(new BigDecimal(10.5));
            it.setQuantity(3);
            item = it;

        }

        //Tells JMH that this method should be called to clean up ("tear down")
        //the state object after the benchmark has been execute
        @TearDown(Level.Iteration)
        public void doTearDown() {
            repository.reset();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingNotInstrumented(StateVariables state) {
        //Create invoice
        Long invoiceNumber = state.invoiceService.createInvoice(state.invoice);

        //Add item / items
        LineItem item = state.item;
        state.invoiceService.addLineItem(invoiceNumber, item);

        // Issue invoice
        Invoice invoice = state.invoiceService.getInvoice(invoiceNumber);
        return state.invoiceService.issueInvoice(invoiceNumber);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingNoopTracer(StateVariables state) {
        //Create invoice
        Long invoiceNumber = state.invoiceServiceNoopTracer
                .createInvoice(state.invoice);

        //Add item / items
        LineItem item = state.item;
        state.invoiceServiceNoopTracer.addLineItem(invoiceNumber, item);

        // Issue invoice
        Invoice invoice = state.invoiceServiceNoopTracer.getInvoice(invoiceNumber);
        return state.invoiceServiceNoopTracer.issueInvoice(invoiceNumber);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingJaegerTracer(StateVariables state) {
        //Create invoice
        Long invoiceNumber = state.invoiceServiceJaegerTracer.createInvoice(state.invoice);

        //Add item / items
        LineItem item = state.item;
        state.invoiceServiceJaegerTracer.addLineItem(invoiceNumber, item);

        // Issue invoice
        Invoice invoice = state.invoiceServiceJaegerTracer.getInvoice(invoiceNumber);
        return state.invoiceServiceJaegerTracer.issueInvoice(invoiceNumber);
    }
}


