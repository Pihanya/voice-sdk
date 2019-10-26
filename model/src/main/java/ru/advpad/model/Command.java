package ru.advpad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Command implements Serializable {

    private String name;
    private Collection<String> aliases;

}
