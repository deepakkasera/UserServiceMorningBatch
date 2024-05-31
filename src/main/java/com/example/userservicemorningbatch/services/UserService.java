package com.example.userservicemorningbatch.services;

import com.example.userservicemorningbatch.configs.KafkaProducerClient;
import com.example.userservicemorningbatch.dtos.SendEmailDto;
import com.example.userservicemorningbatch.exceptions.InvalidPasswordException;
import com.example.userservicemorningbatch.exceptions.InvalidTokenException;
import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.repositories.TokenRepository;
import com.example.userservicemorningbatch.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.common.network.Send;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private KafkaProducerClient kafkaProducerClient;
    private ObjectMapper objectMapper;

    UserService(UserRepository userRepository,
                BCryptPasswordEncoder bCryptPasswordEncoder,
                TokenRepository tokenRepository,
                KafkaProducerClient kafkaProducerClient,
                ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }

    public User signUp(String email, String password, String name) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            //user is already present in the DB, so no need to signup
            return optionalUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        //Once the signup is complete, send a message to Kafka for sending an email to the User.
        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setTo(user.getEmail());
        sendEmailDto.setFrom("admin@scaler.com");
        sendEmailDto.setSubject("Welcome to Scaler");
        sendEmailDto.setBody("Thanks for joining Scaler");

        try {
            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(sendEmailDto));
        } catch (JsonProcessingException e) {
            System.out.println("Something went wrong while sending a message to Kafka");
        }

        return userRepository.save(user);
    }

    public Token login(String email, String password) throws InvalidPasswordException {
        /*
        1. Check if the use exists with the given email or not.
        2. If not, throw an exception or redirect the user to signup.
        3. If yes, then compare the incoming password with the password stored in the DB.
        4. If password matches then login successful and return new token.
         */
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            //User with given email isn't present in DB.
            return null;
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            //throw an exception
            throw new InvalidPasswordException("Please enter correct password");
        }

        //Login successful, generate a new token.
        Token token = generateToken(user);

        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {
        LocalDate currentTime = LocalDate.now(); // current time.
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);

        //Token value is a randomly generated String of 128 characters.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);

        return token;
    }

    public void logout(String tokenValue) throws InvalidTokenException {
        //Validate if the given token is present in the DB as well as is_deleted = false.
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(
                tokenValue,
                false
        );

        if (optionalToken.isEmpty()) {
            //Throw an exception
            throw new InvalidTokenException("Invalid token passed.");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);

        return;
    }

    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(token, false);

        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token passed.");
        }

        return optionalToken.get().getUser();
    }
}
