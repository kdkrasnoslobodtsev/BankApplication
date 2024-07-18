package com.example.controllers;

import com.example.dto.AccountResponse;
import com.example.services.ConvertService;
import com.example.errors.ErrorResponse;
import converter.Convert;
import converter.ConverterGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.net.ConnectException;
import java.util.Map;

@GrpcService
@AllArgsConstructor
public class ConvertController extends ConverterGrpc.ConverterImplBase {
    private final ConvertService convertService;

    @Override
    public void convert(Convert.ConvertRequest request, StreamObserver<Convert.ConvertResponse> responseObserver) {
        System.out.println("Got convert request");
        BigDecimal converted = new BigDecimal(Double.toString(convertService.convert(request.getFrom(),
                request.getTo(), request.getAmount())));
        converted = converted.setScale(2, RoundingMode.HALF_EVEN);
        Convert.ConvertResponse response = Convert.ConvertResponse.newBuilder()
                .setAmount(converted.doubleValue()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
