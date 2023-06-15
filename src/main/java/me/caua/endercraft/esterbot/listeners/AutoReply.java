package me.caua.endercraft.esterbot.listeners;

import me.caua.endercraft.esterbot.commands.IPCmd;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoReply extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (!e.isFromGuild()) return;
        String msg = e.getMessage().getContentRaw();

        // to do
        if (msg.contains("qual") && msg.contains("ip")) {
            e.getMessage().replyEmbeds(IPCmd.sendIPEmbed().build()).queue();
        } else if ((msg.contains("erro") || msg.contains("error")) && (msg.contains("entrar") || msg.contains("entra"))) {
            e.getMessage().reply("Por favor, verifique se está digitando nosso IP corretamente.\n\n**IP JAVA (PC):** endercraft.com.br\n**IP Bedrock (Celular/PC/Console):** bed.endercraft.com.br\n**Porta Bedrock:** 19132\n\nCaso o problema continue, abra um ticket no canal <#873654817531256848>.").queue();
        } else if ((msg.contains("não") || msg.contains("n") || msg.contains("nao")) && (msg.contains("entrar") || msg.contains("entra")) && (msg.contains("consigo") || msg.contains("conseguindo") || msg.contains("consiguo"))) {
            e.getMessage().reply("Por favor, verifique se está digitando nosso IP corretamente.\n\n**IP JAVA (PC):** endercraft.com.br\n**IP Bedrock (Celular/PC/Console):** bed.endercraft.com.br\n**Porta Bedrock:** 19132\n\nCaso o problema continue, abra um ticket no canal <#873654817531256848>.").queue();
        } else if ((msg.contains("vaga") || msg.contains("vagas")) && (msg.contains("equipe") || msg.contains("staf") || msg.contains("staff"))) {
            e.getMessage().reply("Caso deseje fazer parte da nossa equipe, preencha nosso formulário em: https://ecbr.me/staff").queue();
        } else if ((msg.contains("como") || msg.contains("faz")) && (msg.contains("entrar") || msg.contains("entra")) && (msg.contains("server") || msg.contains("servido") || msg.contains("servidor"))) {
            e.getMessage().reply("Para entrar no servidor, utilize nosso IP disponível no canal <#887782024138621008> ou assista ao vídeo tutorial https://www.youtube.com/watch?v=UWj_mdwtIO0").queue();
        } else if ((msg.contains("como") || msg.contains("faz")) && (msg.contains("usa") || msg.contains("usar") || msg.contains("abrir") || msg.contains("abre") || msg.contains("abro")) && (msg.contains("caixa") || msg.contains("chave") || msg.contains("caixas") || msg.contains("chaves"))) {
            e.getMessage().reply("Para abrir caixas, basta ir ao /spawn, colocar a chave da caixa em sua mão e clicar na caixa usando a chave.").queue();
        } else if ((msg.contains("como") || msg.contains("faz") || msg.contains("qual") || msg.contains("site")) && (msg.contains("compra") || msg.contains("comprar") || msg.contains("adquirir") || msg.contains("compro") || msg.contains("pegar")) && (msg.contains("vip") || msg.contains("endergold") || msg.contains("vips") || msg.contains("ender") || msg.contains("dragon"))) {
            e.getMessage().reply("Para adquirir VIP ou Endergold, acesse nosso site https://endercraft.com.br \nA ativação de todos os produtos é automática!").queue();
        } else if ((msg.contains("como") || msg.contains("faz") || msg.contains("qual")) && (msg.contains("salva") || msg.contains("volta") || msg.contains("volto") || msg.contains("voltar") || msg.contains("salvar") || msg.contains("teleportar")) && (msg.contains("casa") || msg.contains("base") || msg.contains("cama") || msg.contains("terreno"))) {
            e.getMessage().reply("Para salvar um local use o comando /sethome <nome> e para voltar até ele o comando /home <nome>").queue();
        }
    }

}
