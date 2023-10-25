package com.example.backend.mapper;

import com.example.backend.dto.GroceryDTO;
import com.example.backend.entities.Grocery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroceryMapper {

    public GroceryDTO mapToDTO(Grocery entity){
        GroceryDTO dto = new GroceryDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setQuantity(entity.getQuantity());

        return dto;
    }

    public Grocery mapToEntity(GroceryDTO dto){
        Grocery entity = new Grocery();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setQuantity(dto.getQuantity());

        return  entity;
    }

    public List<GroceryDTO> mapToDTO(List<Grocery> entities){
        return entities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<Grocery> mapToEntity(List<GroceryDTO> dtos){
        return dtos.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

}
