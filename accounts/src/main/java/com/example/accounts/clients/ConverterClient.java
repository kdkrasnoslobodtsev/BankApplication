package com.example.accounts.clients;

import converter.Convert;
import converter.ConverterGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ConverterClient {
    private final ConverterGrpc.ConverterBlockingStub converterBlockingStub;

    public ConverterClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("converter", 9090)
                .usePlaintext()
                .build();
        converterBlockingStub = ConverterGrpc.newBlockingStub(channel);
    }

    public BigDecimal convert(String from, String to, BigDecimal amount) {
        Convert.ConvertRequest request = Convert.ConvertRequest.newBuilder()
                .setFrom(from)
                .setTo(to)
                .setAmount(amount.doubleValue())
                .build();
        Convert.ConvertResponse response = converterBlockingStub.convert(request);
        return BigDecimal.valueOf(response.getAmount());
    }
}
