package org.sample.billing.service.impl;

import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    @Qualifier("noopTracer")
    public Tracer tracer;

    @Override
    public Boolean notifyCustomer(Invoice invoice) {

        try (io.opentracing.Scope scope = tracer.buildSpan("createInvoice").startActive(true)) {
            String email = invoice.getCustomer().getEmail();

            //sending email
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scope.span().setTag("sending email", email);
            return Boolean.TRUE;
        }
    }
}
