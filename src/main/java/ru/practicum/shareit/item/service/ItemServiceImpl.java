package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findByOwner_Id(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Item createItem(long userId, Item request) {
        request.setOwner(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
        return itemRepository.save(request);
    }

    @Override
    public Item updateItem(long userId, long id, ItemDto request) {

        Item item = itemRepository.findByIdAndOwner_Id(id, userId)
                .orElseThrow(ItemNotFoundException::new);

        Optional.ofNullable(request.getName()).ifPresent(item::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(request.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(request.getRequest()).ifPresent(item::setRequest);

        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(long userId, long id) {
        itemRepository.delete(itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new));
    }

    @Override
    public Item getItemById(long userId, long id) {
        return itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public List<Item> getItemByName(long userId, String partOfName) {
        return itemRepository.findDistinctByDescriptionContainsIgnoreCaseAndAvailableOrNameContainsIgnoreCaseAndAvailable(partOfName, true, partOfName, true)
                .orElseThrow(ItemNotFoundException::new);
    }
}


