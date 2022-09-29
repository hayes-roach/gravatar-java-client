package com.github.hayesroach.gravatar;

import de.timroes.axmlrpc.XMLRPCException;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException, XMLRPCException {

        GravatarClient client = new GravatarClient("hhroach@live.com", "greenorange2");

        System.out.println(client.test().toString());

    }
}
