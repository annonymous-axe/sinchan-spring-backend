package com.sinchan.restControllers;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.Items;
import com.sinchan.entities.User;
import com.sinchan.services.ItemService;
import com.sinchan.user.credentials.SinchanAuthToken;
import com.sinchan.utility.LabelValService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemRestController {

    private final ItemService itemService;
    private final LabelValService labelValService;

    public ItemRestController(ItemService itemService, LabelValService labelValService){
        this.itemService = itemService;
        this.labelValService = labelValService;
    }

    @GetMapping("item/list")
    public List<Items> itemsList(){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return itemService.listItems(user.getUserId());
    }

    @PostMapping("item")
    public ResponseEntity<String> saveItem(@RequestBody Items item){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        itemService.save(item, user.getUserId());

        return new ResponseEntity<>("Item saved!", HttpStatus.CREATED);

    }

    @GetMapping("item")
    public Items openItem(@RequestParam int itemId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return itemService.findById(itemId, user.getUserId());
    }

    @PutMapping("item")
    public ResponseEntity<String> updateItem(@RequestBody Items item){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        itemService.update(item, user.getUserId());

        return new ResponseEntity<>("Item updated!", HttpStatus.CREATED);
    }

    @DeleteMapping("item")
    public ResponseEntity<String> deleteItem(@RequestParam int itemId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        itemService.delete(itemId, user.getUserId());

        return new ResponseEntity<>("Item deleted!", HttpStatus.NO_CONTENT);

    }

    @GetMapping("item/list/from-category-id")
    public List<Dictionary> dictionaryList(@RequestParam int categoryId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return labelValService.getItemFromCategoryLabelValItemList(categoryId, user.getUserId());
    }

    @GetMapping("item/details")
    public Dictionary itemDetailsFromItemId(@RequestParam("itemId") int itemId, @RequestParam("manufacturerId") int manufacturerId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return labelValService.getItemDetails(itemId, manufacturerId, user.getUserId());
    }
}
