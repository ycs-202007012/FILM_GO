package movie_ticket.FilmGo.converter;

import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.domain.Address;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;

@ExChangeEntity
public class TheaterConverter {

    public Theater toEntity(TheaterForm form) {
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        return Theater.builder()
                .name(form.getName())
                .address(address)
                .status(TheaterStatus.REGISTERED)
                .build();
    }

    public TheaterForm toForm(Theater theater) {
        return TheaterForm.builder()
                .name(theater.getName())
                .city(theater.getAddress().getCity())
                .zipcode(theater.getAddress().getZipcode())
                .street(theater.getAddress().getStreet())
                .build();
    }
}
