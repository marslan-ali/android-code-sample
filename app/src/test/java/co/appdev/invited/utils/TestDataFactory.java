package co.appdev.invited.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ahsan on 5/8/17.
 */

public class TestDataFactory {

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static Users makeUser(String uniqueSuffix) {
        return makeProfile(uniqueSuffix);
    }

    public static List<Users> makeListUsers(int number) {
        List<Users> ribots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ribots.add(makeUser(String.valueOf(i)));
        }
        return ribots;
    }

    public static Users makeProfile(String uniqueSuffix) {

        Users user = new Users();
        user.setName(makeName(uniqueSuffix));
        user.setEmail("email" + uniqueSuffix + "@nxb.com.pk");
        user.setPhone("03434410112");
        user.setAge(25);
        user.setPicture("http://api.ribot.io/images/" + uniqueSuffix);
        user.setGender("Male");
        user.setId(randomUuid());

        return user;
    }

    public static Name makeName(String uniqueSuffix) {
        Name name = new Name();
        name.setFirst("Name-" + uniqueSuffix);
        name.setLast("Surname-" + uniqueSuffix);
        return name;
    }

}
