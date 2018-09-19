import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class Handler extends ListenerAdapter
{
    //Will only listen to messages from the server(guild)?
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        //Ignored input from bots.
        if(event.getAuthor().isBot())
        {
            return;
        }
        //Binds the input to the local variable "message".
        String message = event.getMessage().getContentRaw().toLowerCase();
        //We do this so we don't have to access the channel each time via event.getChannel().
        TextChannel channel = event.getChannel();

        if(message.equals("+join"))  //Checks if the command is +join.
        {
            if(!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) //If the bot does not have permission to join a voice channel.
            {
                channel.sendMessage("ERR 01: Missing `Connect` permission.").queue();
                return;
            }

            VoiceChannel connectedChannel = event.getMember().getVoiceState().getChannel(); //Sets the channel the user is in as the variable "connectedChannel".

            if(connectedChannel == null) //If the user is not connected to a voice channel.
            {
                channel.sendMessage("ERR 02: You are not connected to a voice channel.").queue();
                return;
            }

            AudioManager audioManager = event.getGuild().getAudioManager(); //Sets.. something to the variable "audioManager".

            if(audioManager.isAttemptingToConnect()) //If someone is spamming the +join command?
            {
                channel.sendMessage("ERR 03: Already attempting to connect.").queue();
                return;
            }

            //Connects to the voice channel.
            audioManager.openAudioConnection(connectedChannel);
            channel.sendMessage("Connected to the voice channel.").queue();
        }
        else if(message.equals("+leave")) //Checks if the command is +leave.
        {
            //Gets the channel in which the bot is currently connected.
            VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

            if(connectedChannel == null) //If Maya is not connected to a voice channel.
            {
                channel.sendMessage("ERR 04: Not connected to a voice channel.").queue();
                return;
            }

            //Disconnects from the voice channel.
            event.getGuild().getAudioManager().closeAudioConnection();
            channel.sendMessage("Disconnected from the voice channel.").queue();
        }
    }
}
