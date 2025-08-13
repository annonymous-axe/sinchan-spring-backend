package com.sinchan.restControllers;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.Items;
import com.sinchan.services.ItemService;
import com.sinchan.utility.LabelValService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ItemRestController {

    private final ItemService itemService;
    private final LabelValService labelValService;

    public ItemRestController(ItemService itemService, LabelValService labelValService){
        this.itemService = itemService;
        this.labelValService = labelValService;
    }

    @GetMapping("item/list")
    public List<Items> itemsList(){

        return itemService.listItems(100);
    }

    @PostMapping("item")
    public ResponseEntity<String> saveItem(@RequestBody Items item){

        itemService.save(item, 100);

        return new ResponseEntity<>("Item saved!", HttpStatus.CREATED);

    }

    @GetMapping("item")
    public Items openItem(@RequestParam int itemId){

        return itemService.findById(itemId, 100);
    }

    @PutMapping("item")
    public ResponseEntity<String> updateItem(@RequestBody Items item){

        itemService.update(item, 100);

        return new ResponseEntity<>("Item updated!", HttpStatus.CREATED);
    }

    @DeleteMapping("item")
    public ResponseEntity<String> deleteItem(@RequestParam int itemId){

        itemService.delete(itemId, 100);

        return new ResponseEntity<>("Item deleted!", HttpStatus.NO_CONTENT);

    }

    @GetMapping("item/list/from-category-id")
    public List<Dictionary> dictionaryList(@RequestParam int categoryId){

        return labelValService.getItemFromCategoryLabelValItemList(categoryId, 100);
    }

    @GetMapping("item/details")
    public Dictionary itemDetailsFromItemId(@RequestParam("itemId") int itemId, @RequestParam("manufacturerId") int manufacturerId){

        return labelValService.getItemDetails(itemId, manufacturerId, 100);
    }
}
