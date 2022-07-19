package com.flab.commerce.domain.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterDto {

    @NotBlank
    @Length(min = 2)
    private String name;

    @Length(min = 9)
    private String phone;

    private String address;

    private String description;

    private String image;

    public Store toStore(Long ownerId) {
        return Store.builder()
                .name(name)
                .phone(phone)
                .address(address)
                .status(StoreStatus.OPEN)
                .description(description)
                .image(image)
                .ownerId(ownerId)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

    }
}
