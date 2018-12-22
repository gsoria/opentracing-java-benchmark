package org.sample.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.sample.billing.model.*;
import org.sample.billing.service.InvoiceService;
import org.sample.stringconcatenation.service.StringConcatenationService;
import org.sample.stringconcatenation.service.impl.StringConcatenationMockTracerServiceImpl;
import org.sample.stringconcatenation.service.impl.StringConcatenationNoopTracerServiceImpl;
import org.sample.stringconcatenation.service.impl.StringConcatenationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication(scanBasePackages = "org.sample.billing")
public class BenchmarkBilling {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        InvoiceService invoiceService;
        Invoice invoice;

        //Tell JMH that this method should be called to setup the state object before it
        //is passed to the benchmark method.
        @Setup(Level.Trial)
        public void doSetup() {
            ApplicationContext c = SpringApplication.run(BenchmarkBilling.class);
            invoiceService = c.getBean(InvoiceService.class);
        }

        @Setup(Level.Invocation)
        public void LoadInvoice(){
            Customer customer = new Customer("John" , "Doe",
                    "jdoe@gmail.com", "+16044444444");
            Invoice i = new Invoice();

            i.setAmountDue(new Double(300));
            i.setAccountNumber(604123098);
            i.setCustomer(customer);
            i.setCurrency(Currency.CAD);
            invoice = i;

        }

        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Iteration)
        public void doTearDown() {

        }
    }


    @Benchmark
    public void billing(StateVariables state) {
        state.invoiceService.createInvoice(state.invoice);
    }
}


