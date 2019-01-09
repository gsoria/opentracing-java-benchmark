package org.sample.billing.persistence;

import org.sample.billing.model.Currency;
import org.sample.billing.model.Customer;
import org.sample.billing.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InvoiceRepository {

    private static final Random random =  new Random();

    public void persistInvoice(Invoice invoice) {
    }

    public Invoice getInvoice(Long invoiceNumber) {
        Customer customer = new Customer("John" , "Doe",
                "jdoe@gmail.com", "+16044444444",
                "800123123");
        Invoice i = new Invoice();

        i.setAccountNumber(random.nextInt());
        i.setCustomer(customer);
        i.setCurrency(Currency.CAD);
        return i;
    }

    public void reset(){
    }
}
