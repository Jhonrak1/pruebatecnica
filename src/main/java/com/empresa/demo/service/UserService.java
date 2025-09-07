package com.empresa.demo.service;

import com.empresa.demo.domain.model.User;
import org.springframework.stereotype.Service;

@Service
/*Added missing @Service annotation*/
public class UserService {

    public User findById(Long id) {
        // TODO: simular recuperación (p.ej. base de datos)
        return new User(id, "NombreEjemplo", "ejemplo@empresa.com");
    }
}
