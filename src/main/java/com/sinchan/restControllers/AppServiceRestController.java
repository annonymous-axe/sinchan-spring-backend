package com.sinchan.restControllers;

import com.sinchan.entities.District;
import com.sinchan.entities.Tehsil;
import com.sinchan.services.LocationServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
public class AppServiceRestController {

    private final LocationServices locationService;

    public AppServiceRestController(LocationServices locationServices){
        this.locationService = locationServices;
    }

    @GetMapping("district/list")
    public List<District> districtList(HttpServletRequest request) {

        Locale locale = RequestContextUtils.getLocale(request);

        return locationService.districtList();
    }

    @PostMapping("tehsil/list")
    public List<Tehsil> tehsilList(@RequestParam("districtId") int districtId) {

        return locationService.tehsilList(districtId);
    }

}
