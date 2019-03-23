import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.CommandResult;
import me.bhop.bjdautilities.command.CommandTemplate;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.annotation.Usage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class Testing {
    private static JDA jda;

    public static void main(String[] args) throws Exception {
        jda = new JDABuilder(AccountType.BOT).setToken(args[0]).build();

        CommandHandler handler = new CommandHandler(jda);
        handler.register(new TestCommand());
        handler.register(new TestChild());

    }

    @Command(label = "test", children = TestChild.class)
    private static class TestCommand {
        @Execute
        public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
            new ReactionMenu.Builder(jda)
                    .setMessage("Test reaction menu!")
                    .onClick("\uD83D\uDD04", menu -> {
                        if (!menu.data.containsKey("count"))
                            menu.data.put("count", 1);
                        menu.getMessage().setContent("I have been updated. You have updated me " + menu.data.get("count") + " times!");
                        menu.data.put("count", ((int) menu.data.get("count")) + 1);
//                        menu.getMessage().setContent("test");
                    })
                    .buildAndDisplay(channel);
            return CommandResult.SUCCESS;
        }

        @Usage
        public void sendUsage(Member member, TextChannel channel, Message message, String label, List<String> args) {
            System.out.println("USAGE");
        }
    }

    @Command(label = "child", minArgs = 1)
    private static class TestChild {
        @Execute
        public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
            System.out.println("EXECUTE CHILD");
            return CommandResult.SUCCESS;
        }

        @Usage
        public void sendUsage(Member member, TextChannel channel, Message message, String label, List<String> args) {
            System.out.println("USAGE CHILD");
        }
    }
}
