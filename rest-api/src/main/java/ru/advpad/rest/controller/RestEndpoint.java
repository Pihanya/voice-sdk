package ru.advpad.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.advpad.rest.Storage;
import ru.advpad.rest.model.Command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commands")
public class RestEndpoint {

    @Autowired
    private Storage storage;

    @PostMapping
    public void addCommands(@RequestBody List<Command> commands) {
        for (Command command : commands) storage.addCommand(command);
    }

    @GetMapping(path = "/{alias}")
    public ResponseEntity<String> getCommand(@RequestParam("alias") String alias) {
        return ResponseEntity.of(storage.getCommandByAlias(alias));
    }

    @GetMapping
    public ResponseEntity<Collection<Command>> getAllCommands() {
        return ResponseEntity.of(Optional.of(storage.getCommandMapping()));
    }
}
