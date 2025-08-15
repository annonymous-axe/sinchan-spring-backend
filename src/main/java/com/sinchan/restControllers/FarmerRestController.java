package com.sinchan.restControllers;

import com.sinchan.entities.Farmer;
import com.sinchan.entities.Invoice;
import com.sinchan.entities.Tehsil;
import com.sinchan.entities.User;
import com.sinchan.services.FarmerService;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.LocationServices;
import com.sinchan.user.credentials.SinchanAuthToken;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FarmerRestController {

	private final FarmerService farmerService;
	private final LocationServices locationService;
	private final InvoiceService invoiceService;
	
	public FarmerRestController(FarmerService farmerService, LocationServices locationService,
								@Qualifier("invoice") InvoiceService invoiceService) {
		this.farmerService = farmerService;
		this.locationService = locationService;
		this.invoiceService = invoiceService;
	}
	
	@GetMapping("farmer/list")
	public List<Farmer> farmerList() {

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

		return farmerService.listFarmers(user.getUserId());
	}

	@PostMapping("farmer")
	public ResponseEntity<String> createFarmer(@RequestBody Farmer farmer){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

		farmerService.save(farmer, user.getUserId());

		return new ResponseEntity<>("Farmer created!", HttpStatus.CREATED);
	}

	@GetMapping("farmer/{farmerId}")
	public Farmer openFarmer(@PathVariable int farmerId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

		Farmer farmer = farmerService.findById(farmerId, user.getUserId());

		farmer.setTehsilList(locationService.tehsilList(farmer.getDistrict()));

		return farmer;
	}

	@PutMapping("farmer")
	public ResponseEntity<String> updateFarmer(@RequestBody Farmer farmer){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

		farmerService.update(farmer, user.getUserId());

		return new ResponseEntity<>("Updation successfull.", HttpStatus.CREATED);
	}

	@DeleteMapping("farmer")
	public ResponseEntity<String> deleteFarmer(@RequestParam int farmerId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

		farmerService.delete(farmerId, user.getUserId());

		return new ResponseEntity<>("Deleted resources", HttpStatus.NO_CONTENT);

	}
}