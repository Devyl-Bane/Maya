package com.github.devylbane.commands;

import com.github.devylbane.Config;
import com.github.devylbane.commands.handlers.CommandManager;
import com.github.devylbane.commands.struct.Command;
import com.github.devylbane.commands.struct.ICommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class HelpCommand extends Command
{
    @Override
    public void executeCommand(@Nonnull GuildMessageReceivedEvent event, @Nonnull String invoke, @Nonnull String[] args)
    {
        // This little trick returns the CommandManager from our JDA instance.
        Optional<CommandManager> opt = event.getJDA().getRegisteredListeners().stream().filter(CommandManager.class::isInstance).map(CommandManager.class::cast).findFirst();

        // Checks if we actually have found the manager, if not we return WHICH NEVER EVER should happen.
        if (!opt.isPresent())
            return;

        // Actually getting it.
        CommandManager commandManager = opt.get();

        if (args.length == 0)
        {
            generateHelp(commandManager, event);
            return;
        }

        ICommand cmd = commandManager.findCommand(args[1]);

        if (cmd == null)
            return;

        reply(event, formatCommand(cmd));
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument") // Suppressing a dumb warning. fuck off intellij
    private void generateHelp(CommandManager manager, GuildMessageReceivedEvent event)
    {
        String help = manager.getCommands().stream().map(this::formatCommand).collect(Collectors.joining("\n"));
        Queue<Message> msgs = new MessageBuilder(help).buildAll(MessageBuilder.SplitPolicy.NEWLINE);
        reply(event, msgs.toArray(new Message[msgs.size()]));
    }

    private String formatCommand(ICommand cmd)
    {
        return String.format("%s - %s%nUsage - %s", cmd.getName(), cmd.getHelp(), cmd.getSyntax());
    }

    @Nonnull
    @Override
    public String getHelp()
    {
        return "help";
    }

    @Nonnull
    @Override
    public String getSyntax()
    {
        return String.format("%shelp [command]", Config.PREFIX);
    }

    @Nonnull
    @Override
    public String[] getAliases()
    {
        return new String[]{"syntax"};
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "help";
    }

    @Override
    public int getMaximumArguments()
    {
        return 1;
    }
}
