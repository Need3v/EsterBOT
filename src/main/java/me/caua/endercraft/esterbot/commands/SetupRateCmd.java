package me.caua.endercraft.esterbot.commands;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class SetupRateCmd extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (Objects.requireNonNull(e.getMessage().getAuthor()).isBot()) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(Objects.requireNonNull(EsterBOT.dotenv.get("CARGO_EQUIPE_ID"))))) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "setuprate")) {
            e.getMessage().getChannel().sendMessageEmbeds(sendRateEmbed().build()).addActionRow(
                    Button.primary("avaliarstaff", Emoji.fromUnicode("⭐")).withLabel("Avaliar um staff")).queue();
            e.getMessage().delete().queue();
        }

    }

    public static EmbedBuilder sendRateEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("⭐ **Avalie um staff**");
        eb.setThumbnail("https://i.imgur.com/7MWP40P.png");
        //eb.setImage("https://api.loohpjames.com/serverbanner.png?ip=endercraft.com.br&width=1224");
        eb.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
        eb.setDescription("A sua avaliação nos ajuda a melhorar! <:pinkbooster:1113211062179856486>\nPara avaliar um staff clique no botão abaixo! :point_down:\n");
        eb.setColor(Color.ORANGE);
        return eb;
    }

}
