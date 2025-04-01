package com.sms.multitenantschool.model.dto;


import com.sms.multitenantschool.model.entity.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message = "Street is mandatory")
    private String street;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "State is mandatory")
    private String state;
    @NotBlank(message = "Country is mandatory")
    private String country;

    public Address toEntity(){
            Address address = new Address();
             address.setStreet(this.street);
              address.setCity(this.city);
              address.setState(this.state);
              address.setCountry(this.country);

              return address;
    }
}