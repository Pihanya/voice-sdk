package ru.advpad.rest;

import org.springframework.stereotype.Service;
import ru.advpad.rest.model.Command;

import java.util.*;

@Service
public class Storage {

    private Map<String, Command> commandMapping = new HashMap<>();

    public void addCommandAlias(String command, String alias) {
        if (commandMapping.containsKey(command)) {
            commandMapping.get(command).getAliases().add(alias);
        } else {
            Command commandToAdd = new Command();
            commandToAdd.setAliases(Arrays.asList(alias));
            commandToAdd.setName(command);
            commandMapping.put(command, commandToAdd);
        }
    }

    public void addCommand(Command command) {
        commandMapping.put(command.getName(), command);
    }

    public Optional<String> getCommandByAlias(String alias) {
        for (Command command : commandMapping.values()) {
            if (command.getAliases().contains(alias)) return Optional.of(command.getName());
        }
        return Optional.empty();
    }

    public Collection<Command> getCommandMapping() {
        return commandMapping.values();
    }
}
