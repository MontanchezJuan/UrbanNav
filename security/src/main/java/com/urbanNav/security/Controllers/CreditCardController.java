package com.urbanNav.security.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> index() {
        try {
            if (this.cardRepository.findAll() != null && this.cardRepository.findAll().isEmpty() == false) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.cardRepository.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay tarjetas de credito registradas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar tarjetas de credito" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<?> store(@RequestBody CreditCard card) {
        try {
            CreditCard theCreditCard = this.cardRepository.getCreditCard(encryp.convertirSHA256(card.getCardNumber()))
                    .orElse(null);
            if (theCreditCard != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe una tarjeta de credito con este numero");
            } else {
                card.setCardCVV(encryp.convertirSHA256(card.getCardCVV()));
                card.setCardNumber(encryp.convertirSHA256(card.getCardNumber()));
                card.setExpiryDate(encryp.convertirSHA256(card.getExpiryDate()));
                this.cardRepository.save(card);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Tarjeta de credito agregado con éxito." + "\n" + card);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar crear la tarjeta de credito" + "\n" + e.toString());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            CreditCard card = this.cardRepository.findById(id).orElse(null);
            if (card != null) {
                return ResponseEntity.status(HttpStatus.OK).body(card);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro la tarjeta de credito");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la busqueda de la tarjeta de credito" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CreditCard card) {
        try {
            CreditCard actualCard = this.cardRepository.findById(id).orElse(null);
            if (actualCard != null) {
                if (actualCard.getCardNumber().equals(card.getCardNumber()) == false
                        && this.cardRepository.getCreditCard(encryp.convertirSHA256(card.getCardNumber()))
                                .orElse(null) == null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Ya existe una tarjeta de credito con este numero");
                } else {
                    actualCard.setName(card.getName());
                    actualCard.setType(card.getType());
                    actualCard.setCardCVV(encryp.convertirSHA256(card.getCardCVV()));
                    actualCard.setCardNumber(encryp.convertirSHA256(card.getCardNumber()));
                    actualCard.setExpiryDate(encryp.convertirSHA256(card.getExpiryDate()));
                    actualCard.setBalance(card.getBalance());
                    actualCard.setStatus(card.getstatus());
                    this.cardRepository.save(actualCard);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Tarjeta de credito actualizada" + "\n" + actualCard);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro la tarjeta de credito a actualizar");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la actualizacion de la tarjeta de credito" + "\n" + e.toString());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            CreditCard card = this.cardRepository.findById(id).orElse(null);
            if (card != null) {
                this.cardRepository.delete(card);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se elimino correctamente la tarjeta de credito:" + "\n" + card);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontro la tarjeta de credito a eliminar");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la eliminacion de la tarjeta de credito" + "\n" + e.toString());
        }
    }
}
