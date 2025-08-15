package com.sinchan.restControllers;

import com.sinchan.entities.Items;
import com.sinchan.entities.Manufacturers;
import com.sinchan.entities.User;
import com.sinchan.services.ManufacturersService;
import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManufacturerRestController {

    private final ManufacturersService manufacturersService;

    public ManufacturerRestController(ManufacturersService manufacturersService){
        this.manufacturersService = manufacturersService;
    }

    @GetMapping("manufacturer/list")
    public List<Manufacturers> manufacturerList(){

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = authToken.getUser();

        return manufacturersService.listManufacturers(user.getUserId());
    }

    @PostMapping("manufacturer")
    public void saveManufacturer(@RequestBody Manufacturers manufacturers){

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = authToken.getUser();

        manufacturersService.save(manufacturers, user.getUserId());

    }

    @GetMapping("manufacturer")
    public Manufacturers openManufacturer(@RequestParam int manufacturerId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return manufacturersService.findById(manufacturerId, user.getUserId());

    }

    @PutMapping("manufacturer")
    public void updateManufacturer(@RequestBody Manufacturers manufactueres){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        manufacturersService.update(manufactueres, user.getUserId());
    }

    @DeleteMapping("manufacturer")
    public void deleteManufacturer(@RequestParam int manufacturerId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        manufacturersService.delete(manufacturerId, user.getUserId());
    }
}
