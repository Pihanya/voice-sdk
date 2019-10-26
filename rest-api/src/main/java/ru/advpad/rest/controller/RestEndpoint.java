package ru.advpad.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.advpad.rest.Storage;
import ru.advpad.model.Command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/commands")
public class RestEndpoint {

    @Autowired
    private Storage storage;

    @PostMapping("/mult")
    public void addCommands(@RequestBody List<Command> commands) {
        for (Command command : commands) storage.addCommand(command);
    }

    @PostMapping
    public void addCommand(@RequestBody Command command) {
        storage.addCommand(command);
    }

    @GetMapping
    public ResponseEntity<String> getCommand(@RequestParam("alias") String alias) {
        return ResponseEntity.of(storage.getCommandByAlias(alias));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Command>> getAllCommands() {
        return ResponseEntity.of(Optional.of(storage.getCommandMapping()));
    }
}
