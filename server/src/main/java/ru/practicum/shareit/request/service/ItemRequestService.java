package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addRequest(Integer userId, ItemRequest itemRequest);

    List<ItemRequestResponseDto> getAllUserRequests(Integer userId);

    List<ItemRequestResponseDto> getRequestsFromOtherUsers(Integer userId);

    ItemRequestResponseDto getRequestById(Long requestId);
}
