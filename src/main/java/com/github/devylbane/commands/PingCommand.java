package com.github.devylbane.commands;

import com.github.devylbane.Config;
import com.github.devylbane.commands.struct.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class PingCommand extends Command
{

    @Override
    public void executeCommand(@Nonnull GuildMessageReceivedEvent event, @NotNull String invoke, @NotNull String[] args) {
        reply(event, String.format("My latency is %dms.", event.getJDA().getPing()));
    }

    @NotNull
    @Override
    public String getHelp() {
        return "Displays the websocket ping!";
    }

    @NotNull
    @Override
    public String getSyntax() {
        return String.format("%sping", Config.PREFIX);
    }

    @NotNull
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @NotNull
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
