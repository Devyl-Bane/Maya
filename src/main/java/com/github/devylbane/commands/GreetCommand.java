package com.github.devylbane.commands;

import com.github.devylbane.Config;
import com.github.devylbane.commands.struct.Command;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.List;

public class GreetCommand extends Command
{
    @Override
    public void executeCommand(@Nonnull GuildMessageReceivedEvent event, @Nonnull String invoke, @Nonnull String[] args)
    {
        if (event.getMessage().getMentionedMembers().isEmpty())
        {
            reply(event, "You need to mention a user to greet!");
        }
        else
        {
            List<User> mentionedUser = event.getMessage().getMentionedUsers();
            reply(event, String.format("Greetings %s, how are you?", mentionedUser.get(0)));
        }
    }

    @Nonnull
    @Override
    public String getHelp()
    {
        return "Greets the mentioned user!";
    }

    @Nonnull
    @Override
    public String getSyntax()
    {
        return String.format("%sgreet @user", Config.PREFIX);
    }

    @Nonnull
    @Override
    public String[] getAliases()
    {
        return new String[0];
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "greet";
    }

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public int getMaximumArguments()
    {
        return 1;
    }
}
