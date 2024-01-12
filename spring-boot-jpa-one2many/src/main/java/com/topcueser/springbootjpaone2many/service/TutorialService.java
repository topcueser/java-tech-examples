package com.topcueser.springbootjpaone2many.service;

import com.topcueser.springbootjpaone2many.entity.Tutorial;
import com.topcueser.springbootjpaone2many.repository.TutorialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorialService {

    private final TutorialRepository tutorialRepository;

    public TutorialService(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    public List<Tutorial> findAll() {
        return tutorialRepository.findAll();
    }

    public List<Tutorial> findByPublished(boolean published) {
        return tutorialRepository.findByPublished(published);
    }

    public List<Tutorial> findByTitleContaining(String title) {
        return tutorialRepository.findByTitleContaining(title);
    }

    public Tutorial save(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }
}
