package org.sample.billing.service.jaegertracer.impl;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationJaegerTracerServiceImpl implements NotificationService {

    @Autowired
    @Qualifier("jaegerTracer")
    public Tracer tracer;

    @Value("${thread.sleep}")
    private String msToSleep;

    @Override
    public Boolean notifyCustomer(Invoice invoice) {

        try (Scope scope = tracer
                .buildSpan("notifyCustomer")
                .startActive(true)) {
            String recipientAddress = invoice.getCustomer().getEmail();
            String taxId = scope.span().getBaggageItem("taxId");

            //mock sending email
            try {
                Thread.sleep(new Integer(msToSleep));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scope.span().setTag("address", recipientAddress);
            scope.span().setTag("customer taxId", taxId);

            return Boolean.TRUE;
        }
    }
}
