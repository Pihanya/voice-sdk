package ru.advpad.rest;

import org.springframework.stereotype.Service;
import ru.advpad.model.Command;

import java.util.*;

@Service
public class Storage {

    private Map<String, Command> commandMapping = new HashMap<>();

    public void addCommand(Command command) {
        if (!commandMapping.containsKey(command.getName())) {
            commandMapping.put(command.getName(), command);
            return;
        }
        Command existingCommand = commandMapping.get(command.getName());
        for (String alias : command.getAliases()) {
            if (existingCommand.getAliases().contains(alias)) continue;
            existingCommand.getAliases().add(alias);
        }
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
