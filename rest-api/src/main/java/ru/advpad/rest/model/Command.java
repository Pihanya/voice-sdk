package ru.advpad.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private String name;
    private Collection<String> aliases;

}
