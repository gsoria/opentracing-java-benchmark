package org.sample.billing.service.notinstrumented.impl;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.sample.billing.model.Invoice;
import org.sample.billing.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public Boolean notifyCustomer(Invoice invoice) {

        String recipientAddress = invoice.getCustomer().getEmail();

        //mock sending email

        return Boolean.TRUE;
    }
}
