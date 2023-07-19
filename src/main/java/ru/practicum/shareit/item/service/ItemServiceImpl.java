package ru.practicum.shareit.item.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        super();
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Item> getAllItems(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Can't add item", HttpStatus.NOT_FOUND));
        return itemRepository.findByOwner(user);
    }

    @Override
    public Item createItem(long userId, Item item) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Can't add item", HttpStatus.NOT_FOUND));

        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(long userId, long id, ItemDto itemRequest) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found", HttpStatus.NOT_FOUND));

        String name = itemRequest.getName();
        String description = itemRequest.getDescription();
        Boolean available = itemRequest.getAvailable();
        ItemRequest request = itemRequest.getRequest();

        User find = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Can't add item", HttpStatus.NOT_FOUND));

        if (find != null) {
            if (item.getOwner().getId() != find.getId()) {
                throw new ResourceNotFoundException("Item not found in items of '" + find.getId() + "' user", HttpStatus.NOT_FOUND);
            }
            item.setOwner(find);
        }

        if (name != null) {
            item.setName(itemRequest.getName());
        }
        if (description != null) {
            item.setDescription(itemRequest.getDescription());
        }
        if (available != null) {
            item.setAvailable(itemRequest.getAvailable());
        }
        if (request != null) {
            item.setRequest(itemRequest.getRequest());
        }
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(long userId, long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found", HttpStatus.NOT_FOUND));
        itemRepository.delete(item);
    }

    @Override
    public Item getItemById(long userId, long id) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Item not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Item> searchByName(long userId, String partOfName) {
        if (partOfName.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.findDistinctByDescriptionContainsIgnoreCaseAndAvailableOrNameContainsIgnoreCaseAndAvailable(partOfName, true, partOfName, true);
        }
    }
}


