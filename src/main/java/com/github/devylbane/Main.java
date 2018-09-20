package com.github.devylbane;

/*
 * Created 9/18/2018.
 */

import com.github.devylbane.commands.handlers.CommandManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;

import javax.security.auth.login.LoginException;

public class Main
{

    public static void main(String[] args)
    {
       try
       {
           Config conf = new Config();

           JDA jda = new JDABuilder(AccountType.BOT) //Creates a new bot builder.
                   .setToken(conf.TOKEN) //Sets the token.
                   .setGame(Game.playing( "with Bonnie?")) //Sets our playing message.
                   .addEventListener(new Handler(), new CommandManager()) //Adds an event listener for the com.github.devylbane.Handler class.
                   .build().awaitReady(); //Builds the bot and turns it online?

           //Should let us know everything worked and she is online.
           /**
            * moved to {@link CommandManager#onReady(ReadyEvent)}
             */

       }
       catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
