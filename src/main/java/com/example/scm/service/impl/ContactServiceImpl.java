package com.example.scm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.repository.ContactRepo;
import com.example.scm.service.interfaces.ContactService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Service class ContactServiceImpl implements ContactService {

	private final ContactRepo contactRepo;

	@Override
	public Contact saveContact(Contact contact) {
		return contactRepo.save(contact);
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepo.findAll();
	}

	@Override
	public List<Contact> getUserContacts(User u) {
		return this.contactRepo.findByUser(u);
	}

	@Override
	public Page<Contact> getAllContactsOfUser(User user, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUser(user, pageable);
	}

	@Override
	public Page<Contact> searchContactsOfUserByName(User user, String keyword, int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndNameContaining(user, keyword, pageable);
	}

	@Override
	public Page<Contact> searchContactsOfUserByEmail(User user, String keyword, int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndEmailContaining(user, keyword, pageable);
	}

	@Override
	public Page<Contact> searchContactsOfUserByPhoneNumber(User user, String keyword, int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndPhoneNumberContaining(user, keyword, pageable);
	}

	@Override
	public Optional<Contact> getContactById(Long id) {
		return contactRepo.findById(id);
	}

	@Override
	public Contact updateContact(Contact contact) {
		Contact c = contactRepo.save(contact);
		return c;
	}

	@Override
	public boolean deleteContact(Long id) {
		contactRepo.deleteById(id);
		return (this.getContactById(id).isPresent()) ? false : true;
	}

	@Override
	public boolean deleteContact(Contact contact) {
		contactRepo.delete(contact);
		return (this.getContactById(contact.getId()).isPresent()) ? false : true;

	}

}
