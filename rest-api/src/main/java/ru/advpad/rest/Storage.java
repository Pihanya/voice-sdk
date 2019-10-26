package ru.advpad.rest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;
import ru.advpad.model.Command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class Storage {

    private static Map<String, Command> commandMapping;
    private final static String STORAGE_PATH = "storage.json";
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());

    static {
        try {
            String contents = new String(Files.readAllBytes(Paths.get(STORAGE_PATH)), StandardCharsets.UTF_8);
            commandMapping = objectMapper.readValue(contents, new TypeReference<Map<String, Command>>() {
            });
        } catch (IOException e) {
            commandMapping = new HashMap<>();
        }
    }

    public void addCommand(Command command) {
        if (!commandMapping.containsKey(command.getName())) {
            commandMapping.put(command.getName(), command);
            syncFileStorage();
            return;
        }
        Command existingCommand = commandMapping.get(command.getName());
        for (String alias : command.getAliases()) {
            if (existingCommand.getAliases().contains(alias)) continue;
            existingCommand.getAliases().add(alias);
        }
        syncFileStorage();
    }

    public Optional<String> getCommandByAlias(String alias) {
        for (Command command : commandMapping.values()) {
            if (command.getAliases().contains(alias)) return Optional.of(command.getName());
        }
        return Optional.empty();
    }

    public Collection<Command> getCommandMapping() {
        Collection<Command> commands = commandMapping.values();
        int id = 0;
        for (Command command : commands) {
            command.setId(id);
            id++;
        }
        return commands;
    }

    private void syncFileStorage() {
        try {
            objectWriter.writeValue(new File(STORAGE_PATH), commandMapping);
            objectWriter.writeValue(System.out, commandMapping);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
