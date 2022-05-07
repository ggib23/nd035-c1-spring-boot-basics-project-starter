package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    private void encryptPassword(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
    }

    public int createCredential(Credential credential) {
        encryptPassword(credential);
        return credentialMapper.addCredential(new Credential(null, credential.getUrl(), credential.getUsername(), credential.getPassword(), credential.getKey(), credential.getUserId()));
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredentialId(credentialId);
    }

    public void updateCredential(Credential credential) {
        encryptPassword(credential);
        credentialMapper.updateCredential(credential);
    }
}
