package com.github.hayesroach.gravatar;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/*
 * Implements the Gravatar XML-RPC API defined here: https://en.gravatar.com/site/implement/xmlrpc/
 =========================================================
 * Copyright 2021 Hayes Roach (https://github.com/hayes-roach)
 * Licensed under MIT (https://github.com/hayes-roach/GravatarXmlRpc/LICENSE)
 =========================================================
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

public class GravatarClient {

    private static final String PASSWORD = "password";

    private final XMLRPCClient client;
    private final String email;
    private final String password;
    private final ConcurrentHashMap<String, String> parametersMap;



    public GravatarClient(String email, String password) throws MalformedURLException {
        String emailHash = md5Hex(email);
        this.client = new XMLRPCClient(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));
        this.email = email;
        this.password = password;
        this.parametersMap = new ConcurrentHashMap<>(1);
        parametersMap.put(PASSWORD, password);
    }

    /**
     * Description: Checks whether email hashes have a gravatar
     * @param hashes
     * List of hashes
     * @return
     * Returns a map of hashes to booleans
     * @throws XMLRPCException io exception
     */
    public Map<String, Boolean> gravatarExists(List<String> hashes) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("hashes", hashes);
        parameters.put(PASSWORD, password);
        Map<String, Integer> map = (Map<String, Integer>) client.call("grav.exists", parameters);
        return map.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().equals(0) ? Boolean.FALSE : Boolean.TRUE)
        );
    }

    /**
     * Description: Returns a map of email addresses for this account and associated metadata (image rating, user image, image url)
     * @return
     * Returns a map of emails and Address objects
     * @throws XMLRPCException io exception
     */
    public Map<String, Address> getAddresses() throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        return (HashMap<String, Address>) client.call("grav.addresses", parameters);
    }

    /**
     * Description: Returns an map of user images for this account and associated metadata array (image rating [0], image url [1])
     * @return Map of user images
     * @throws XMLRPCException io exception
     */
    public Map<String, Object[]> getUserImages() throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        return (HashMap<String, Object[]>) client.call("grav.userimages", parameters);
    }

    /**
     * Description: Returns an map of user images for this account and associated metadata array (image rating [0], image url [1])
     * @return Map of user images
     * @throws XMLRPCException io exception
     */
    @SuppressWarnings("unchecked")
    public List<UserImage> test() throws XMLRPCException {
        var map = (HashMap<String, Object[]>) client.call("grav.userimages", parametersMap);
       // map.entrySet().stream().

        map.entrySet().stream().forEach(entry -> {
            System.out.println(Integer.parseInt(entry.getValue()[0].toString()));
            System.out.println((String) entry.getValue()[1]);
        });

        return map.entrySet().stream().map(entry -> new UserImage(entry.getKey(), Integer.parseInt(entry.getValue()[0].toString()), entry.getValue()[1].toString())).collect(Collectors.toList());
    }


    /**
     * Description: Saves binary image data as a userimage for this account.
     * It does not set the img as the users active Gravatar. Use the
     * gravUseUserimage() function to do that.
     * @param base64
     * This is the base64 String for an image
     * @param rating
     * This is the rating for an image. Ratings to choose from: 0:G, 1:PG, 2:R, 3:X
     * @return
     * Returns 0 on failure, and userimage on success
     * @throws XMLRPCException io exception
     */
    public String saveImageBase64(String base64, int rating) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        parameters.put("data", base64); // base64 img String
        parameters.put("rating", rating); // 0, 1, 2, or 3
        return (String) client.call("grav.saveData", parameters);
    }

    /**
     * Description: Reads an image via its URL and saves that as a userimage for this account.
     * It does not set the img as the users active Gravatar. Use the
     * gravUseUserimage() function to do that.
     * @param url
     * This is the URL string for an image
     * @param rating
     * This is the rating for an image. Ratings to choose from: 0:G, 1:PG, 2:R, 3:X
     * @return
     * Returns 0 on failure, and userimage on success
     * @throws XMLRPCException io exception
     */
    public String saveImageUrl(String url, int rating) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        parameters.put("url", url); // url of image
        parameters.put("rating", rating); // 0, 1, 2, or 3
        return (String) client.call("grav.saveUrl", parameters);
    }

    /**
     * Description: Sets a userimage as the current gravatar for one of more addresses on this account
     * @param userImage user image
     * @param emails email addresses
     * This is the userImage for the account
     * @return
     * Returns a map of email and a boolean. True if successful, False if failure
     * @throws XMLRPCException io exception
     */
    public Map<String, Boolean> setUserImage(String userImage, String... emails) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        parameters.put("userimage", userImage);
        parameters.put("addresses", new ArrayList<>(Arrays.asList(emails)));
        return (Map<String, Boolean>) client.call("grav.useUserimage", parameters);
    }


    /**
     * Description: Sets Gravatar to default Gravatar image
     * @param emails email addresses
     * @return Map of emails and booleans
     * @throws XMLRPCException io exception
     */
    public Map<String, Boolean> setCurrentGravatarToDefault(String... emails) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        parameters.put("addresses", new ArrayList<>(Arrays.asList(emails)));
        return (Map<String, Boolean>) client.call("grav.removeImage", parameters);
    }


    /**
     * Description: Deletes the userimage associated with any email addresses on the account
     * @param userImage userImage
     * @return Boolean true if user image was deleted, false otherwise.
     * @throws XMLRPCException io exception
     */
    public Boolean deleteUserImage(String userImage) throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD, password);
        parameters.put("userimage", userImage);
        return (Boolean) client.call("grav.deleteUserimage", parameters);
    }

    public Map<String, String> getEmailHashes(String... emails) {
        Map<String, String> parameters = new HashMap<>();

        for(String email : emails) {
            parameters.put(email, md5Hex(email));
        }
        return parameters;
    }


    /**
     * These bottom two functions are used hash the email address
     */

    private String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        return null;
    }

}
