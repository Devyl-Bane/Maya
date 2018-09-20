package com.github.devylbane.commands.handlers;

import com.github.devylbane.Config;
import com.github.devylbane.commands.PingCommand;
import com.github.devylbane.commands.struct.Command;
import com.github.devylbane.commands.struct.ICommand;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {
    private List<ICommand> commands = new ArrayList<ICommand>()
    {
        @Override
        public boolean add(ICommand o)
        {
            if (this.contains(o))
                return false;
            return super.add(o);
        }

        // Advanced contains check so we have no duplicated names nor aliases
        public boolean contains(ICommand o) {
            List<String> names = this.stream().map(ICommand::getName).collect(Collectors.toList());
            List<String[]> aliases = this.stream().map(ICommand::getAliases).collect(Collectors.toList());

            if (names.contains(o.getName()))
                return true;

            for (String[] alias : aliases) {
                if (arrayContainsElement(alias, o.getName()))
                    return true;
                for (String s : o.getAliases()) {
                    if (arrayContainsElement(alias, s))
                        return true;
                    if (names.contains(s))
                        return true;
                }
            }

            return super.contains(o);
        }
    };

    public CommandManager() {
        commands.add(new PingCommand());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        String raw = event.getMessage().getContentRaw();

        if (!raw.startsWith(Config.PREFIX))
            return;

        // Strip prefix
        raw = raw.replaceFirst(Pattern.quote(Config.PREFIX), "");

        String[] split = raw.split("\\s+", 2);

        String invoke = split[0];

        ICommand icmd = findCommand(invoke);

        if (icmd == null)
            return;

        Command cmd = (Command) icmd;

        if (cmd.getRequiredArguments() > 0 && split.length == 1)
        {
            cmd.replyFailure(event, String.format("Missing arguments! Required syntax %s requires at least %d arguments and max. %d arguments.",
                    cmd.getSyntax(), cmd.getRequiredArguments(), cmd.getMaximumArguments()), 15000L);
            return;
        }
        else if (cmd.getRequiredArguments() == 0 && split.length == 1)
        {
            cmd.executeCommand(event, invoke, new String[0]);
            return;
        }

        String rawArgs = split[1];

        cmd.executeCommand(event, invoke, Arrays.copyOfRange(rawArgs.split("\\s+"), 0, cmd.getMaximumArguments() - 1));

    }

    @Override
    public void onReady(ReadyEvent event)
    {
        System.out.printf("Logged in as %#s!%n%d guilds are available and %d guilds are unavailable.",
                event.getJDA().getSelfUser(), event.getGuildAvailableCount(), event.getGuildUnavailableCount());
    }

    private ICommand findCommand(String invoke)
    {
        Optional<ICommand> icmd = commands.stream().filter((cmd) -> cmd.getName().equalsIgnoreCase(invoke)).findFirst();

        if (icmd.isPresent())
            return icmd.get();

        icmd = commands.stream().filter((cmd) -> arrayContainsElement(cmd.getAliases(), invoke)).findFirst();

        return icmd.orElse(null);

    }

    private boolean arrayContainsElement(String[] array, String element)
    {
        for (String s : array) {
            if (s.equalsIgnoreCase(element))
                return true;
        }
        return false;
    }
}
