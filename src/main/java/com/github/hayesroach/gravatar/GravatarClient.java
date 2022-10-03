package com.github.hayesroach.gravatar;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.hayesroach.gravatar.Helper.*;

/*
 =========================================================
 * Copyright 2022 Hayes Roach (https://github.com/hayes-roach)
 * Licensed under MIT (https://github.com/hayes-roach/gravatar-java-client/LICENSE)
 =========================================================
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

/**
 * Java client that implements the Gravatar XML-RPC API defined here: https://en.gravatar.com/site/implement/xmlrpc/
 */
public class GravatarClient {

    private static final String PASSWORD = "password";
    private static final String HASHES = "hashes";
    private static final String BASE64 = "data";
    private static final String RATING = "rating";
    private static final String IMAGE_URL = "url";
    private static final String USER_IMAGE = "userimage";
    private static final String ADDRESSES = "addresses";
    private static final String GRAVATAR_XML_RPC_BASE_URL = "https://secure.gravatar.com/xmlrpc?user=";

    private final XMLRPCClient client;
    private final String email;
    private final ConcurrentHashMap<String, Object> parametersMap;

    /**
     * @param email email address of Gravatar account
     * @param password password of Gravatar account used for authenticating request
     */
    public GravatarClient(String email, String password) throws MalformedURLException {
        this.client = new XMLRPCClient(new URL(GRAVATAR_XML_RPC_BASE_URL + md5Hex(email)));
        this.email = email;
        this.parametersMap = new ConcurrentHashMap<>(1);
        this.parametersMap.put(PASSWORD, password);
    }

    /**
     * Checks whether emails have a gravatar
     * @param emails list of email addresses
     * @return
     * Returns a map of emails and booleans stating if a gravatar exists or not
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> gravatarExists(List<String> emails) throws XMLRPCException {
        Map<String, String> emailHashMap = new HashMap<>();
        emails.forEach(email -> emailHashMap.put(md5Hex(email), email));
        parametersMap.put(HASHES, emailHashMap.keySet());
        Map<String, Integer> map = (Map<String, Integer>) client.call("grav.exists", parametersMap);
        parametersMap.remove(HASHES);
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> emailHashMap.get(entry.getKey()),
                entry -> entry.getValue().equals(0) ? Boolean.FALSE : Boolean.TRUE)
        );
    }

    /**
     * Checks whether an email has a gravatar
     * @param email list of email addresses
     * @return
     * Returns a boolean stating if a gravatar exists or not
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Boolean gravatarExists(String email) throws XMLRPCException {
        String hash = md5Hex(email);
        List<String> hashList = new ArrayList<>();
        hashList.add(md5Hex(email));
        parametersMap.put(HASHES, hashList);
        Map<String, Integer> map = (Map<String, Integer>) client.call("grav.exists", parametersMap);
        parametersMap.remove(HASHES);
        return map.getOrDefault(hash, 0).equals(0) ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * Gets the list of emails associated with the account
     * @return Returns the List of email addresses associated with the account
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public List<String> getEmails() throws XMLRPCException {
        Map<String, Map<String, Object>> response = (HashMap<String, Map<String, Object>>) client.call("grav.addresses", parametersMap);
        return new ArrayList<>(response.keySet());
    }

    /**
     * Returns the current Gravatar for an email
     * @param email email address of gravatar account
     * @return
     * Returns the current Gravatar for the account
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Gravatar getCurrentGravatar(String email) throws XMLRPCException {
        Map<String, Map<String, Object>> response = (HashMap<String, Map<String, Object>>) client.call("grav.addresses", parametersMap);
        Map<String, Object> userImageMap = response.get(email);
        return new Gravatar(userImageMap.getOrDefault("userimage", "").toString(),
                getRating((Integer) userImageMap.get("rating")),
                userImageMap.get("userimage_url").toString());
    }


    /**
     * Returns a list of all of the available Gravatars associated with the account
     * @return List of Gravatars
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public List<Gravatar> getAllGravatars() throws XMLRPCException {
        Map<String, Object[]> response = (HashMap<String, Object[]>) client.call("grav.userimages", parametersMap);
        return response.entrySet().stream()
                .map(entry -> new Gravatar(entry.getKey(), getRating(entry.getValue()[0].toString()), entry.getValue()[1].toString()))
                .collect(Collectors.toList());
    }

    /**
     * Uploads a Gravatar to the account. It does not set the current Gravatar to this image, use setGravatar to do that.
     *
     * InputSteam is closed after this method is called.
     * @param inputStream InputStream of an image
     * @param rating Rating of the image. Rating can be one of the following: [G, PG, R, X]
     * @return Returns Gravatar on success
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Gravatar upload(InputStream inputStream, Rating rating) throws XMLRPCException, IOException {
        try {
            parametersMap.put(BASE64, encodeInputStreamToBase64(inputStream));
            inputStream.close();
            return getUserImageBase64(rating);
        }catch (XMLRPCException e) {
            parametersMap.remove(BASE64);
            parametersMap.remove(RATING);
            throw e;
        }
    }

    private Gravatar getUserImageBase64(Rating rating) throws XMLRPCException, IOException {
        parametersMap.put(RATING, rating.getNumberValue());
        String userImageId = (String) client.call("grav.saveData", parametersMap);
        parametersMap.remove(BASE64);
        parametersMap.remove(RATING);
        if(userImageId.equals("0")) {
            throw new IOException("Image failed to upload");
        }
        return new Gravatar(userImageId, rating);
    }

    /**
     * Uploads a Gravatar to the account. It does not set the current Gravatar to this image, use setGravatar to do that.
     *
     * InputSteam is closed after this method is called.
     * @param file image file
     * @param rating Rating of the image. Rating can be one of the following: [G, PG, R, X]
     * @return Returns Gravatar on success
     * @throws XMLRPCException io exception
     */
    public Gravatar upload(File file, Rating rating) throws XMLRPCException, IOException {
        try {
            parametersMap.put(BASE64, encodeFileToBase64(file));
            return getUserImageBase64(rating);
        }catch (XMLRPCException e) {
            parametersMap.remove(BASE64);
            parametersMap.remove(RATING);
            throw e;
        }
    }


