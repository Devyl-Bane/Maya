package com.github.devylbane;

/*
 * Created 9/18/2018.
 */

import com.github.devylbane.commands.handlers.CommandManager;
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
                    .setToken(Config.TOKEN) //Sets the token.
                    .setGame(Game.playing( "with Bonnie?")) //Sets our playing message.
                    .addEventListener(new Handler(), new CommandManager()) //Adds her event listeners
                    .build(); //Builds the bot and turns it online?
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
    }
}
