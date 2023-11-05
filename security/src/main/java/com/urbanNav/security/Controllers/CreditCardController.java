package com.urbanNav.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanNav.security.Models.CreditCard;
import com.urbanNav.security.Repositories.CreditCardRepository;
import com.urbanNav.security.Services.EncryptionService;

@CrossOrigin
@RestController
@RequestMapping("/credit-card")
public class CreditCardController {

    @Autowired
    private CreditCardRepository cardRepository;

    @Autowired
    private EncryptionService encryp;

    @GetMapping("")
    public List<CreditCard> index() {
        return this.cardRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public CreditCard store(@RequestBody CreditCard card) {
        card.setCardCVV(encryp.convertirSHA256(card.getCardCVV()));
        card.setCardNumber(encryp.convertirSHA256(card.getCardNumber()));
        card.setExpiryDate(encryp.convertirSHA256(card.getExpiryDate()));
        return this.cardRepository.save(card);
    }

    @GetMapping("{id}")
    public CreditCard show(@PathVariable String id) {
        CreditCard card = this.cardRepository.findById(id).orElse(null);
        return card;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    public CreditCard update(@PathVariable String id, @RequestBody CreditCard card) {
        CreditCard current = this.cardRepository.findById(id).orElse(null);
        if (current != null) {
            current.setName(card.getName());
            current.setType(card.getType());
            current.setCardNumber(card.getCardNumber());
            current.setCardCVV(card.getCardCVV());
            current.setExpiryDate(card.getExpiryDate());
            current.setBalance(card.getBalance());
            return this.cardRepository.save(current);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        CreditCard card = this.cardRepository.findById(id).orElse(null);
        if (card != null) {
            this.cardRepository.delete(card);
        }
    }
}
