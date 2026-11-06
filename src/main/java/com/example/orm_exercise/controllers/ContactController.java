package com.example.orm_exercise.controllers;

import com.example.orm_exercise.models.Address;
import com.example.orm_exercise.models.Contact;
import com.example.orm_exercise.repositories.ContactRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable int id) {
        return contactRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {

        for (Address address : contact.getAddresses()) {
            address.setContact(contact);
        }

        return contactRepository.save(contact);
    }

    @PostMapping("/{contactId}/addresses")
    public Contact addAddress(
            @PathVariable int contactId,
            @RequestBody Address address) {

        return contactRepository.findById(contactId).map(contact -> {

            address.setContact(contact);

            contact.getAddresses().add(address);

            return contactRepository.save(contact);

        }).orElse(null);
    }
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable int id, @RequestBody Contact updatedContact) {
        return contactRepository.findById(id).map(contact -> {
            contact.setName(updatedContact.getName());
            contact.setEmail(updatedContact.getEmail());
            return contactRepository.save(contact);
        }).orElse(null);
    }

    @DeleteMapping("/{contactId}/addresses/{addressId}")
    public Contact deleteAddress(
            @PathVariable int contactId,
            @PathVariable int addressId) {

        return contactRepository.findById(contactId).map(contact -> {

            contact.getAddresses().removeIf(
                    address -> address.getId() == addressId
            );

            return contactRepository.save(contact);

        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable int id) {
        contactRepository.deleteById(id);
    }
}
