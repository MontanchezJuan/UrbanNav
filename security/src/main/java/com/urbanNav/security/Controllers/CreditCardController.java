package com.urbanNav.security.Controllers;

import java.util.List;

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
import com.urbanNav.security.Services.JSONResponsesService;

@CrossOrigin
@RestController
@RequestMapping("/credit-card")
public class CreditCardController {

    @Autowired
    private CreditCardRepository cardRepository;

    @Autowired
    private EncryptionService encryp;
    @Autowired
    private JSONResponsesService jsonResponsesService;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            if (this.cardRepository.findAll() != null && this.cardRepository.findAll().isEmpty() == false) {
                List<CreditCard> creditCards = this.cardRepository.findAll();
                this.jsonResponsesService.setData(creditCards);
                this.jsonResponsesService.setMessage(null);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No hay tarjetas de credito registradas");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al buscar tarjetas de credito");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<?> store(@RequestBody CreditCard card) {
        try {
            CreditCard theCreditCard = this.cardRepository.getCreditCard(encryp.convertirSHA256(card.getCardNumber()))
                    .orElse(null);
            if (theCreditCard != null) {
                this.jsonResponsesService.setMessage("Ya existe una tarjeta de credito con este numero");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                card.setCardCVV(encryp.convertirSHA256(card.getCardCVV()));
                card.setCardNumber(encryp.convertirSHA256(card.getCardNumber()));
                this.cardRepository.save(card);
                this.jsonResponsesService.setMessage("Tarjeta de credito agregado con éxito");
                this.jsonResponsesService.setData(card);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al intentar crear la tarjeta de credito");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            CreditCard card = this.cardRepository.findById(id).orElse(null);
            if (card != null) {
                this.jsonResponsesService.setData(card);
                this.jsonResponsesService.setMessage("Tarjeta de credito encontrada con exito");
                return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro la tarjeta de credito");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la busqueda de la tarjeta de credito");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
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
                    this.jsonResponsesService.setMessage("Ya existe una tarjeta de credito con este numero");
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(this.jsonResponsesService.getFinalJSON());
                } else {
                    actualCard.setName(card.getName());
                    actualCard.setType(card.getType());
                    actualCard.setCardCVV(encryp.convertirSHA256(card.getCardCVV()));
                    actualCard.setCardNumber(encryp.convertirSHA256(card.getCardNumber()));
                    actualCard.setExpiryDate(encryp.convertirSHA256(card.getExpiryDate()));
                    actualCard.setBalance(card.getBalance());
                    actualCard.setStatus(card.getstatus());
                    this.cardRepository.save(actualCard);
                    this.jsonResponsesService.setData(actualCard);
                    this.jsonResponsesService.setMessage("Tarjeta de credito actualizada");
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(this.jsonResponsesService.getFinalJSON());
                }
            } else {
                this.jsonResponsesService.setMessage("No se encontro la tarjeta de credito a actualizar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());

            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la actualizacion de la tarjeta de credito");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        try {
            CreditCard card = this.cardRepository.findById(id).orElse(null);
            if (card != null) {
                this.cardRepository.delete(card);
                this.jsonResponsesService.setData(card);
                this.jsonResponsesService.setMessage("Se elimino correctamente la tarjeta de credito");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No se encontro la tarjeta de credito a eliminar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error en la eliminacion de la tarjeta de credito");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }
}
