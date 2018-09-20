package com.github.devylbane.commands;

import com.github.devylbane.Config;
import com.github.devylbane.commands.struct.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;

public class PingCommand extends Command
{

    @Override
    public void executeCommand(@Nonnull GuildMessageReceivedEvent event, @Nonnull String invoke, @Nonnull String[] args) {
        reply(event, String.format("My latency is %dms.", event.getJDA().getPing()));
    }

    @Nonnull
    @Override
    public String getHelp() {
        return "Displays the websocket ping!";
    }

    @Nonnull
    @Override
    public String getSyntax() {
        return String.format("%sping", Config.PREFIX);
    }

    @Nonnull
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Nonnull
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public int getRequiredArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 0;
    }
}
