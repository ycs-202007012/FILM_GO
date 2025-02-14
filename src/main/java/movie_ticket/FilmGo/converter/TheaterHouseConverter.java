package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseResponse;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterService;

@ExChangeEntity
@RequiredArgsConstructor
public class TheaterHouseConverter {

    private final TheaterService theaterService;

    public TheaterHouse toEntity(TheaterHouseForm form){
        return TheaterHouse.builder()
                .houseNumber(form.getHouseNumber())
                .theater(theaterService.findById(form.getTheaterId()))
                .seat(form.getSeat())
                .build();
    }

    public TheaterHouseResponse toResponse(TheaterHouse house){
        return TheaterHouseResponse.builder()
                .houseNumber(house.getHouseNumber())
                .theaterId(house.getTheater().getId())
                .seat(house.getSeat())
                .build();
    }
}
