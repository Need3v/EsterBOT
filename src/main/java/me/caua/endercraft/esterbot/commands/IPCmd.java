package me.caua.endercraft.esterbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static me.caua.endercraft.esterbot.EsterBOT.jda;
import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class IPCmd extends ListenerAdapter {



    public IPCmd() {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equals("ip")) {
            e.replyEmbeds(sendIPEmbed().build()).queue();
        }
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "ip")) {
            e.getMessage().replyEmbeds(sendIPEmbed().build()).queue();
        }

    }

    public EmbedBuilder sendIPEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("<a:jumpingblock:876537116304236605> Informações do servidor");
        //eb.setThumbnail("https://i.imgur.com/7MWP40P.png");
        eb.setImage("https://api.loohpjames.com/serverbanner.png?ip=endercraft.com.br&width=1224");
        eb.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
        eb.setDescription("Confira algumas informações do servidor:");
        eb.addField("<:minecraft_beacon:1104573758213193808> **IP Minecraft Java Edition**", "```endercraft.com.br```", false);
        eb.addField("<:minecraft_world:1104573698184314900> **IP Minecraft Bedrock Edition**", "```bed.endercraft.com.br``` *Porta: 19132*", false);
        eb.addField("**Links**", "<:discord:1104575084401807531> Discord: https://ecbr.me/disc\n <:goldingot:1104805184921681941> Site oficial: https://ecbr.me/site", false);
        eb.setColor(Color.GREEN);
        return eb;
    }

}