    /**
     * Uploads a Gravatar to the account. It does not set the current Gravatar to this image, use setGravatar to do that.
     *
     * InputSteam is closed after this method is called.
     * @param base64 base64 String of an image
     * @param rating Rating of the image. Rating can be one of the following: [G, PG, R, X]
     * @return Returns Gravatar on success
     * @throws XMLRPCException io exception
     */
    public Gravatar upload(String base64, Rating rating) throws XMLRPCException, IOException {
        try {
            parametersMap.put(BASE64, base64);
            return getUserImageBase64(rating);
        }catch (XMLRPCException e) {
            parametersMap.remove(BASE64);
            parametersMap.remove(RATING);
            throw e;
        }
    }

    /**
     * Uploads a Gravatar to the account. It does not set the current Gravatar to this image, use setGravatar to do that.
     *
     * InputSteam is closed after this method is called.
     * @param imageUrl URL of an image
     * @param rating Rating of the image. Rating can be one of the following: [G, PG, R, X]
     * @return Returns Gravatar on success
     * @throws XMLRPCException io exception
     */
    public Gravatar upload(URL imageUrl, Rating rating) throws XMLRPCException, IOException {
        try {
            parametersMap.put(IMAGE_URL, imageUrl.toString());
            parametersMap.put(RATING, rating.getNumberValue());
            String userImageId = (String) client.call("grav.saveUrl", parametersMap);
            parametersMap.remove(IMAGE_URL);
            parametersMap.remove(RATING);
            if(userImageId.equals("0")) {
                throw new IOException("Image failed to upload");
            }
            return new Gravatar(userImageId, rating);
        } catch (XMLRPCException e) {
            parametersMap.remove(IMAGE_URL);
            parametersMap.remove(RATING);
            throw e;
        }
    }

    /**
     * Sets a Gravatar as the active gravatar for one of more addresses on this account
     * @param gravatar gravatar
     * @param emails email addresses
     * @return
     * Returns a map of email and a boolean. True if successful, False if failure
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> setGravatar(Gravatar gravatar, String... emails) throws XMLRPCException {
        parametersMap.put(USER_IMAGE, gravatar.getId());
        parametersMap.put(ADDRESSES, new ArrayList<>(Arrays.asList(emails)));
        Map<String, Boolean> response = (Map<String, Boolean>) client.call("grav.useUserimage", parametersMap);
        parametersMap.remove(USER_IMAGE);
        parametersMap.remove(ADDRESSES);
        return response;
    }

    /**
     * Sets a Gravatar as the active gravatar
     * @param gravatar gravatar
     * @return True if successful, False if failure
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Boolean setGravatar(Gravatar gravatar) throws XMLRPCException {
        parametersMap.put(USER_IMAGE, gravatar.getId());
        parametersMap.put(ADDRESSES, new ArrayList<>(Collections.singletonList(email)));
        var response = (Map<String, Boolean>) client.call("grav.useUserimage", parametersMap);
        parametersMap.remove(USER_IMAGE);
        parametersMap.remove(ADDRESSES);
        return response.values().iterator().next();
    }


    /**
     * Removes the active Gravatar for the account, and a default image will be used instead.
     * @param emails email addresses
     * @return Map of emails and booleans determining if it was successful or not
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> removeActiveGravatar(String... emails) throws XMLRPCException {
        parametersMap.put(ADDRESSES, new ArrayList<>(Arrays.asList(emails)));
        Map<String, Boolean> response = (Map<String, Boolean>) client.call("grav.removeImage", parametersMap);
        parametersMap.remove(ADDRESSES);
        return response;
    }


    /**
     * Deletes a Gravatar from an account
     * @param gravatar gravatar
     * @return Boolean true if user image was deleted, false otherwise.
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public Boolean deleteGravatar(Gravatar gravatar) throws XMLRPCException {
        parametersMap.put(USER_IMAGE, gravatar.getId());
        Boolean response = (Boolean) client.call("grav.deleteUserimage", parametersMap);
        parametersMap.remove(USER_IMAGE);
        return response;
    }
}
