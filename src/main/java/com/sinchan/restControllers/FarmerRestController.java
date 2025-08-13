package com.sinchan.restControllers;

import com.sinchan.entities.Farmer;
import com.sinchan.entities.Invoice;
import com.sinchan.entities.Tehsil;
import com.sinchan.entities.User;
import com.sinchan.services.FarmerService;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.LocationServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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
	public List<Farmer> clientViewPage() {

//		User user = (User) session.getAttribute("user");

		return farmerService.listFarmers(100);
	}

	@PostMapping("farmer")
	public ResponseEntity<String> createFarmer(@RequestBody Farmer farmer){

		farmerService.save(farmer, 100);

		return new ResponseEntity<>("Farmer created!", HttpStatus.CREATED);
	}

	@GetMapping("farmer/{farmerId}")
	public Farmer openFarmer(@PathVariable int farmerId){

		Farmer farmer = farmerService.findById(farmerId, 100);

		farmer.setTehsilList(locationService.tehsilList(farmer.getDistrict()));

		return farmer;
	}

	@PutMapping("farmer")
	public ResponseEntity<String> updateFarmer(@RequestBody Farmer farmer){

		farmerService.update(farmer, 100);

		return new ResponseEntity<>("Updation successfull.", HttpStatus.CREATED);
	}

	@DeleteMapping("farmer")
	public ResponseEntity<String> deleteFarmer(@RequestParam int farmerId){

		farmerService.delete(farmerId, 100);

		return new ResponseEntity<>("Deleted resources", HttpStatus.NO_CONTENT);

	}
}