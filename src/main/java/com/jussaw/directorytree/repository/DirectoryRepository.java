package com.jussaw.directorytree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jussaw.directorytree.model.Directory;
import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {
    Directory findByNameAndParentIsNull(String name);

    Directory findByNameAndParent(String name, Directory parent);

    List<Directory> findByParentIsNull();
}
