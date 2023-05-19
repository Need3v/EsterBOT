package me.caua.endercraft.esterbot.commands;

import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class SayeCmd extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (Objects.requireNonNull(e.getMessage().getAuthor()).isBot()) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(Objects.requireNonNull(EsterBOT.dotenv.get("CARGO_EQUIPE_ID"))))) return;

        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder text = new StringBuilder();
        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "saye")) {
                boolean hasMention = false;
                String mention = "";
                if (args.length == 1) {
                    e.getMessage().reply("**Utilize:** !saye <mensagem>\n\n**Inclua as opções abaixo em qualquer lugar da mensagem para customizar:**\n\nmention:(id do usuario/cargo)\nimg:(url da imagem)\ntitle:(titulo da publicacao, use _ para espaços)\n\n**Exemplo:**\n!saye Olá jogador! Este é um anúncio importate. title:Anuncio_Importante mention:1102192750272720963").queue();
                    return;
                }
                for (String arg : args) {
                    if (!arg.equalsIgnoreCase(prefix + "saye")) {
                        if (arg.contains("mention:")) {
                            mention = arg.replace("mention:", "");
                            hasMention = true;
                        } else if (arg.contains("title:")) {
                            eb.setTitle(arg.replace("title:", "").replaceAll("_", " "));
                        } else if (arg.contains("img:")) {
                            String url = arg.replace("img:", "");
                            System.out.println(url);
                            eb.setImage(url);
                        } else {
                            text.append(arg).append(" ");
                        }
                    }
                }
            eb.setColor(new Color(176, 7, 227));
            eb.setDescription(text.toString());
            e.getMessage().delete().queue();
            if (hasMention) {

                if (mention.equalsIgnoreCase("everyone")) {
                    e.getChannel().sendMessage("@everyone").queue(message -> message.editMessageEmbeds(eb.build()).queue());
                } else if (mention.equalsIgnoreCase("here")) {
                    e.getChannel().sendMessage("@here").queue(message -> message.editMessageEmbeds(eb.build()).queue());
                } else if (e.getGuild().getRoleById(mention) != null) {
                    e.getChannel().sendMessage("<@&"+mention+">").queue(message -> message.editMessageEmbeds(eb.build()).queue());
                } else if (e.getGuild().getMemberById(mention) != null) {
                    e.getChannel().sendMessage("<@"+mention+">").queue(message -> message.editMessageEmbeds(eb.build()).queue());
                }
            } else {
                e.getChannel().sendMessageEmbeds(eb.build()).queue();
            }
        }
    }
}
