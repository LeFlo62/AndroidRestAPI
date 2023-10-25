package com.example.backend.service;

import com.example.backend.entities.Grocery;
import com.example.backend.repositories.GroceryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroceryService {

    private GroceryRepository groceryRepository;

    public Grocery add(Grocery grocery){
        return groceryRepository.save(grocery);
    }

    public Optional<Grocery> findById(Long id){
        return groceryRepository.findById(id);
    }

    public void update(Grocery grocery){
        groceryRepository.save(grocery);
    }

    public void remove(Grocery grocery){
        groceryRepository.delete(grocery);
    }

    public List<Grocery> list(){
        return groceryRepository.findAll();
    }

}
