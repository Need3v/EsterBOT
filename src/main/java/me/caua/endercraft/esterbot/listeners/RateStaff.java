package me.caua.endercraft.esterbot.listeners;

import me.caua.endercraft.esterbot.EsterBOT;
import me.caua.endercraft.esterbot.tickets.TicketSetup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateStaff extends ListenerAdapter {
    private HashMap<Member, Long> cooldown = new HashMap<>();
    public void onModalInteraction(ModalInteractionEvent e){
        if (!e.getModalId().equals("ratestaffmodal")) return;
        int nota;
        String comentario;
        try {
            nota = Integer.parseInt(e.getValue("Nota").getAsString());
        } catch (NumberFormatException ex) {
            e.reply("A nota digitada não é um valor válido").setEphemeral(true).queue();
            return;
        }
        if (nota < 1 || nota > 5) {
            e.reply("A nota digitada não é um valor válido").setEphemeral(true).queue();
            return;
        }
        comentario = e.getValue("Comentario").getAsString();
        if (cooldown.containsKey(e.getMember()) && !(System.currentTimeMillis() >= cooldown.get(e.getMember()))) {
            e.reply("❌ Você deve aguardar mais " +converter(e.getMember())+ " minutos para enviar uma nova avaliação.\n\n**Atenção!** Não envie revisões repetidas (da mesma punição), sujeito a banimento do discord.").setEphemeral(true).queue();
            return;
        } else cooldown.remove(e.getMember());
        cooldown.put(e.getMember(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));

        e.getChannel().sendMessageEmbeds(buildRateEmbed(e.getMember(), e.getValue("Nick").getAsString(), e.getValue("Staff").getAsString(), nota, comentario)).addActionRow(
                Button.primary("avaliarstaff", Emoji.fromUnicode("⭐")).withLabel("Avaliar um staff")).queue();
        e.reply("Sua avaliação foi enviada com sucesso! Obrigado 💜").setEphemeral(true).queue();
    }

    private Long converter(Member m) {
        long tempo = System.currentTimeMillis() - cooldown.get(m);
        return TimeUnit.MILLISECONDS.toMinutes(tempo) * -1;
    }

    public MessageEmbed buildRateEmbed(Member member, String nick, String staff, int nota, String comentario) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("<a:yayy:876537117524762694> **Nova avaliação!**");
        eb.setThumbnail("https://i.imgur.com/7MWP40P.png");
        //eb.setImage("https://api.loohpjames.com/serverbanner.png?ip=endercraft.com.br&width=1224");
        eb.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
        eb.setTimestamp(Instant.now());
        eb.setDescription("A sua avaliação nos ajuda a melhorar! <:pinkbooster:1113211062179856486>\nPara avaliar um staff clique no botão abaixo! :point_down:\n");

        StringBuilder notastars = new StringBuilder();

        for (int i = 0; i < nota; i++) {
            notastars.append("⭐");
        }
        notastars.append(" _(").append(nota).append(" de 5)_");

        eb.addField("**Enviada por:**", nick + " ("+member.getAsMention()+")", false);
        eb.addField("**Staff avaliado:**", staff, false);
        eb.addField("**Nota:**", notastars.toString(), false);
        if (!Objects.equals(comentario, "")) {
            eb.addField("**Comentário:**", "```"+comentario+"```", true);
        }

        if (nota < 3) {
            eb.setColor(Color.RED);
        } else if (nota == 3) {
            eb.setColor(Color.YELLOW);
        } else {
            eb.setColor(Color.GREEN);
        }

        return eb.build();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        TextChannel textChannel = e.getChannel().asTextChannel();
        if (e.getComponentId().equals("avaliarstaff")) {

            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput denunciado = TextInput.create("Staff", "Staff que deseja avaliar", TextInputStyle.SHORT)
                    .setPlaceholder("Nome do staff que deseja avaliar")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput nota = TextInput.create("Nota", "Nota de 1 a 5", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Número de 1 a 5")
                    .setMinLength(1)
                    .setMaxLength(1)
                    .build();

            TextInput body = TextInput.create("Comentario", "Comentário", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Se desejar, deixe um comentário na avaliação")
                    .setMaxLength(100)
                    .build();

            Modal modal = Modal.create("ratestaffmodal", "Avaliar um staff")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(denunciado), ActionRow.of(nota), ActionRow.of(body))
                    .build();

            e.replyModal(modal).queue();

            return;
        }
    }

}
