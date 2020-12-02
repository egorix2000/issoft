package by.bychenok.building.floor;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FloorSystem {
    private final List<Floor> floors;

    public FloorSystem(int numberOfFloors) {
        floors = ImmutableList.copyOf(
                IntStream.range(0, numberOfFloors)
                        .mapToObj(i -> new Floor(i))
                        .collect(Collectors.toList()));
    }
}
