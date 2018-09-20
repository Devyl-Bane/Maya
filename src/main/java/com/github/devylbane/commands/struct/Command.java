package com.github.devylbane.commands.struct;

import com.github.devylbane.Emojis;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.ErrorResponse;
import net.dv8tion.jda.core.utils.Helpers;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class Command implements ICommand
{

    // Our default permissions we require for our replying methods
    protected final EnumSet<Permission> SEND_PERMISSIONS =
            EnumSet.of(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION);

    // Our default fail consumer for adding reactions or deleting a message
    private final Consumer<Throwable> FAIL_CONSUMER = (throwable ->
    {
        if (!(throwable instanceof ErrorResponseException))
        {
            throwable.printStackTrace();
            return;
        }
        ErrorResponseException ex = (ErrorResponseException) throwable;
        if (ex.getErrorResponse() != ErrorResponse.UNKNOWN_MESSAGE)
            ex.printStackTrace();
        // We ignore unknown messages because it can happen that the commands message is cleared
        // before we can delete or add reactions to it.
    });

    /**
     * The method that we use in our commands for replying success if we want to reply success.
     * @param event   the event that triggered the command execution.
     * @param message the message that we want to reply, we parse {@code null} if we do not want to reply a message.
     * @param millis  the time we wait before we delete our replied message, we parse {@code -1L} if we do not want to delete it.
     */
    public void replySuccess(final GuildMessageReceivedEvent event, final String message, final long millis)
    {
        reply(event, Emojis.CHECK_MARK.getUnicode(), message, millis);
    }

    protected void replySuccess(GuildMessageReceivedEvent event, String message)
    {
        replySuccess(event, message, -1L);
    }

    protected void replySuccess(GuildMessageReceivedEvent event)
    {
        replySuccess(event, null, -1L);
    }

    /**
     * The method that we use in our commands for replying success if we want to reply success.
     * @param event   the event that triggered the command execution.
     * @param message the message that we want to reply, we parse {@code null} if we do not want to reply a message.
     * @param millis  the time we wait before we delete our replied message, we parse {@code -1L} if we do not want to delete it.
     */
    public void replyFailure(final GuildMessageReceivedEvent event, final String message, final long millis)
    {
        reply(event, Emojis.RED_CROSS_MARK.getUnicode(), message, millis);
    }

    protected void replyFailure(GuildMessageReceivedEvent event, String message)
    {
        replyFailure(event, message, -1L);
    }

    protected void replyFailure(GuildMessageReceivedEvent event)
    {
        replyFailure(event, null, -1L);
    }


    /**
     * The universal method we use for replying.
     * @param event    the event that triggered command execution.
     * @param reaction the reaction we want to add.
     * @param message  the message we want to reply.
     * @param millis   the time we wait before we delete the sent message.
     */
    public void reply(final GuildMessageReceivedEvent event, final String reaction, final String message, final long millis)
    {
        if (event == null)
            return;

        if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), SEND_PERMISSIONS))
            return;

        if (reaction != null && !reaction.isEmpty() && !Helpers.isBlank(reaction))
            event.getMessage().addReaction(reaction).queue(null, FAIL_CONSUMER);

        if (message == null || message.isEmpty() || Helpers.isBlank(message))
            return;

        if (millis == -1)
            event.getChannel().sendMessage(message).queue();
        else
            event.getChannel().sendMessage(message).queue((sentMessage) ->
            {
                sentMessage.delete().queueAfter(millis, TimeUnit.MILLISECONDS);
            }, FAIL_CONSUMER);
    }

    protected void reply(final GuildMessageReceivedEvent event, final String message, final long millis)
    {
        reply(event, null, message, millis);
    }

    protected void reply(final GuildMessageReceivedEvent event, final String message)
    {
        reply(event, null, message, -1L);
    }
}
