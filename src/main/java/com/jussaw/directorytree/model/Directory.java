package com.jussaw.directorytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a directory.
 */
@Entity
@Data
@NoArgsConstructor
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Directory> subdirectories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Directory parent;

    /**
     * Constructs a new directory with the given name.
     *
     * @param name the name of the directory
     */
    public Directory(String name) {
        this.name = name;
    }

    /**
     * Adds a subdirectory to this directory.
     *
     * @param subdirectory the subdirectory to add
     */
    public void addSubdirectory(Directory subdirectory) {
        subdirectories.add(subdirectory);
        subdirectory.setParent(this);
    }

    /**
     * Removes a subdirectory from this directory.
     *
     * @param subdirectory the subdirectory to remove
     */
    public void removeSubdirectory(Directory subdirectory) {
        subdirectories.remove(subdirectory);
        subdirectory.setParent(null);
    }
}
