package com.github.hayesroach.gravatar;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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

    private static final String PASSWORD_LABEL = "password";

    private final XMLRPCClient client;
    private final Authentication authentication;

    public GravatarClient(Authentication authentication) throws MalformedURLException {
        String emailHash = md5Hex(authentication.getEmail());
        this.client = new XMLRPCClient(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));
        this.authentication = authentication;
    }

    /**
     * Description: Checks whether a hash has a gravatar
     * @param hashes
     * List of hashes
     * @return
     * Returns a map of hashes to booleans
     */
    public Map<String, Boolean> gravExists(List<String> hashes) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("hashes", hashes);
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
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
     */
    public Map<String, Address> gravAddresses() throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        return (HashMap<String, Address>) client.call("grav.addresses", parameters);
    }

    /**
     * Description: Returns an map of user images for this account and associated metadata array (image rating [0], image url [1])
     */
    public Map<String, Object[]> gravUserImages() throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        return (HashMap<String, Object[]>) client.call("grav.userimages", parameters);
    }


    /**
     * Description: Saves binary image data as a userimage for this account.
     * It does not set the img as the users active Gravatar. Use the
     * gravUseUserimage() function to do that.
     * @param base64
     * This is the base64 String for an image
     * @param rating
     * This is the rating for an image. Ratings to choose from -> 0:G, 1:PG, 2:R, 3:X
     * @return
     * Returns 0 on failure, and userimage on success
     */
    public String gravSaveData(String base64, int rating) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
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
     * This is the rating for an image. Ratings to choose from -> 0:G, 1:PG, 2:R, 3:X
     * @return
     * Returns 0 on failure, and userimage on success
     */
    public String gravSaveUrl(String url, int rating) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        parameters.put("url", url); // url of image
        parameters.put("rating", rating); // 0, 1, 2, or 3
        return (String) client.call("grav.saveUrl", parameters);
    }

    /**
     * Description: Sets a userimage as the current gravatar for one of more addresses on this account
     * @param userImage
     * This is the userImage for the account
     * @return
     * Returns a map of email and a boolean. True if successful, False if failure
     */
    public Map<String, Boolean> gravUseUserImage(String userImage, String... emails) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        parameters.put("userimage", userImage);
        parameters.put("addresses", new ArrayList<>(Arrays.asList(emails)));
        return (Map<String, Boolean>) client.call("grav.useUserimage", parameters);
    }


    /**
     * Description: Sets Gravatar to default Gravatar
     */
    public Map<String, Boolean> gravRemoveImage(String... emails) throws XMLRPCException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        parameters.put("addresses", new ArrayList<>(Arrays.asList(emails)));
        return (Map<String, Boolean>) client.call("grav.removeImage", parameters);
    }


    /**
     * Description: Deletes the userimage associated with any email addresses on the account
     */
    public Boolean gravDeleteUserImage(String userImage) throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        parameters.put("userimage", userImage);
        return (Boolean) client.call("grav.deleteUserimage", parameters);
    }

    /**
     * Description: Removes a userimage from the account and any email addresses with which it is associated
     */
    public Map<String, Object> gravTest() throws XMLRPCException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PASSWORD_LABEL, authentication.getPassword());
        return (Map<String, Object>) client.call("grav.test", parameters);
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
