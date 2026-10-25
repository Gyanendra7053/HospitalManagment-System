package user.service;

import user.dto.UserDto;
import user.exception.HmsExcpetion;

public interface UserService {

    public void registerUser(UserDto userDto) throws HmsExcpetion;

    public UserDto loginUser(UserDto userDto) throws HmsExcpetion; 

    public UserDto getUserById(Long id) throws HmsExcpetion;

    public void updateUser(UserDto userDto);

}
