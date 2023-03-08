package site.shkrr.kreamAuction.service.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.controller.dto.AddressDto.SaveRequest;
import site.shkrr.kreamAuction.domain.address.Address;
import site.shkrr.kreamAuction.domain.address.AddressRepository;
import site.shkrr.kreamAuction.domain.user.User;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    @Transactional
    public void save(SaveRequest requestDto, User loginUser){
        Address address=requestDto.toEntity();
        address.updateUser(loginUser);
        addressRepository.save(address);
    }


}
