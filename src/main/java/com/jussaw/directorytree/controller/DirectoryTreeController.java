package com.jussaw.directorytree.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jussaw.directorytree.exception.DirectoryAlreadyExistsException;
import com.jussaw.directorytree.model.Directory;
import com.jussaw.directorytree.service.DirectoryTreeService;
import java.util.Optional;

@RestController
@RequestMapping("/directories")
public class DirectoryTreeController {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryTreeController.class);

    @Autowired
    private DirectoryTreeService directoryService;

    @PostMapping("/create")
    public ResponseEntity<String> createDirectory(@RequestParam String path) {
        logger.info("Received request to create directory with path: {}", path);
        try {
            Directory directory = directoryService.createDirectoryFromPath(path);
            logger.info("Created directory with path: {}", path);
            return ResponseEntity.ok("CREATE " + path);
        } catch (DirectoryAlreadyExistsException e) {
            logger.warn("Directory already exists with path: {}", path);
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error creating directory with path: {}", path, e);
            return ResponseEntity.status(500).body("Error creating directory with path: " + path);
        }
    }

    @PostMapping("/move")
    public ResponseEntity<String> moveDirectory(@RequestParam String sourcePath,
            @RequestParam String targetPath) {
        logger.info("Received request to move directory from {} to {}", sourcePath, targetPath);
        try {
            Optional<Directory> moved = directoryService.moveDirectory(sourcePath, targetPath);
            if (moved.isPresent()) {
                logger.info("Moved directory from {} to {}", sourcePath, targetPath);
                return ResponseEntity.ok("MOVE " + sourcePath + " " + targetPath);
            } else {
                logger.warn("Cannot move {} - source or target does not exist", sourcePath);
                return ResponseEntity.badRequest()
                        .body("Cannot move " + sourcePath + " - source or target does not exist");
            }
        } catch (Exception e) {
            logger.error("Error moving directory from {} to {}", sourcePath, targetPath, e);
            return ResponseEntity.status(500)
                    .body("Error moving directory from " + sourcePath + " to " + targetPath);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDirectory(@RequestParam String path) {
        logger.info("Received request to delete directory with path: {}", path);
        try {
            boolean deleted = directoryService.deleteDirectory(path);
            if (deleted) {
                logger.info("Deleted directory with path: {}", path);
                return ResponseEntity.ok("DELETE " + path);
            } else {
                logger.warn("Cannot delete {} - directory does not exist", path);
                return ResponseEntity.badRequest()
                        .body("Cannot delete " + path + " - directory does not exist");
            }
        } catch (Exception e) {
            logger.error("Error deleting directory with path: {}", path, e);
            return ResponseEntity.status(500).body("Error deleting directory with path: " + path);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<String> listDirectories() {
        logger.info("Received request to list directories");
        try {
            String formattedStructure = directoryService.formatDirectoryStructure();
            logger.info("Listed directories:\n{}", formattedStructure);
            return ResponseEntity.ok(formattedStructure);
        } catch (Exception e) {
            logger.error("Error listing directories", e);
            return ResponseEntity.status(500).body("Error listing directories");
        }
    }
}
