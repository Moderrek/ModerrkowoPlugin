package pl.moderr.moderrkowo.core.commands.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class RegulaminCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtils.color("&cRegulamin MODERRKOWO.PL"));
        sender.sendMessage(ColorUtils.color("&c1. &eNa serwerze jeden gracz może posiadać 1 działkę na danej mapie."));
        sender.sendMessage(ColorUtils.color("&c2. &eZakaz przeklinania oraz ubliżania innym graczom."));
        sender.sendMessage(ColorUtils.color("&c3. &eZakaz spamowania."));
        sender.sendMessage(ColorUtils.color("&c4. &eZakaz reklamowania innych serwerów (przykładowo, podawanie adresów IP, nazw itp)."));
        sender.sendMessage(ColorUtils.color("&c5. &eNie popieramy griefowania/kradzieży."));
        sender.sendMessage(ColorUtils.color("&c6. &eZakaz wykorzystywania bugów gry/pluginów."));
        sender.sendMessage(ColorUtils.color("&c7. &eHandel z graczami odbywa się na własne ryzyko."));
        sender.sendMessage(ColorUtils.color("&c8. &eZakaz podawania się za członka administracji serwera oraz powoływania się na znajomości przez nieuprawnionych graczy."));
        sender.sendMessage(ColorUtils.color("&c9. &eZakazane są: nękanie innych graczy oraz groźby nie związane z grą."));
        sender.sendMessage(ColorUtils.color("&c10. &eZakaz pisania nagminnych próśb do administracji o itemy"));
        sender.sendMessage(ColorUtils.color("&c11. &eTP-Kill'e są zakazane"));
        sender.sendMessage(ColorUtils.color("&c12. &eZakaz nadużywania dużych liter (Caps Lock)"));
        sender.sendMessage(ColorUtils.color("&c13. &eAdmin ma zawsze racje."));
        return false;
    }
}
