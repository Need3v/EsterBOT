package me.caua.endercraft.esterbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static me.caua.endercraft.esterbot.EsterBOT.*;

public class PingCmd extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "ping")) {
            e.getChannel().sendMessage("O meu ping é de **" + jda.getGatewayPing() + "ms**.").queue();
        }

    }

}
