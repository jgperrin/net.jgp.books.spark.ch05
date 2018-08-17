package net.jgp.books.sparkWithJava.ch05.lab900.simpleLambda;

import java.util.ArrayList;
import java.util.List;

public class SimpleLambdaApp {

  public static void main(String[] args) {
    List<String> frenchFirstNameList = new ArrayList<>();
    // a few French first name that can be composed with Jean
    frenchFirstNameList.add("Georges");
    frenchFirstNameList.add("Claude");
    frenchFirstNameList.add("Philippe");
    frenchFirstNameList.add("Pierre");
    frenchFirstNameList.add("François");
    frenchFirstNameList.add("Michel");
    frenchFirstNameList.add("Bernard");
    frenchFirstNameList.add("Guillaume");
    frenchFirstNameList.add("André");
    frenchFirstNameList.add("Christophe");
    frenchFirstNameList.add("Luc");
    frenchFirstNameList.add("Louis");

    frenchFirstNameList.forEach(
        name -> System.out.println(name + " and Jean-" + name
            + " are different French first names!"));

    System.out.println("-----");

    frenchFirstNameList.forEach(
        name -> {
          String message = name + " and Jean-";
          message += name;
          message += " are different French first names!";
          System.out.println(message);
        });
  }
}
