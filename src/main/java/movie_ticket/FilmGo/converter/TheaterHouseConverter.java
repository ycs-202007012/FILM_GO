package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.service.TheaterService;

@ExChangeEntity
@RequiredArgsConstructor
public class TheaterHouseConverter {

    private final TheaterService theaterService;

    /*public TheaterHouse toEntity(TheaterHouseForm form) {
        return TheaterHouse.builder()
                .houseName(form.getHouseName())
                .theater(theaterService.findById(form.getTheaterId()))
                .seat(form.getSeat())
                .build();
    }

    public TheaterHouseResponse toResponse(TheaterHouse house) {
        return TheaterHouseResponse.builder()
                .houseNumber(house.getHouseName())
                .theaterId(house.getTheater().getId())
                .seat(house.getSeat())
                .build();
    }*/
}
