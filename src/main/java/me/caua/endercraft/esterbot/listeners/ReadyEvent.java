package me.caua.endercraft.esterbot.listeners;

import me.caua.endercraft.esterbot.EsterBOT;
import me.caua.endercraft.esterbot.status.StatusChecker;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class ReadyEvent extends ListenerAdapter {
    public static StatusChecker statusChecker;
    @Override
    public void onReady(@NotNull net.dv8tion.jda.api.events.session.ReadyEvent e) {
        Guild guild = e.getJDA().getGuildById(EsterBOT.dotenv.get("GUILD_ID"));
        assert guild != null;
        guild.updateCommands().addCommands(
                Commands.slash("revisao", "Solicite análise de sua punição.")
                        .addOption(OptionType.INTEGER, "id", "ID da sua punição.", true)
                        .addOption(OptionType.STRING, "nick", "Escreva seu nick.", true)
                        .addOption(OptionType.STRING, "motivo_da_punicao", "Escreva o motivo da sua punição.", true)
                        .addOption(OptionType.STRING, "por_que_deve_ser_desbanido", "Escreva o motivo de por quê deve ser desbanido.", true),
                Commands.slash("pix", "Realize um pagamento via PIX."),
                Commands.slash("ip", "Veja o IP e informações do servidor.")
        ).queue();

    }

}
