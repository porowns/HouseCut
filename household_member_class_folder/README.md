# Householdmember & Household class
Java classes that implement basic user functionality (like register & login) as well as data storage.

How to compile: -> javac household_member_class.java

How to run: ->     java household_member_class 

How to run test: -> On Android Studio as a main activity (unfin)

The classes are meant to be implemented by the overlying login/user interface XML, and will send POST requests to the server for information and to create (register) new user accounts, as well as login, delete accounts, change passwords, join households and leave households.

The Household class is simply a vector of householdmember objects, and also has functions to perform on the household.

To implement the functions of the household member class, you will instantiate an instance of it and pass in the parameters (usually password, email, name, or sometimes token)

# Example

household_member_class Adam = new household_member_class();

Adam.register(password, name, email);
