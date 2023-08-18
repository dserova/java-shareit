package ru.practicum.shareit.helpers;

import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.EmailRandomizer;
import org.jeasy.random.randomizers.range.LocalDateTimeRangeRandomizer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.jeasy.random.FieldPredicates.*;

@RequiredArgsConstructor
public class Generate {
    public <T> T random(Class<T> targetClass) {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(12345L)
                .randomize(named("email").and(ofType(String.class)), () -> new EmailRandomizer().getRandomValue())
                .randomize(named("available").and(ofType(Boolean.class)), () -> true)
                .objectPoolSize(100)
                .randomizationDepth(15)
                .charset(StandardCharsets.UTF_8)
                .stringLengthRange(5, 50)
                .collectionSizeRange(1, 10)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(false)
                .randomize(named("start").and(ofType(LocalDateTime.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusSeconds(15),
                        LocalDateTime.now().plusDays(1)
                ).getRandomValue())
                .randomize(named("end").and(ofType(LocalDateTime.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(30)
                ).getRandomValue())
                .ignoreRandomizationErrors(true);


        EasyRandom generator = new EasyRandom(parameters);
        return generator.nextObject(targetClass);
    }

    public <T> T randomNullId(Class<T> targetClass) {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(12345L)
                .randomize(named("email").and(ofType(String.class)), () -> new EmailRandomizer().getRandomValue())
                .randomize(named("available").and(ofType(Boolean.class)), () -> true)
                .objectPoolSize(100)
                .randomizationDepth(15)
                .charset(StandardCharsets.UTF_8)
                .stringLengthRange(5, 50)
                .collectionSizeRange(1, 10)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(false)
                .randomize(named("start").and(ofType(LocalDateTime.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusSeconds(15),
                        LocalDateTime.now().plusDays(1)
                ).getRandomValue())
                .randomize(named("end").and(ofType(LocalDateTime.class)), () -> new LocalDateTimeRangeRandomizer(
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(30)
                ).getRandomValue())
                .ignoreRandomizationErrors(true)
                .excludeField(named("id").and(inClass(targetClass)));


        EasyRandom generator = new EasyRandom(parameters);
        return generator.nextObject(targetClass);
    }
}