package com.example.backend.message;

import com.example.backend.loan_request.LoanRequest;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,UserRepository userRepository){

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // Get all my messages
    public ResponseEntity<Map<String,Object>> getMyMessages(String user_email){

        Map<String, Object> mapa = new HashMap<>();
        int number_of_unread = 0;

        List<Message> allMessages = messageRepository.findAll();
        if (allMessages.isEmpty()){
            mapa.put("error","Ziadne spravy");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);
        }
        List<Message> myMessages = new ArrayList<>();
        for(Message message:allMessages){
            if(message.getReceiver().getEmail().equals(user_email)){
                myMessages.add(message);
                if (message.getStatus().equals("UNREAD")){
                    number_of_unread++;
                }
            }
        }
        if (myMessages.isEmpty()){
            mapa.put("error","Ziadne spravy");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);
        }
        Collections.sort(myMessages,Comparator.comparing(Message::getCreated_date).reversed());
        mapa.put("success",myMessages);
        mapa.put("number_of_unread",number_of_unread);
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Send a message to somebody
    // Transactional, because more operations in the database are being done
    @Transactional
    public ResponseEntity<Map<String,Object>> sendMessage(String content,String sender_email,String receiver_email){
        Map<String, Object> mapa = new HashMap<>();

        Optional<User> checkReceiver = userRepository.findById(receiver_email);

        if (checkReceiver.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašiel sa zadaný email!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }
        Optional<User> Sender = userRepository.findById(sender_email);

        Message message = new Message();
        message.setStatus("UNREAD");
        message.setContent(content);
        message.setSender(Sender.get());
        message.setReceiver(checkReceiver.get());
        message.setCreated_date(new Date());

        messageRepository.save(message);
        mapa.put("success",message);
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Change the state of a message to read
    public ResponseEntity<Map<String,Object>> openMessage(int message_id){
        Map<String, Object> mapa = new HashMap<>();

        Optional<Message> message = messageRepository.findById(message_id);

        if (message.isEmpty()){
            mapa.put("error", "Nenašla sa zadaná správa!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        message.get().setStatus("READ");
        messageRepository.save(message.get());

        mapa.put("success","Úspešne prečítané.");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Delete a message
    public ResponseEntity<Map<String,Object>> deleteMessage(int message_id){
        Map<String, Object> mapa = new HashMap<>();

        Optional<Message> message = messageRepository.findById(message_id);

        if (message.isEmpty()){
            mapa.put("error", "Nenašla sa zadaná správa!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        messageRepository.delete(message.get());

        mapa.put("success","Úspešne odstránené.");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }


}
