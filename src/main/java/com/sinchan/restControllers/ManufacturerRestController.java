package com.sinchan.restControllers;

import com.sinchan.entities.Items;
import com.sinchan.entities.Manufacturers;
import com.sinchan.services.ManufacturersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ManufacturerRestController {

    private final ManufacturersService manufacturersService;

    public ManufacturerRestController(ManufacturersService manufacturersService){
        this.manufacturersService = manufacturersService;
    }

    @GetMapping("manufacturer/list")
    public List<Manufacturers> manufacturerList(){

        return manufacturersService.listManufacturers(100);
    }

    @PostMapping("manufacturer")
    public void saveManufacturer(@RequestBody Manufacturers manufacturers){

        manufacturersService.save(manufacturers, 100);

    }

    @GetMapping("manufacturer")
    public Manufacturers openManufacturer(@RequestParam int manufacturerId){

        System.out.println("id : "+manufacturerId);
        return manufacturersService.findById(manufacturerId, 100);

    }

    @PutMapping("manufacturer")
    public void updateManufacturer(@RequestBody Manufacturers manufactueres){

        manufacturersService.update(manufactueres, 100);
    }

    @DeleteMapping("manufacturer")
    public void deleteManufacturer(@RequestParam int manufacturerId){

        manufacturersService.delete(manufacturerId, 100);
    }
}
