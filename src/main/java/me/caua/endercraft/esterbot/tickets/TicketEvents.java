package me.caua.endercraft.esterbot.tickets;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TicketEvents extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e) {
        if (e.getComponentId().equals("ticket")) {

            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_TICKET_ID")))) {
                e.reply("Você já possui um ticket aberto!").setEphemeral(true).queue();
                return;
            }

            List<String> selected = e.getValues();

            if (selected.get(0).equals("ticket:denuncia")) {
                e.replyModal(getModal("denuncia")).queue();
            } else if (selected.get(0).equals("ticket:revisao")) {
                e.replyModal(getModal("revisao")).queue();
            } else if (selected.get(0).equals("ticket:financeiro")) {
                e.replyModal(getModal("financeiro")).queue();
            } else if (selected.get(0).equals("ticket:tecnico")) {
                e.replyModal(getModal("tecnico")).queue();
            } else if (selected.get(0).equals("ticket:geral")) {
                e.replyModal(getModal("geral")).queue();
            }

        }
    }

    public void onModalInteraction(ModalInteractionEvent e){
        if (e.getModalId().equalsIgnoreCase("ticketfinanceiro")) {
            TicketSetup.createTicket(e, "financeiro", EsterBOT.dotenv.get("CATEGORY_FINANCEIRO_ID"), e.getValues());
        } else if (e.getModalId().equalsIgnoreCase("ticketdenuncia")) {
            TicketSetup.createTicket(e, "denuncia", EsterBOT.dotenv.get("CATEGORY_DENUNCIAS_ID"), e.getValues());
        } else if (e.getModalId().equalsIgnoreCase("ticketgeral")) {
            TicketSetup.createTicket(e, "geral", EsterBOT.dotenv.get("CATEGORY_GERAL_ID"), e.getValues());
        } else if (e.getModalId().equalsIgnoreCase("tickettecnico")) {
            TicketSetup.createTicket(e, "financeiro", EsterBOT.dotenv.get("CATEGORY_TECNICO_ID"), e.getValues());
        } else if (e.getModalId().equalsIgnoreCase("ticketrevisao")) {
            sendRevisao(e);
            return;
        }
        e.getGuild().addRoleToMember(e.getUser(), e.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_TICKET_ID"))).queue();
    }


    public Modal getModal(String type) {
        if (type.equals("geral")) {
            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput body = TextInput.create("Motivo", "Descreva o motivo do contato", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Descreva o motivo do seu contato e como podemos ajudá-lo.")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .build();

            Modal modal = Modal.create("ticketgeral", "Abrir Ticket")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(body))
                    .build();

            return modal;
        } else if (type.equals("denuncia")) {
            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput denunciado = TextInput.create("Denunciado", "Jogador denunciado", TextInputStyle.SHORT)
                    .setPlaceholder("Nick do jogador denunciado")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput body = TextInput.create("Motivo", "Motivo da denúncia e provas", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Por favor, descreva o motivo da denúncia e envie todas as provas que possuir.")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .build();

            Modal modal = Modal.create("ticketdenuncia", "Denunciar jogador")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(denunciado), ActionRow.of(body))
                    .build();

            return modal;
        } else if (type.equals("revisao")) {
            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();

            TextInput id = TextInput.create("IDPunicao", "ID da Punição", TextInputStyle.SHORT)
                    .setPlaceholder("Digite o ID da sua punição")
                    .setMinLength(1)
                    .setMaxLength(10) // or setRequiredRange(10, 100)
                    .build();

            TextInput motivobanimento = TextInput.create("MotivoDoBanimento", "Por que você foi banido?", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Digite o por que você foi banido")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            TextInput body = TextInput.create("Motivo", "Por que acredita que a punição esteja errada?", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Digite o por que acredita que a punição foi aplicada incorretamente.")
                    .setMinLength(1)
                    .setMaxLength(300)
                    .build();

            Modal modal = Modal.create("ticketrevisao", "Denunciar jogador")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(id), ActionRow.of(motivobanimento), ActionRow.of(body))
                    .build();

            return modal;
        } else if (type.equals("financeiro")) {
            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();
            TextInput email = TextInput.create("Email", "Seu e-mail", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu e-mail")
                    .setMinLength(3)
                    .setMaxLength(255) // or setRequiredRange(10, 100)
                    .build();


            TextInput body = TextInput.create("Motivo", "Descreva o motivo do contato", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Descreva o motivo do seu contato e como podemos ajudá-lo.")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .build();

            Modal modal = Modal.create("ticketfinanceiro", "Abrir Ticket")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(email), ActionRow.of(body))
                    .build();

            return modal;
        } else if (type.equals("tecnico")) {
            TextInput nick = TextInput.create("Nick", "Seu nick", TextInputStyle.SHORT)
                    .setPlaceholder("Digite seu nick")
                    .setMinLength(3)
                    .setMaxLength(30) // or setRequiredRange(10, 100)
                    .build();


            TextInput body = TextInput.create("Motivo", "Descreva o motivo do contato", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Descreva o motivo do seu contato e como podemos ajudá-lo.")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .build();

            Modal modal = Modal.create("tickettecnico", "Abrir Ticket")
                    .addActionRows(ActionRow.of(nick), ActionRow.of(body))
                    .build();

            return modal;
        }
        return null;
    }

    private HashMap<Member, Long> cooldown = new HashMap<>();
    EmbedBuilder eb = new EmbedBuilder();
    EmbedBuilder eb2 = new EmbedBuilder();
    EmbedBuilder eb3 = new EmbedBuilder();
    EmbedBuilder eb4 = new EmbedBuilder();
    private ArrayList<Integer> ids = new ArrayList<>();
    String nick;


    public void sendRevisao(ModalInteractionEvent e) {
        if (e.getModalId().equals("ticketrevisao")) {

            String idStr = e.getValues().get(1).getAsString();
            String nick = e.getValues().get(0).getAsString();
            String causa = e.getValues().get(2).getAsString();
            String motivo = e.getValues().get(3).getAsString();
            Integer id;

            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException nfe) {
                e.reply("❌ ID da punição inválido, tente novamente.").setEphemeral(true).queue();
                return;
            }

            if (ids.contains(id)) {
                e.reply("❌ Uma revisão para esta punição já foi enviada.").setEphemeral(true).queue();
                return;
            }
            if (nick.length() > 30 || nick.length() < 3) {
                e.reply("❌ Por favor, digite um nick válido.").setEphemeral(true).queue();
                return;
            }
            if (causa.length() > 100) {
                e.reply("❌ O motivo da punição não pode ser maior que 100 caracteres.").setEphemeral(true).queue();
                return;
            }
            if (motivo.length() > 300) {
                e.reply("❌ O motivo do desbanimento não pode ser maior que 300 caracteres.").setEphemeral(true).queue();
                return;
            }

            if (cooldown.containsKey(e.getMember()) && !(System.currentTimeMillis() >= cooldown.get(e.getMember()))) {
                e.reply("❌ Você deve aguardar mais " +converter(e.getMember())+ " minutos para enviar uma nova revisão.\n\n**Atenção!** Não envie revisões repetidas (da mesma punição), sujeito a banimento do discord.").setEphemeral(true).queue();
                return;
            } else cooldown.remove(e.getMember());
            cooldown.put(e.getMember(), System.currentTimeMillis() + TimeUnit.HOURS.toMillis(4));

            eb.setTitle("Nova revisão: Pendente");
            eb.setDescription("Enviada por: " + e.getMember().getNickname());
            eb.setTimestamp(e.getTimeCreated());
            eb.setThumbnail(Objects.requireNonNull(e.getMember()).getEffectiveAvatarUrl());
            eb.setFooter(e.getUser().getId(), e.getMember().getEffectiveAvatarUrl());
            eb.addField("ID", String.valueOf(id), true);
            eb.addField("Nick", nick, true);
            eb.addField("Motivo da punição", causa, false);
            eb.addField("Por que deve ser desbanido?", motivo, false);
            eb.setColor(Color.BLUE);

            e.getJDA().getTextChannelById(EsterBOT.dotenv.get("REVISAO_CHANNEL_ID")).sendMessageEmbeds(eb.build()).queue(message -> {
                message.addReaction(Emoji.fromUnicode("✅")).queue();
                message.addReaction(Emoji.fromUnicode("❌")).queue();
            });
            eb.clear();
            e.replyEmbeds(new EmbedBuilder()
                            .setTitle("✅ **Sua revisão foi enviada!**")
                            .setThumbnail(e.getUser().getEffectiveAvatarUrl())
                            .setTimestamp(LocalDateTime.now())
                            .setColor(Color.GREEN)
                            .setDescription("Agora basta aguardar que nossa Equipe irá analisar. Você receberá o resultado em seu Privado.\n\n*Lembrando que é necessário estar com a opção \"Permitir mensagens diretas de membros do servidor\" ativada nas configurações de privacidade da sua conta do Discord, para conseguir receber mensagens do Bot.*")
                    .build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (!e.getChannel().getId().equals(EsterBOT.dotenv.get("REVISAO_CHANNEL_ID"))) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(EsterBOT.dotenv.get("CARGO_EQUIPE_ID")))) return;
        if (Objects.requireNonNull(e.getUser()).isBot()) return;

        if (e.getReaction().getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase("U+2705")) {
            e.getChannel().retrieveMessageById(e.getMessageId()).queue( (message -> {
                MessageEmbed me = message.getEmbeds().get(0);
                System.out.println(3);
                eb2.setColor(Color.CYAN);
                eb2.setThumbnail(Objects.requireNonNull(me.getThumbnail()).getUrl());
                eb2.setDescription("Revisão aprovada! Agora desbana o jogador e depois reaja com \uD83D\uDD13.");
                for (MessageEmbed.Field field : me.getFields()) {
                    eb2.addField(field);
                    if (field.getName().equalsIgnoreCase("nick")) {
                        this.nick = field.getValue();
                    }
                }
                eb2.addField("Aprovada por", e.getMember().getEffectiveName(), true);
                eb2.setFooter(me.getFooter().getText(), me.getFooter().getIconUrl());
                eb2.setTimestamp(me.getTimestamp());
                eb2.setTitle("Aprovada: Aguardando desbanimento");
                message.editMessageEmbeds(eb2.build()).queue();
                e.getReaction().removeReaction(e.getUser()).queue();
                for (MessageReaction reaction : message.getReactions()) {
                    reaction.removeReaction().queue();
                }
                eb2.clear();
                message.addReaction(Emoji.fromUnicode("\uD83D\uDD13")).queue();
            }) );
        }

        if (e.getReaction().getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase("U+274c")) {
            e.getChannel().retrieveMessageById(e.getMessageId()).queue( (message -> {
                MessageEmbed me = message.getEmbeds().get(0);

                eb3.setColor(Color.RED);
                eb3.setThumbnail(me.getThumbnail().getUrl());
                eb3.setDescription("Revisão negada! O jogador foi informado.");
                for (MessageEmbed.Field field : me.getFields()) {
                    eb3.addField(field);
                    if (field.getName().equalsIgnoreCase("nick")) {
                    }
                }
                eb3.addField("Negada por", e.getMember().getNickname(), true);
                eb3.setFooter(me.getFooter().getText(), me.getFooter().getIconUrl());
                eb3.setTimestamp(me.getTimestamp());
                eb3.setTitle("Revisão: Negada");
                message.editMessageEmbeds(eb3.build()).queue();
                e.getReaction().removeReaction(e.getUser()).queue();
                for (MessageReaction reaction : message.getReactions()) {
                    reaction.removeReaction().queue();
                }
                e.getGuild().retrieveMemberById(eb3.build().getFooter().getText()).queue(member -> {
                    member.getUser().openPrivateChannel().queue( (privateChannel -> {
                        eb3.clear();
                        eb3.setColor(Color.red);
                        eb3.setTitle("Revisão");
                        eb3.setThumbnail(e.getUser().getEffectiveAvatarUrl());
                        eb3.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
                        eb3.setTimestamp(me.getTimestamp());
                        eb3.setDescription("Olá " + privateChannel.getUser().getAsMention() + "! \nInfelizmente a sua revisão de punição foi **negada**.\n\n**Por que isto aconteceu?**\nA revisão de punição é somente para punições que foram aplicadas incorretamente, sendo assim, a sua punição está correta.");
                        privateChannel.sendMessageEmbeds(eb3.build()).queue();
                    }));
                });
            }) );
        }

        if (e.getReaction().getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase("U+1f513")) {
            e.getChannel().retrieveMessageById(e.getMessageId()).queue( (message -> {
                MessageEmbed me = message.getEmbeds().get(0);

                eb4.setColor(Color.GREEN);
                eb4.setThumbnail(me.getThumbnail().getUrl());
                eb4.setDescription("Revisão aprovada! O jogador foi informado.");
                for (MessageEmbed.Field field : me.getFields()) {
                    eb4.addField(field);
                    if (field.getName().equalsIgnoreCase("nick")) {
                    }
                }
                eb4.setFooter(me.getFooter().getText(), me.getFooter().getIconUrl());
                eb4.setTimestamp(me.getTimestamp());
                eb4.setTitle("Revisão: Aprovada");
                message.editMessageEmbeds(eb4.build()).queue();
                e.getReaction().removeReaction(e.getUser()).queue();
                for (MessageReaction reaction : message.getReactions()) {
                    reaction.removeReaction().queue();
                }
                e.getGuild().retrieveMemberById(eb4.build().getFooter().getText()).queue(member -> {
                    member.getUser().openPrivateChannel().queue( (privateChannel -> {
                        eb4.clear();
                        eb4.setColor(Color.green);
                        eb4.setTitle("Revisão");
                        eb4.setThumbnail(e.getUser().getEffectiveAvatarUrl());
                        eb4.setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
                        eb4.setTimestamp(me.getTimestamp());
                        eb4.setDescription("Olá " + privateChannel.getUser().getAsMention() + "! \nA sua revisão de punição foi **aprovada**!\n\nVocê já pode acessar nosso servidor novamente! \nAgradecemos a paciência e compreensão.");
                        privateChannel.sendMessageEmbeds(eb4.build()).queue();
                    }) );
                });
            }) );
        }

    }

    private Long converter(Member m) {
        long tempo = System.currentTimeMillis() - cooldown.get(m);
        return TimeUnit.MILLISECONDS.toMinutes(tempo) * -1;
    }

}
