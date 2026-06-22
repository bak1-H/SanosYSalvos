package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.RegistroClient;
import cl.sys.bff.bfffsanys.mapper.LoginMapper;
import cl.sys.bff.bfffsanys.model.LoginRegistroResponseDTO;
import cl.sys.bff.bfffsanys.model.LoginRequestDTO;
import cl.sys.bff.bfffsanys.model.LoginResponseDTO;
import cl.sys.bff.bfffsanys.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final RegistroClient registroClient;
    private final LoginMapper loginMapper;

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        LoginRegistroResponseDTO registroResponse = registroClient.login(dto);
        return loginMapper.toLoginResponse(registroResponse);
    }
}
