package libros.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import libros.backend.models.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro,Long>{
    
}