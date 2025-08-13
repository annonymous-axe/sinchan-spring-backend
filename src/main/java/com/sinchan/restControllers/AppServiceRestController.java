package com.sinchan.restControllers;

import com.sinchan.entities.District;
import com.sinchan.entities.Tehsil;
import com.sinchan.services.LocationServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AppServiceRestController {

    private final LocationServices locationService;

    public AppServiceRestController(LocationServices locationServices){
        this.locationService = locationServices;
    }

    @GetMapping("district/list")
    public List<District> districtList() {

        return locationService.districtList();
    }

    @PostMapping("tehsil/list")
    public List<Tehsil> tehsilList(@RequestParam("districtId") int districtId) {

        return locationService.tehsilList(districtId);
    }

}
