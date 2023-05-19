package me.caua.endercraft.esterbot.commands;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class SayCmd extends ListenerAdapter {

    String text = "";

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (Objects.requireNonNull(e.getMessage().getAuthor()).isBot()) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_EQUIPE_ID")))) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "say")) {
            for (String arg : args) {
                if (!arg.equalsIgnoreCase(prefix + "say")) {
                    this.text = this.text + arg + " ";
                }
            }
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(text).queue();
            text = "";
        }
    }

}
