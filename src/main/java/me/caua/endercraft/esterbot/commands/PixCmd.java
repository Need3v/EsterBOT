package me.caua.endercraft.esterbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PixCmd extends ListenerAdapter {

    EmbedBuilder eb = new EmbedBuilder();

    public PixCmd() {
        eb.setTitle("<:pix:876537670556336199> Pagamento via PIX");
        eb.setThumbnail("https://i.imgur.com/7MWP40P.png");
        eb.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
        eb.setDescription("Após o pagamento envie o comprovante por aqui!");
        eb.addField("Chave E-mail:", "```admin@endercraft.com.br```", false);
        eb.setImage("https://i.imgur.com/mgmgi0q.png");
        eb.setColor(Color.GREEN);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equals("pix")) {
            e.reply("QR Code PIX enviado.").setEphemeral(true).queue();
            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

}
