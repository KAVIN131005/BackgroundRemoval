package in.Kavin.removebg.service;

import in.Kavin.removebg.dto.UserDTO;

public interface UserService {
     UserDTO saveUser(UserDTO userDTO);

     UserDTO getUserByClerkId(String clerkId);
   void deleteUserByClerkId(String clerkId);
}
