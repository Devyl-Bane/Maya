package com.github.devylbane.commands.struct;

import com.github.devylbane.Emojis;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
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
     * @see Command#reply(GuildMessageReceivedEvent, String, String, long)
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
     * @see Command#reply(GuildMessageReceivedEvent, String, String, long)
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
     * @param reaction the reaction we want to add. {@code null} prohibits reacting to the received message
     * @param messages  the messages we want to reply. {@code null} prevents us from sending a reply.
     * @param millis   the time we wait before we delete the sent message. {@code -1L} avoids deleting our reply.
     */
    protected void reply(final GuildMessageReceivedEvent event, final String reaction, final long millis, final Message... messages)
    {
        if (event == null)
            return;

        if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), SEND_PERMISSIONS))
            return;

        if (reaction != null && !reaction.isEmpty() && !Helpers.isBlank(reaction))
            event.getMessage().addReaction(reaction).queue(null, FAIL_CONSUMER);

        if (messages == null || messages.length == 0)
            return;

        if (millis == -1)
            for (Message msg : messages) {
                event.getChannel().sendMessage(msg).queue();
            }
        else
            for (Message message : messages) {
                event.getChannel().sendMessage(message).queue((sentMessage) ->
                        sentMessage.delete().queueAfter(millis, TimeUnit.MILLISECONDS), FAIL_CONSUMER);
            }
    }

    public void reply(final GuildMessageReceivedEvent event, final String reaction, final String message, final long millis)
    {
        reply(event, reaction, millis, new MessageBuilder(message).build());
    }

    protected void reply(final GuildMessageReceivedEvent event, final String message, final long millis)
    {
        reply(event, null, message, millis);
    }

    protected void reply(final GuildMessageReceivedEvent event, final Message... message)
    {
        reply(event, null, -1L, message);
    }

    protected void reply(final GuildMessageReceivedEvent event, final String message)
    {
        reply(event, null, message, -1L);
    }
}
