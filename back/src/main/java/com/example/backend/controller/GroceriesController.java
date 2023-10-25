package com.example.backend.controller;

import com.example.backend.dto.GroceryDTO;
import com.example.backend.entities.Grocery;
import com.example.backend.mapper.GroceryMapper;
import com.example.backend.service.GroceryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("groceries")
@AllArgsConstructor
public class GroceriesController {

    private GroceryService groceryService;
    private GroceryMapper groceryMapper;

    @PostMapping("add")
    public ResponseEntity<Long> add(@RequestBody GroceryDTO dto){
        return ResponseEntity.ok(groceryService.add(groceryMapper.mapToEntity(dto)).getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryDTO> getById(@PathVariable("id") long id){
        Optional<Grocery> groceryOpt = groceryService.findById(id);
        if(groceryOpt.isPresent()){
            return ResponseEntity.ok(groceryMapper.mapToDTO(groceryOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("update")
    public HttpStatus update(@RequestBody GroceryDTO dto){
        groceryService.update(groceryMapper.mapToEntity(dto));
        return HttpStatus.OK;
    }
    @PostMapping("remove")
    public HttpStatus remove(@RequestBody GroceryDTO dto){
        groceryService.remove(groceryMapper.mapToEntity(dto));
        return HttpStatus.OK;
    }

    @GetMapping("list")
    public ResponseEntity<List<GroceryDTO>> listGroceries(){
        return ResponseEntity.ok(groceryMapper.mapToDTO(groceryService.list()));
    }

}
