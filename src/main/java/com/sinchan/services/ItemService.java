package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.Items;
import org.springframework.transaction.annotation.Transactional;

public interface ItemService {

    void save(Items item, int userId);

    List<Items> listItems(int userId);

    Items findById(int id, int userId);

    void delete(int itemId, int userId);

    void update(Items item, int userId);

    List<Dictionary> listUnits(int userId);
}
