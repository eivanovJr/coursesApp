package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }

    public void delete(UserDto userDto) {
        userRepository.delete(userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException("User not found!")));
    }

    public void save(UserDto userDto) {
        userRepository.save(userMapper.toUserFromDto(userDto));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public boolean isEnrolled(Long userId, Long courseId){
        Optional<User> user = userRepository.findIfUserIsEnrolledOnCourse(userId, courseId);
        return user.isPresent();
    }


    public List<UserDto> findUsersNotAssignedToCourse(Long courseId) {
        return userMapper.convertToDtoList(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return userMapper.convertToDtoList(userRepository.findByUsernameLike(username));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return  userMapper.convertToDtoList(new ArrayList<>(userRepository.getUsersOfCourse(id)));
    }

    public UserDto findUserByUsername(String remoteUser) {
        return userRepository.findUserByUsername(remoteUser).map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
