package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestResponseDto addRequest(Integer userId, ItemRequest itemRequest) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в БД"));
        itemRequest.setRequestor(requestor);
        itemRequestRepository.save(itemRequest);
        return RequestMapper.toResponseDto(itemRequest, new ArrayList<>());
    }

    @Override
    public List<ItemRequestResponseDto> getAllUserRequests(Integer userId) {
        ArrayList<ItemRequestResponseDto> result = new ArrayList<>();
        List<ItemRequest> userRequests = itemRequestRepository.findByRequestor_Id(userId);
        for (ItemRequest request : userRequests) {
            ItemRequestResponseDto responseDto = getResponseDtoRequest(request);
            result.add(responseDto);
        }
        return result;
    }

    @Override
    public List<ItemRequestResponseDto> getRequestsFromOtherUsers(Integer userId) {
        ArrayList<ItemRequestResponseDto> result = new ArrayList<>();
        List<ItemRequest> userRequests = itemRequestRepository.findByRequestorIdNot(userId);
        for (ItemRequest request : userRequests) {
            ItemRequestResponseDto responseDto = getResponseDtoRequest(request);
            result.add(responseDto);
        }
        return result;
    }

    @Override
    public ItemRequestResponseDto getRequestById(Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запроса с таким id не найдено"));
        return getResponseDtoRequest(request);
    }

    private ItemRequestResponseDto getResponseDtoRequest(ItemRequest request) {
        List<Item> requestItems = itemRepository.findByRequestId(request.getId());
        return RequestMapper.toResponseDto(request, requestItems);
    }
}
