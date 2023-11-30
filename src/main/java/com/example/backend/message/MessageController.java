package com.example.backend.message;

import com.example.backend.loan_request.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    // Get all my messages that should be displayed
    @PostMapping(path = "/getMyMessages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> getMyMessages(
            @RequestParam("user_email") String user_email
    )
    {
        return messageService.getMyMessages(user_email);
    }
    // Send a message to a certain user
    @PostMapping(path = "/sendMessage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> sendMessage(
            @RequestParam("content") String content,
            @RequestParam("sender_email") String sender_email,
            @RequestParam("receiver_email") String receiver_email
    )
    {
        return messageService.sendMessage(content, sender_email, receiver_email);
    }

    // Changes the state of message to read
    @PutMapping(path = "/openMessage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> openMessage(
            @RequestParam("message_id") int message_id
    )
    {
        return messageService.openMessage(message_id);
    }

    // Deletes a message from the database
    @DeleteMapping(path = "/deleteMessage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> deleteMessage(
            @RequestParam("message_id") int message_id
    )
    {
        return messageService.deleteMessage(message_id);
    }
}
