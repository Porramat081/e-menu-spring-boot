package com.porramat081.e_menu.service.payment;

import co.omise.Client;
import co.omise.models.Charge;
import co.omise.models.Source;
import co.omise.models.SourceType;
import co.omise.requests.Request;
import com.porramat081.e_menu.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    @Value("${omise.publicKey}")
    private String publicKey;

    @Value("${omise.secretKey}")
    private String secretKey;

    @Override
    public PaymentResponse createPayment(BigDecimal amount) throws Exception {
        Client client = new Client.Builder()
                .publicKey(publicKey)
                .secretKey(secretKey)
                .build();

        Long result = amount
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        Request<Source> request = new Source.CreateRequestBuilder()
                .amount(result)
                .currency("thb")
                .type(SourceType.PromptPay)
                .build();

        Source source = client.sendRequest(request);

        Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                .source(source.getId())
                .amount(source.getAmount())
                .currency(source.getCurrency())
                .build();

        Charge charge = client.sendRequest(chargeRequest);

        String qrUrl = charge.getSource()
                .getScannableCode()
                .getImage()
                .getDownloadUri();

        return new PaymentResponse(qrUrl);
    }
}
