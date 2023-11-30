package com.example.backend.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    @Deprecated
    @Query("SELECT s FROM Book s WHERE s.title=?1")
    Optional<Book> findBookByTitle(String title);

    // Find book that contains a specific string
    List<Book> findBookByTitleContainingOrAuthorContaining(String title, String author);
    // Delete books of a user
    void deleteBooksByOwner(String owner);
}
