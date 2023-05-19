package me.caua.endercraft.esterbot.tickets;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class TicketSetup {

    public static EmbedBuilder ticketEmbed = new EmbedBuilder()
            .setColor(new Color(176, 7, 227))
            .setThumbnail("https://i.imgur.com/7MWP40P.png")
            .setTitle(":mailbox: Central de atendimento")
            .setDescription("Olá, seja bem-vindo a central de atendimento da **EnderCraft**, abaixo vamos listar os tipos de atendimento disponíveis para uso.\n" +
                    "\n" +
                    "**\uD83D\uDCE8 Atendimento via chamado** \n" +
                    "- Selecione abaixo qual departamento está relacionado a sua dúvida e será gerado um canal de texto privado para que seu atendimento seja realizado.\n" +
                    "\n" +
                    "**\uD83D\uDDE3️ Atendimento via voz** \n" +
                    "- Dsponível para jogadores VIPs que possuem suporte prioritário, ou quando for solicitado pela nossa equipe.\n")
            .setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png")
            .addField("**\uD83C\uDF0E Links**", "[Site](https://ecbr.me/site)\n[Discord](https://ecbr.me/disc)", true)
            .addField("**⏲ Horário de Atendimento**", "10:00 às 21:00", true)
            ;

    // The string here is the id later used to identify the menu on use. Make it unique for each menu.

    public static void createTicket(ModalInteractionEvent interaction, String type, String categoryId, List<ModalMapping> values) {
        Guild guild = EsterBOT.jda.getGuildById(Objects.requireNonNull(EsterBOT.dotenv.get("GUILD_ID")));
        assert guild != null;
        Member member = interaction.getMember();

            guild.getCategoryById(categoryId)
                    .createTextChannel("ticket│"+member.getEffectiveName())
                    .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION), null)
                    .addRolePermissionOverride(Long.parseLong(EsterBOT.dotenv.get("CARGO_EQUIPE_ID")), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue(

                            textChannel -> {
                                interaction.reply("Seu ticket foi criado com sucesso. " + textChannel.getAsMention()).setEphemeral(true).queue();
                                textChannel.sendMessage(member.getAsMention()).queue();
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setColor(new Color(176, 7, 227))
                                        .setThumbnail("https://i.imgur.com/7MWP40P.png")
                                        .setTitle("<:ZeroTwoGreeting:884744365711454238> Bem vindo ao seu ticket.")
                                        .setDescription("Olá! Seu ticket foi criado com sucesso.\nAgora é só aguardar que nossa equipe irá lhe atender o mais rápido possível")
                                        .setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png");
                                for (ModalMapping modalMap : values) {
                                    eb.addField("**"+modalMap.getId()+"**", "```"+modalMap.getAsString()+"```", false);
                                }
                                textChannel.sendMessageEmbeds(eb.build()).addActionRow(
                                        Button.primary("ticketfecharmember", Emoji.fromUnicode("📩")).withLabel("Fechar ticket"),
                                        Button.link("https://ecbr.me/site", "Site").withEmoji(Emoji.fromUnicode("🌍"))).queue();
                            }
                    );
    }


}
