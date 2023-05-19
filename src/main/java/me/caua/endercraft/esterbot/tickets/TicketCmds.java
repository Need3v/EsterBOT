package me.caua.endercraft.esterbot.tickets;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static me.caua.endercraft.esterbot.EsterBOT.dotenv;
import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class TicketCmds extends ListenerAdapter {

    private String cargoEquipeId = dotenv.get("CARGO_EQUIPE_ID");
    private ArrayList<Long> deleting = new ArrayList<>();

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (Objects.requireNonNull(e.getMessage().getAuthor()).isBot()) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(cargoEquipeId))) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "ticket")) {
            if (args[1].equalsIgnoreCase("setup")) {

                StringSelectMenu.Builder builder = StringSelectMenu.create("ticket");
                builder.addOption("Financeiro", "ticket:financeiro", "Para assuntos financeiros ou comprar com PayPal.", Emoji.fromUnicode("💵"));
                builder.addOption("Denúncias", "ticket:denuncia", "Denúncie jogadores que tenham violado nossas regras.", Emoji.fromUnicode("📢"));
                builder.addOption("Suporte Técnico", "ticket:tecnico", "Suporte para problemas técnicos e bugs.", Emoji.fromUnicode("🔧"));
                builder.addOption("Suporte Geral", "ticket:geral", "Suporte para qualquer tipo de problema, dúvida ou ajuda.", Emoji.fromUnicode("📩"));
                builder.addOption("Revisão de Punição", "ticket:revisao", "Acredita que foi punido injustamente? Faça uma revisão.", Emoji.fromUnicode("👮‍♂️"));
                builder.setPlaceholder("Escolha uma categoria");
                StringSelectMenu menu = builder.build();

                e.getMessage().getChannel().sendMessageEmbeds(TicketSetup.ticketEmbed.build()).setActionRow(menu).queue();
            } else if (args[1].equalsIgnoreCase("fechar")) {
                TextChannel textChannel = e.getMessage().getChannel().asTextChannel();
                if (textChannel.getName().contains("ticket│")) {
                    e.getMessage().delete().queue();
                    e.getChannel().sendMessageEmbeds(new EmbedBuilder()
                                    .setTitle("**Deseja encerrar o ticket?**")
                                    .setDescription("Reaja abaixo para continuar ou encerrar o ticket.")
                                    .setColor(Color.RED)
                                    .setFooter("Esta ação é irreversível!")
                            .build()).addActionRow(
                            Button.primary("ticketfecharok", Emoji.fromUnicode("✅")),
                            Button.secondary("ticketfecharcancel", Emoji.fromUnicode("❌")))
                            .queue();
                    System.out.println(textChannel.getMembers());
                } else {
                    e.getMessage().reply("Este canal não é um ticket!").queue();
                }
            }

        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        TextChannel textChannel = e.getChannel().asTextChannel();
        if (e.getComponentId().equals("ticketfecharmember")) {
            textChannel.getHistoryFromBeginning(1).queue(history ->
            {
                if (!history.isEmpty())
                {
                    String id = history.getRetrievedHistory().get(0).getContentRaw().replaceAll("<@", "").replaceAll(">", "");
                    if (e.getMember().getId().equals(id)) {
                        if (deleting.contains(e.getChannel().getIdLong())) {
                            e.reply("Aguarde, este ticket já está sendo fechado!").setEphemeral(true).queue();
                            return;
                        }
                        e.reply("**Finalizando ticket em 5 segundos.** \n*Obs: Iniciado pelo autor do ticket.*").queue();
                        deleting.add(e.getChannel().getIdLong());
                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                        scheduler.schedule(() -> {deleteTicket(e.getChannel().asTextChannel());}, 5, TimeUnit.SECONDS);
                    } else if(e.getMember().getRoles().contains(e.getGuild().getRoleById(cargoEquipeId))) {
                        if (deleting.contains(e.getChannel().getIdLong())) {
                            e.reply("Aguarde, este ticket já está sendo fechado!").setEphemeral(true).queue();
                            return;
                        }
                        e.reply("**Finalizando ticket em 5 segundos.** \n*Obs: Iniciado por: " +e.getMember().getEffectiveName()+"*.").queue();
                        deleting.add(e.getChannel().getIdLong());
                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                        scheduler.schedule(() -> {deleteTicket(e.getChannel().asTextChannel());}, 5, TimeUnit.SECONDS);
                    } else {
                        e.reply("Sem permissão.").setEphemeral(true).queue();
                    }
                    //textChannel.sendMessage(firstMsg.toString()).queue();
                }
                else
                    textChannel.sendMessage("No history for this channel!").queue();
            });

            return;
        }

        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(cargoEquipeId))) return;
        if (e.getComponentId().equals("ticketfecharok")) {
            if (deleting.contains(e.getChannel().getIdLong())) {
                e.reply("Aguarde, este ticket já está sendo fechado!").setEphemeral(true).queue();
                return;
            }
            e.getMessage().delete().queue();
            e.getChannel().sendMessage("**Finalizando ticket em 5 segundos.**").queue();
            deleting.add(e.getChannel().getIdLong());
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {deleteTicket(e.getChannel().asTextChannel());}, 5, TimeUnit.SECONDS);
        } else if (e.getComponentId().equals("ticketfecharcancel")) {
            e.getMessage().delete().queue();
            e.reply("Operação cancelada.").setEphemeral(true).queue();
        }
    }




    public void deleteTicket(TextChannel textChannel) {
        if (textChannel == null) return;
        textChannel.getHistoryFromBeginning(1).queue(history ->
        {
            if (!history.isEmpty())
            {
                Message firstMsg = history.getRetrievedHistory().get(0);
                textChannel.getGuild().retrieveMemberById(Long.parseLong(firstMsg.getContentRaw()
                        .replaceAll("<@", "").replaceAll(">", ""))).queue(member -> {
                    if (member != null) {
                        if (member.getRoles().contains(textChannel.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_TICKET_ID")))) {
                            System.out.println("Removendo cargo Ticket de " + member.getEffectiveName());
                            textChannel.getGuild().removeRoleFromMember(member, textChannel.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_TICKET_ID"))).queue();
                        }
                    }
                        });

                //textChannel.sendMessage(firstMsg.toString()).queue();
            }
            else
                textChannel.sendMessage("No history for this channel!").queue();
        });
        deleting.remove(textChannel.getIdLong());
        textChannel.delete().queue();
    }

}
