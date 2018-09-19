/*
Created 9/18/2018.
 */

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import javax.security.auth.login.LoginException;

public class Main
{
    public static void main(String[] args)
    {
       try
       {
           JDA jda = new JDABuilder(AccountType.BOT) //Creates a new bot builder.
                   .setToken(Constants.botToken) //Sets the token.
                   .setGame(Game.of(Game.GameType.DEFAULT, "with Bonnie?")) //Sets our playing message.
                   .addEventListener(new Handler()) //Adds an event listener for the Handler class.
                   .build().awaitReady(); //Builds the bot and turns it online?

           //Should let us know everything worked and she is online.
           System.out.println("Logged in as " + jda.getSelfUser().getName() + "#"
                   + jda.getSelfUser().getDiscriminator() + "!");
       }
       catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
