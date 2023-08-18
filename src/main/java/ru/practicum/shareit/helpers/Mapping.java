package ru.practicum.shareit.helpers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
public class Mapping {
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper mapper = new ModelMapper();
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
