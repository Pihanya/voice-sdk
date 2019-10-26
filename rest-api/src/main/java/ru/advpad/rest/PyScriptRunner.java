package ru.advpad.rest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Service;
import ru.advpad.rest.grpc.RecognitionServiceGrpc;
import ru.advpad.rest.grpc.RecognizeRequest;
import ru.advpad.rest.grpc.RecognizeResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class PyScriptRunner {

    private static final String PYTHON = "python ";
    private static final String SCRIPT_PATH = "script.py ";
    private static final String STORAGE_PATH = "storage.json";
    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

    private RecognitionServiceGrpc.RecognitionServiceBlockingStub stub
            = RecognitionServiceGrpc.newBlockingStub(channel);

    public String predictPossible(String command) {

        RecognizeResponse helloResponse = stub.recognize(RecognizeRequest.newBuilder()
                .setAlias(command)
                .build());
        if (helloResponse.getCommand().equals("null")) return null;
        else return helloResponse.getCommand();
    }
}
