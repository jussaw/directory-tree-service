package com.jussaw.directorytree.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jussaw.directorytree.exception.DirectoryAlreadyExistsException;
import com.jussaw.directorytree.model.Directory;
import com.jussaw.directorytree.repository.DirectoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirectoryTreeService {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryTreeService.class);

    @Autowired
    private DirectoryRepository directoryRepository;

    /**
     * Creates a new directory.
     *
     * @param directory the directory to create
     * @return the created directory
     */
    public Directory createDirectory(Directory directory) {
        try {
            logger.info("Creating directory: {}", directory.getName());
            return directoryRepository.save(directory);
        } catch (Exception e) {
            logger.error("Error creating directory: {}", directory.getName(), e);
            throw e;
        }
    }

    /**
     * Creates a new directory from a given path.
     *
     * @param path the path of the directory to create
     * @return the created directory
     * @throws DirectoryAlreadyExistsException if the directory already exists
     */
    public Directory createDirectoryFromPath(String path) {
        try {
            logger.info("Creating directory from path: {}", path);
            String[] parts = path.split("/");
            Directory parent = null;
            Directory current = null;

            for (String part : parts) {
                if (parent == null) {
                    current = directoryRepository.findByNameAndParentIsNull(part);
                } else {
                    current = directoryRepository.findByNameAndParent(part, parent);
                }

                if (current == null) {
                    current = new Directory(part);
                    current.setParent(parent);
                    directoryRepository.save(current);
                } else if (part.equals(parts[parts.length - 1])) {
                    throw new DirectoryAlreadyExistsException(
                            "Directory already exists with path: " + path);
                }

                parent = current;
            }

            logger.info("Created directory: {}", current.getName());
            return current;
        } catch (Exception e) {
            logger.error("Error creating directory from path: {}", path, e);
            throw e;
        }
    }

    /**
     * Moves a directory from source path to target path.
     *
     * @param sourcePath the source path of the directory
     * @param targetPath the target path of the directory
     * @return the moved directory, if present
     */
    public Optional<Directory> moveDirectory(String sourcePath, String targetPath) {
        try {
            logger.info("Moving directory from {} to {}", sourcePath, targetPath);
            Optional<Directory> sourceDirectory = findDirectoryByPath(sourcePath);
            Optional<Directory> targetDirectory =
                    targetPath.isEmpty() ? Optional.empty() : findDirectoryByPath(targetPath);

            if (sourceDirectory.isPresent()) {
                Directory dir = sourceDirectory.get();
                dir.setParent(targetDirectory.orElse(null));
                logger.info("Moved directory {} to {}", sourcePath,
                        targetPath.isEmpty() ? "root" : targetPath);
                return Optional.of(directoryRepository.save(dir));
            }
            logger.warn("Cannot move {} - source or target does not exist", sourcePath);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error moving directory from {} to {}", sourcePath, targetPath, e);
            throw e;
        }
    }

    /**
     * Deletes a directory with the given path.
     *
     * @param path the path of the directory to delete
     * @return true if the directory was deleted, false otherwise
     */
    public boolean deleteDirectory(String path) {
        try {
            logger.info("Deleting directory with path: {}", path);
            Optional<Directory> directory = findDirectoryByPath(path);
            if (directory.isPresent()) {
                directoryRepository.delete(directory.get());
                logger.info("Deleted directory with path: {}", path);
                return true;
            }
            logger.warn("Cannot delete {} - directory does not exist", path);
            return false;
        } catch (Exception e) {
            logger.error("Error deleting directory with path: {}", path, e);
            throw e;
        }
    }

    /**
     * Lists all directories.
     *
     * @return the list of all directories
     */
    public List<Directory> listDirectories() {
        try {
            logger.info("Listing all directories");
            return directoryRepository.findAll();
        } catch (Exception e) {
            logger.error("Error listing directories", e);
            throw e;
        }
    }

    /**
     * Formats the directory structure as a string.
     *
     * @return the formatted directory structure
     */
    public String formatDirectoryStructure() {
        try {
            logger.info("Formatting directory structure");
            List<Directory> rootDirectories = directoryRepository.findByParentIsNull();
            List<Directory> sortedRootDirectories = rootDirectories.stream()
                    .sorted((d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName()))
                    .collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            for (Directory dir : sortedRootDirectories) {
                formatDirectory(dir, sb, 0);
            }
            String formattedStructure = sb.toString();
            logger.info("Formatted directory structure:\n{}", formattedStructure);
            return formattedStructure;
        } catch (Exception e) {
            logger.error("Error formatting directory structure", e);
            throw e;
        }
    }

    /**
     * Recursively formats a directory and its subdirectories.
     *
     * @param directory the directory to format
     * @param sb the string builder to append the formatted structure
     * @param level the current level of indentation
     */
    private void formatDirectory(Directory directory, StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(directory.getName()).append("\n");

        List<Directory> sortedSubdirectories = directory.getSubdirectories().stream()
                .sorted((d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName()))
                .collect(Collectors.toList());

        for (Directory subdirectory : sortedSubdirectories) {
            formatDirectory(subdirectory, sb, level + 1);
        }
    }

    /**
     * Finds a directory by its path.
     *
     * @param path the path of the directory
     * @return the directory, if found
     */
    private Optional<Directory> findDirectoryByPath(String path) {
        try {
            logger.info("Finding directory by path: {}", path);
            String[] parts = path.split("/");
            Directory current = null;
            for (String part : parts) {
                if (current == null) {
                    current = directoryRepository.findByNameAndParentIsNull(part);
                } else {
                    current = directoryRepository.findByNameAndParent(part, current);
                }
                if (current == null) {
                    logger.warn("Directory not found for path: {}", path);
                    return Optional.empty();
                }
            }
            logger.info("Found directory: {}", current.getName());
            return Optional.ofNullable(current);
        } catch (Exception e) {
            logger.error("Error finding directory by path: {}", path, e);
            throw e;
        }
    }
}
