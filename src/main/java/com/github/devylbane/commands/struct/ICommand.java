package com.github.devylbane.commands.struct;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface ICommand
{

    /**
     * The method every command has to implement.
     * @param event the event the command should use.
     * @param args  the args that we parsed for easier execution.
     */
    void executeCommand(@Nonnull GuildMessageReceivedEvent event, @Nonnull String invoke, @Nonnull String[] args);

    /**
     *
     * @return the detailed help and description of the command.
     */
    @Nonnull
    String getHelp();

    /**
     * @return an example people can use for executing the command.
     */
    @Nonnull
    String getSyntax();

    /**
     *
     * @return an array of strings with all possible aliases
     */
    @Nonnull
    String[] getAliases();

    /**
     *
     * @return the commands name
     */
    @Nonnull
    String getName();

    /**
     *
     * @return the count of required args
     */
    @Nonnegative
    int getRequiredArguments();

    /**
     *
     * @return the count of max args
     */
    @Nonnegative
    int getMaximumArguments();
}
