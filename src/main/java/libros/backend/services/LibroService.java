package libros.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import libros.backend.repositories.LibroRepository;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
}
