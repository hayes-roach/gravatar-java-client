import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/*
 =========================================================
 * Copyright 2020 Hayes Roach (https://github.com/hayes-roach)
 * Licensed under MIT (https://github.com/hayes-roach/GravatarXmlRpc/LICENSE)
 =========================================================
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

public class GravatarXmlRpc {

    /**
     * Description: Checks whether a hash has a Gravatar
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravExists(String email, String password) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        List<String> hashes = new ArrayList<>();
        hashes.add("example_hash");
        hashes.add("example_hash2");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("hashes", hashes);

        List<Map<String, Object>> data = new ArrayList<>();

        data.add(parameters);

        try {
            HashMap result = (HashMap) client.execute("grav.exists", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Gets a list of addresses for this account
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravAddresses(String email, String password) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);

        try {
            HashMap result = (HashMap) client.execute("grav.addresses", Collections.singletonList(parameters));
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Returns an array of userimages for this account
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravUserImages(String email, String password) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);

        try {
            HashMap result = (HashMap) client.execute("grav.userimages", Collections.singletonList(parameters));
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Saves binary image data as a userimage for this account.
     * It does not set the img as the users active Gravatar. Use the
     * gravUseUserimage() function to do that.
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @param base64
     * This is the base64 String for an image
     * @param rating
     * This is the rating for an image. Ratings to choose from -> 0:g, 1:pg, 2:r, 3:x
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravSaveData(String email, String password, String base64, String rating) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("data", base64); // base64 img String
        parameters.put("rating", rating); // 0, 1, 2, or 3

        List<Map<String, String>> data = new ArrayList<>();

        data.add(parameters);

        try {
            String result = (String) client.execute("grav.saveData", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Reads an image via its URL and saves that as a userimage for this account.
     * It does not set the img as the users active Gravatar. Use the
     * gravUseUserimage() function to do that.
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @param url
     * This is the URL string for an image
     * @param rating
     * This is the rating for an image. Ratings to choose from -> 0:g, 1:pg, 2:r, 3:x
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravSaveUrl(String email, String password, String url, String rating) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("url", url); // url of image
        parameters.put("rating", rating); // 0, 1, 2, or 3
        parameters.put("password", password);

        List<Map<String, Object>> data = new ArrayList<>();

        data.add(parameters);

        try {
            String result = (String) client.execute("grav.saveUrl", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }



    /**
     * Description: Sets a userimage as the current gravatar for one of more addresses on this account
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @param userImage
     * This is the userImage for the account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravUseUserImage(String email, String password, String userImage) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        String emailHash = md5Hex(email);

        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("userimage", userImage);

        ArrayList<String> addresses = new ArrayList<>();
        addresses.add(email); // you can add more to the list if you have more addresses associated with the account.

        parameters.put("addresses", addresses);

        List<Map<String, Object>> data = new ArrayList<>();
        data.add(parameters);

        try {
            HashMap result = (HashMap) client.execute("grav.useUserimage", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Removes the userimage associated with one or more email addresses (Changes Gravatar to default Gravatar)
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravRemoveImage(String email, String password) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);

        ArrayList<String> addresses = new ArrayList<>();
        addresses.add(email); // if you have more than one email associated with this account that you want to remove the Gravatar for then you can add that as well

        parameters.put("addresses", addresses);

        List<Map<String, Object>> data = new ArrayList<>();
        data.add(parameters);

        try {
            HashMap result = (HashMap) client.execute("grav.removeImage", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Removes the userimage associated with one or more email addresses (Changes Gravatar to default Gravatar)
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravDeleteUserImage(String email, String password, String userImage) throws MalformedURLException {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash)); // replace with md5 email hash

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("userimage", userImage);

        List<Map<String, Object>> data = new ArrayList<>();
        data.add(parameters);

        try {
            Boolean result = (Boolean) client.execute("grav.deleteUserimage", data);
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: Removes a userimage from the account and any email addresses with which it is associated
     * @param email
     * Email is user email for Gravatar account
     * @param password
     * Password is user password for Gravatar account
     * @throws MalformedURLException
     * If Malformed URL then print stack trace
     */

    public void gravTest(String email, String password) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        String emailHash = md5Hex(email);
        config.setServerURL(new URL("https://secure.gravatar.com/xmlrpc?user=" + emailHash));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);

        try {
            HashMap result = (HashMap) client.execute("grav.test", Collections.singletonList(parameters));
            System.out.println(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
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
