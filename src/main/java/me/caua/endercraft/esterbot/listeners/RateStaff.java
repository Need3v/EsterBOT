package me.caua.endercraft.esterbot.listeners;

import me.caua.endercraft.esterbot.EsterBOT;
import me.caua.endercraft.esterbot.tickets.TicketSetup;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateStaff extends ListenerAdapter {

    public void onModalInteraction(ModalInteractionEvent e){
        if (!e.getModalId().equals("ratestaffmodal")) return;
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
