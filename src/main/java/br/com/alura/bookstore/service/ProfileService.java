package br.com.alura.bookstore.service;

import br.com.alura.bookstore.model.Profile;
import br.com.alura.bookstore.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Informed profile (ID: " + id + ") not found!"));
    }
}
